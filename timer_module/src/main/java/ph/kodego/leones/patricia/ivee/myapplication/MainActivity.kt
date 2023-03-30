package ph.kodego.leones.patricia.ivee.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import ph.kodego.leones.patricia.ivee.myapplication.MainActivity.TimerMode.*
import ph.kodego.leones.patricia.ivee.myapplication.databinding.ActivityMainBinding
import ph.kodego.leones.patricia.ivee.myapplication.util.NotificationUtil
import ph.kodego.leones.patricia.ivee.myapplication.util.PrefUtil
import ph.kodego.leones.patricia.ivee.myapplication.util.PrefUtil.Companion.getTaskName
import ph.kodego.leones.patricia.ivee.myapplication.util.PrefUtil.Companion.getTimerCycles
import ph.kodego.leones.patricia.ivee.myapplication.util.PrefUtil.Companion.getTimerMode
import ph.kodego.leones.patricia.ivee.myapplication.util.PrefUtil.Companion.setTimerMode
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class TimerState{
        STOPPED,
        PAUSED,
        RUNNING,
        COMPLETED
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.STOPPED
    private var timerMode = FOCUS
    //when task is completed it will be moved to completed tasks. Not sure if this is still necessary
    private var isTaskCompleted = false
    private var secondsRemaining: Long = 0
    //focusTimerCount is supposed to be a var for the completed focus time (HOw many focus time
    // is completed )
    private var focusTimerCount = 0
    private var playLongBreak = 0
    private var cycles = 0
    var currentCycle = 0
    private var onTaskCompleteListener: OnTaskCompleteListener? = null
    private var taskStartTime: Long = 0

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "      Timer"

        setTimerMode(FOCUS, this)
        bindItems()

        binding.btnPlay.setOnClickListener{v ->
            startTimer()
            timerState =  TimerState.RUNNING
            updateButtons()
        }

        binding.btnPause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.PAUSED
            updateButtons()
        }

        binding.btnStop.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()
        removeAlarm(this)

        //hide notification
        NotificationUtil.hideTimerNotification(this)

        // Updates the name of the task if you want from the settings preferences
        val taskName = PrefUtil.getTaskName(this)

        binding.textTaskName.text = taskName ?: ""
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.RUNNING){
            timer.cancel()
            //run background timer
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            //show notification
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        }
        else if (timerState == TimerState.PAUSED){
            //show notification
            NotificationUtil.showTimerPaused(this)
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }



    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this)

        getTimerMode(this)
        Log.d("initTimer", "timer mode is ${timerMode}")
        Log.d("initTimer", "get timer mode is ${getTimerMode(this)}")
        Log.d("initTimer","current cycle is $currentCycle")

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.STOPPED)

            setTimerLength()

        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        //resume where we left off

        if (alarmSetTime > 0){
            secondsRemaining -= nowSeconds - alarmSetTime
        }

        if (secondsRemaining <= 0){
            onTimerFinished()
        } else if (timerState == TimerState.RUNNING){
            startTimer()
        }

        updateButtons()
        updateCountdownUI()
    }

    private val completedTimes = ArrayList<Long>()
    private fun onTimerFinished(){

        timerState = TimerState.STOPPED
        if (timerMode == FOCUS){
            val focusTimerCompleted = System.currentTimeMillis()
            completedTimes.add(focusTimerCompleted)
            val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(Date(focusTimerCompleted))
            Log.d("onTimerFinished", "$timerMode end at $formattedTime")
            Log.d("onTimerFinished", "$timerMode end at $focusTimerCompleted")
//            Log.d("onTimerFinished", "$timerMode end at $focusTimerCompleted")
        }
        getTimerMode(this)
        Log.d("onTimerFinished", "$timerMode Finished")
        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        switchTimer()

        bindItems()

        setTimerLength()

        taskDone()

        binding.progressBarTimer.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private val startTimes = ArrayList<Long>()
    private fun startTimer(){
        timerState = TimerState.RUNNING
        if (timerMode == FOCUS){
            val focusTimerStarted = System.currentTimeMillis() // record start time
            startTimes.add(focusTimerStarted) // add start time to array list
            val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(Date(focusTimerStarted))
            Log.d("startTimer", "$timerMode start at $formattedTime")
            Log.d("startTimer", "$timerMode start at $focusTimerStarted")
        }


        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }

    private fun setShortBreakTimerLength(){
        val lengthInMinutes = PrefUtil.getShortBreakTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }
    private fun setLongBreakTimerLength(){
        val lengthInMinutes = PrefUtil.getLongBreakTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.textTimer.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        binding.progressBarTimer.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons(){
        when (timerState) {
            TimerState.RUNNING ->{
                binding.btnPlay.isEnabled = false
                binding.btnPause.isEnabled = true
                binding.btnStop.isEnabled = true
            }
            TimerState.STOPPED -> {
                binding.btnPlay.isEnabled = true
                binding.btnPause.isEnabled = false
                binding.btnStop.isEnabled = false
            }
            TimerState.PAUSED -> {
                binding.btnPlay.isEnabled = true
                binding.btnPause.isEnabled = false
                binding.btnStop.isEnabled = true
            }

            TimerState.COMPLETED -> {
                binding.btnPlay.isEnabled = true
                binding.btnPause.isEnabled = false
                binding.btnStop.isEnabled = false
            }
        }
    }

    enum class TimerMode{
        FOCUS,
        SHORT_BREAK,
        LONG_BREAK
    }
    //This is supposed to switch the timer if for example the focus time is finished and now
    //it's either going to be a short break or a long break

    private fun setTimerLength() {
        Log.d("SET TIMER LENGTH","timer mode is ${timerMode}")
        Log.d("SET TIMER LENGTH","get timer mode is ${getTimerMode(this) }")
        when(timerMode) {
            FOCUS -> setNewTimerLength()
            SHORT_BREAK -> setShortBreakTimerLength()
            LONG_BREAK -> setLongBreakTimerLength()
        }

    }

    private fun switchTimer() {
        Log.d("SWITCH TIMER","${timerMode}")
        Log.d("SWITCH TIMER","switch timer mode is ${getTimerMode(this) }")

        when (timerMode) {
            FOCUS -> {
                currentCycle++
                Log.d("SWITCH TIMER","current cycle is $currentCycle")
                val longBreakRepetition = PrefUtil.getLongBreakRepetition(this)
                if (longBreakRepetition > 0 && currentCycle % longBreakRepetition == 0) {
                    timerMode = LONG_BREAK
                    setTimerMode(timerMode, this)
                }else{
                    timerMode = SHORT_BREAK
                    setTimerMode(timerMode, this)
                }

            }
            SHORT_BREAK -> {
                timerMode = FOCUS
                setTimerMode(timerMode, this)

            }
            LONG_BREAK -> {
                timerMode = FOCUS
                setTimerMode(timerMode, this)

            }
        }

    }


    private fun taskDone(){
        cycles = PrefUtil.getTimerCycles(this)
        if (currentCycle == cycles){
            isTaskCompleted = true
            timerState = TimerState.COMPLETED

            //COMPUTE THE TIME COMPLETED
            var totalTimeCompleted = 0L
            for (i in 0 until completedTimes.size) {
                val cycleTimeCompleted = completedTimes[i] - startTimes[i]
                totalTimeCompleted += cycleTimeCompleted

            }
            val hours = TimeUnit.MILLISECONDS.toHours(totalTimeCompleted)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeCompleted) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeCompleted) % 60

            //00:00:00 FORMAT
            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            //SOMEWHAT STRING FORMAT
            val timeCompletedFormatted = "$hours hours"

            Log.d("taskDone", "time completed $timeCompletedFormatted")
            Log.d("taskDone", "time completed $totalTimeCompleted")
            Log.d("taskDone", "time completed $formattedTime")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Timer Finished")
            builder.setMessage("Congratulations! \nYou've successfully finished your task.")
            builder.setMessage("time completed $timeCompletedFormatted")
            builder.setMessage("time completed $totalTimeCompleted")
            builder.setMessage("time completed $formattedTime")
            builder.setPositiveButton("OK") { dialog, which ->



                //PUT TIME COMPLETED IN ANOTHER MODULE
                val taskCompletionEvent = System.currentTimeMillis()
//                onTaskCompleteListener?.onTaskCompleted(taskCompletionEvent)
                //TODO: ADD or update the task in the database or firebase
                // Perform any action when the user clicks the OK button
                // For example, you can start a new timer or navigate to another activity
            }
            builder.show()
        }else {
            isTaskCompleted = false
        }
    }

    private fun bindItems() {
        val textStatus = getTimerMode(this).toString()
        binding.textTaskStatus.text = textStatus

        val taskName = getTaskName(this).toString()
        binding.textTaskName.text = taskName

        val cycles = getTimerCycles(this).toString()
        binding.textViewCycles.text = "$currentCycle / $cycles"

        val progressPercent = (currentCycle.toFloat()
                / getTimerCycles(this).toFloat()) * 100
        binding.progressBarCycles.progress = progressPercent.toInt()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setOnTaskCompleteListener(listener: OnTaskCompleteListener) {
        onTaskCompleteListener = listener
    }
}


