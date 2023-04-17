package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ph.kodego.leones.patricia.ivee.pomodoroapplication.receiver.TimerExpiredReceiver
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentTimerBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.model.OnTaskCompleteListener
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.NotificationUtil
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PrefUtil
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

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

    enum class TimerMode{
        FOCUS,
        SHORT_BREAK,
        LONG_BREAK
    }

    enum class TimerState{
        STOPPED,
        PAUSED,
        RUNNING,
        COMPLETED
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerFragment.TimerState.STOPPED
    private var timerMode = TimerFragment.TimerMode.FOCUS

    private var isTaskCompleted = false
    private var secondsRemaining: Long = 0

    private var cycles = 0
    var currentCycle = 0
    private var onTaskCompleteListener: OnTaskCompleteListener? = null
    private var focusStartTime: Long = 0
    private var focusEndTime: Long = 0
    private val focusStartTimes = ArrayList<Long>()
    private val focusCompletedTimes = ArrayList<Long>()

//    val toolbar = (activity as AppCompatActivity).supportActionBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentTimerBinding.inflate(inflater, container, false)

//        toolbar?.title = "Timer"
//        toolbar?.setHomeAsUpIndicator(R.drawable.ic_settings)
//        toolbar?.setDisplayHomeAsUpEnabled(true)

        PrefUtil.setTimerMode(TimerMode.FOCUS, requireContext())
        bindItems()

        binding.btnPlay.setOnClickListener{v ->
            startTimer()
            timerState =  TimerState.RUNNING
            updateButtons()
        }

        binding.btnPause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerFragment.TimerState.PAUSED
            updateButtons()
        }

        binding.btnStop.setOnClickListener { v ->
            timer.cancel()
            onTimerFinished()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        initTimer()
        removeAlarm(requireContext())

        //hide notification
        NotificationUtil.hideTimerNotification(requireContext())

        // Updates the name of the task if you want from the settings preferences
        val taskName = PrefUtil.getTaskName(requireContext())

        binding.textTaskName.text = taskName ?: ""
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.RUNNING){
            timer.cancel()
            //run background timer
            val wakeUpTime = setAlarm(requireContext(), nowSeconds, secondsRemaining)
            //show notification
            NotificationUtil.showTimerRunning(requireContext(), wakeUpTime)
        }
        else if (timerState == TimerState.PAUSED){
            //show notification
            NotificationUtil.showTimerPaused(requireContext())
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext())
        PrefUtil.setSecondsRemaining(secondsRemaining, requireContext())
        PrefUtil.setTimerState(timerState, requireContext())
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(requireContext())

        PrefUtil.getTimerMode(requireContext())
        Log.d("initTimer", "timer mode is ${timerMode}")
        Log.d("initTimer", "get timer mode is ${PrefUtil.getTimerMode(requireContext())}")
        Log.d("initTimer","current cycle is $currentCycle")

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.STOPPED)

            setTimerLength()

        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED)
            PrefUtil.getSecondsRemaining(requireContext())
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(requireContext())
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


    private fun onTimerFinished(){

        timerState = TimerState.STOPPED
        if (timerMode == TimerMode.FOCUS){
            focusEndTime = System.currentTimeMillis()
//            val focusTimerCompleted = System.currentTimeMillis()
            focusCompletedTimes.add(focusEndTime)
            val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(Date(focusEndTime))

            Log.d("onTimerFinished", "$timerMode end at $formattedTime")
            Log.d("onTimerFinished", "$timerMode end at $focusEndTime")

        }
        PrefUtil.getTimerMode(requireContext())
        Log.d("onTimerFinished", "$timerMode Finished")
        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running

        switchTimer()

        bindItems()

        setTimerLength()

        taskDone()

        binding.progressBarTimer.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext())
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }


    private fun startTimer(){
        timerState = TimerState.RUNNING
        if (timerMode ==TimerMode.FOCUS){
            focusStartTime = System.currentTimeMillis() // record start time
            focusStartTimes.add(focusStartTime) // add start time to array list
            val formattedTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(Date(focusStartTime))

            Log.d("startTimer", "$timerMode start at $formattedTime")
            Log.d("startTimer", "$timerMode start at $focusStartTime")
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
        val lengthInMinutes = PrefUtil.getTimerLength(requireContext())
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }

    private fun setShortBreakTimerLength(){
        val lengthInMinutes = PrefUtil.getShortBreakTimerLength(requireContext())
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }
    private fun setLongBreakTimerLength(){
        val lengthInMinutes = PrefUtil.getLongBreakTimerLength(requireContext())
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireContext())
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


    //This is supposed to switch the timer if for example the focus time is finished and now
    //it's either going to be a short break or a long break

    private fun setTimerLength() {
        Log.d("SET TIMER LENGTH","timer mode is ${timerMode}")
        Log.d("SET TIMER LENGTH","get timer mode is ${PrefUtil.getTimerMode(requireContext())}")
        when(timerMode) {
            TimerMode.FOCUS -> setNewTimerLength()
            TimerMode.SHORT_BREAK -> setShortBreakTimerLength()
            TimerMode.LONG_BREAK -> setLongBreakTimerLength()
        }

    }

    private fun switchTimer() {
        Log.d("SWITCH TIMER","${timerMode}")
        Log.d("SWITCH TIMER","switch timer mode is ${PrefUtil.getTimerMode(requireContext())}")

        when (timerMode) {
            TimerMode.FOCUS -> {
                currentCycle++
                Log.d("SWITCH TIMER","current cycle is $currentCycle")
                val longBreakRepetition = PrefUtil.getLongBreakRepetition(requireContext())
                if (longBreakRepetition > 0 && currentCycle % longBreakRepetition == 0) {
                    timerMode = TimerMode.LONG_BREAK
                    PrefUtil.setTimerMode(timerMode, requireContext())
                }else{
                    timerMode = TimerMode.SHORT_BREAK
                    PrefUtil.setTimerMode(timerMode, requireContext())
                }

            }
           TimerMode.SHORT_BREAK -> {
                timerMode = TimerMode.FOCUS
                PrefUtil.setTimerMode(timerMode, requireContext())

            }
            TimerMode.LONG_BREAK -> {
                timerMode = TimerMode.FOCUS
                PrefUtil.setTimerMode(timerMode, requireContext())

            }
        }

    }


    private fun taskDone(){
        cycles = PrefUtil.getTimerCycles(requireContext())
        if (currentCycle == cycles){
            isTaskCompleted = true
            timerState = TimerState.COMPLETED

            //COMPUTE THE TIME COMPLETED
            var totalTimeCompleted = 0L
            for (i in 0 until focusCompletedTimes.size) {
                val cycleTimeCompleted = focusCompletedTimes[i] - focusStartTimes[i]
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

            val builder = AlertDialog.Builder(requireContext())
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

    var textStatus = ""
    private fun textStatusUpdate(){
        PrefUtil.getTimerMode(requireContext())

        textStatus = when (timerMode){
            TimerMode.FOCUS ->{
                "FOCUS TIME"
            }
           TimerMode.SHORT_BREAK ->{
                "SHORT BREAK"
            }
           TimerMode.LONG_BREAK ->{
                "LONG BREAK"
            }

        }
    }

    private fun bindItems() {
        textStatusUpdate()
//        val textStatus = getTimerMode(this).toString()
        binding.textTaskStatus.text = textStatus

        val taskName = PrefUtil.getTaskName(requireContext()).toString()
        binding.textTaskName.text = taskName

        val cycles = PrefUtil.getTimerCycles(requireContext()).toString()
        binding.textViewCycles.text = "$currentCycle / $cycles"

        val progressPercent = (currentCycle.toFloat()
                / PrefUtil.getTimerCycles(requireContext()).toFloat()) * 100
        binding.progressBarCycles.progress = progressPercent.toInt()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