//Make the progress bar into dots or some shape
//val maxDots = 5 // maximum number of dots to display in progress bar
//var originalCycles = 15 // original number of cycles set by user
//
//fun setNumberOfCycles(cycles: Int) {
//    if (cycles > maxDots) {
//        originalCycles = cycles
//        setupProgressBar(maxDots)
//    } else {
//        originalCycles = cycles
//        setupProgressBar(cycles)
//    }
//}
// create a custom drawable with a number of dots equal to the maximum number of dots,
// but store the original number of cycles in a separate variable in the drawable.
// For example:
//kotlin
//Copy code
//class CustomProgressBarDrawable(dotsCount: Int, dotSize: Int, defaultColor: Int) : Drawable() {
//    private var dotsCount = dotsCount
//    private var originalCycles = dotsCount
//    // other fields and methods here
//}

//fun setupProgressBar(cycles: Int) {
//    val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
//    val drawable = CustomProgressBarDrawable(dotsCount = maxDots, dotSize = 16, defaultColor = Color.GRAY)
//    drawable.originalCycles = originalCycles
//    progressBar.progressDrawable = drawable
//}

//val currentCycle = 3 // get current cycle number here
//val progress = currentCycle.toFloat() / originalCycles.toFloat() * maxDots
//drawable.setCurrentProgress(progress)




