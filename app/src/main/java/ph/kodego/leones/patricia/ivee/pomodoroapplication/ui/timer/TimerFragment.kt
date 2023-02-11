package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentTimerBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.NotificationUtil
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PreferenceUtility
import java.util.Calendar
import java.util.Timer

class TimerFragment : Fragment() {

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long) : Long {
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver ::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime,pendingIntent)
            PreferenceUtility.setAlarmSetTime(nowSeconds,context)

            return wakeUpTime
        }

        fun removeAlarm(context:Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0 , intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PreferenceUtility.setAlarmSetTime(0,context)
        }


        val nowSeconds:Long
        get() = Calendar.getInstance().timeInMillis/1000
    }

    private var _binding: FragmentTimerBinding? = null


    private lateinit var originalBitmap: Bitmap
    private lateinit var gradientTransform: GradientTransform
    private lateinit var gradientBitmap: Bitmap

    private val binding get() = _binding!!

    private final var LOGINFO = "TIMER_FRAGMENT"


    private var focusTime: Int? = null
    private var shortBreak: Int? = null // breakTimer
    private var longBreak: Int? = null // breakTimer
    private var repetition: Int? = null //roundCount
//    private var timeLeft: Long = focusTime!!.toLong() + 500

    private var focusTimer: CountDownTimer? = null
    private var shortBreakTimer: CountDownTimer? = null
    private var longBreakTimer : CountDownTimer? = null


    private var restTimer: CountDownTimer? = null
    private var studyTimer: CountDownTimer? = null
    private var breakTimer: CountDownTimer? = null



    private var isStudy = true

    private var isStop = false

    private var mp: MediaPlayer? = null

    private var red_background = R.drawable.red_background_gradient

    enum class TimerMode{
        FOCUS_TIME,
        SHORT_BREAK,
        LONG_BREAK
    }

    enum class TimerState{
        STOPPED,
        PAUSED,
        RUNNING
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.STOPPED
    private var timerMode = TimerMode.FOCUS_TIME
    private var secondsRemaining: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTimerBinding.inflate(inflater, container, false)

//        binding.progressBarTimer.setMax(60)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var data = this.arguments
        //Arguments from the taskFragment
        arguments?.let { args ->

            Log.d(LOGINFO,"taskName value is: ${ args.getString("taskName") }")
            Log.d(LOGINFO,"FocusTime value is: ${ args.getInt("focusTime").toString() }")
            Log.d(LOGINFO,"ShortBreak value is: ${  args.getInt("shortBreak").toString() }")
            Log.d(LOGINFO,"LongBreak value is: ${  args.getInt("longBreak").toString() }")
            Log.d(LOGINFO,"Repetition value is: ${  args.getInt("repetition").toString() }")


            //Get task Name from the task fragment
            val taskName = args.getString("taskName").toString()
            binding.textTaskName.text = taskName
            Log.d(LOGINFO,"taskName value is: $taskName")



            // Get the Info Needed for Timer
            focusTime = args.getInt("focusTime", 0).toInt() * 60 * 1000
            shortBreak = args.getInt("shortBreak", 0).toInt()* 60 * 1000
            longBreak = args.getInt("longBreak", 0).toInt()* 60 * 1000
            repetition = args.getInt("repetition", 0) //Round
//
//            binding.textViewRepetition.text = "$timeFinished / $repetition "
        }

        //Play the timer
        binding.btnPlay.setOnClickListener {
            setDefaultPatternTimer()
//            resumeTimer()
//            startTimer()
//            timerState = TimerState.RUNNING
//            updateButtons()
        }

        //Pause the timer
        binding.btnPause.setOnClickListener{
            pauseTimer()
//            focusTimer!!.cancel()
//            timeLeft = binding.textTimer.text.toString().toLong() * 1000
//            timer.cancel()
//            timerState = TimerState.PAUSED
//            updateButtons()
        }
        // Gets an error when you apply it

        //Stop and reset Timer
        binding.btnStop.setOnClickListener {
//            timer.cancel()
//            timerState = TimerState.STOPPED
//            onTimerFinished()

        }

    }

    private var timeFinished = 0 // time finished over the time repetition you need

    private var focusTimeCount = 0 // var for focus time interval repetition
    private var cyclesBeforeLongBreak = 4 // When (cycle) Long break to be inserted
    private var nextBreakIsLong = false
    var remainingTime: Long = 0
    var isTimerPaused = false


    //TODO: make a variable that will get all the time the user completed (to be added to the database)
    //TODO: Change pattern to not constant pattern
    //TODO: Add sound when the timer starts, pauses, runs and stops
    private fun setDefaultPatternTimer() {
        //current pattern = 4 focus time repeats before the long break comes

//        val timer: CountDownTimer
        when (timeFinished) {
            0, 2, 4, 6 -> {
                timer = object : CountDownTimer(focusTime!!.toLong() + 500, 1000) {
                    override fun onTick(remainingTime: Long) {
                        binding.backgroundLayout.background =
                            context?.let { ContextCompat.getDrawable(it, R.drawable.red_background_gradient) }
                        binding.progressBarTimer.progress = (100 * (remainingTime / 1000) / (focusTime!!.toLong() / 1000)).toInt()
                        binding.textTaskStatus.text = "Focus Time!"
                        binding.textTimer.text = createTimeFormat((remainingTime / 1000).toInt())
                    }

                    override fun onFinish() {
                        timeFinished++
                        setDefaultPatternTimer()
                    }
                }
            }
            1, 3, 5 -> {
                timer = object : CountDownTimer(shortBreak!!.toLong() + 500, 1000) {
                    override fun onTick(remainingTime: Long) {
                        binding.backgroundLayout.background =
                            context?.let { ContextCompat.getDrawable(it, R.drawable.blue_gradient_background) }
                        binding.progressBarTimer.progress = (100 * (remainingTime / 1000) / (shortBreak!!.toLong() / 1000)).toInt()
                        binding.textTaskStatus.text = "Short Break"
                        binding.textTimer.text = createTimeFormat((remainingTime / 1000).toInt())

                    }

                    override fun onFinish() {
                        timeFinished++
                        setDefaultPatternTimer()
                    }
                }
            }
            else -> {
                timer = object : CountDownTimer(longBreak!!.toLong() + 500, 1000) {
                    override fun onTick(remainingTime: Long) {
                        binding.backgroundLayout.background =
                            context?.let { ContextCompat.getDrawable(it, R.drawable.green_gradient_background) }
                        binding.progressBarTimer.progress = (100 * (remainingTime / 1000) / (longBreak!!.toLong() / 1000)).toInt()
                        binding.textTaskStatus.text = "Long Break"
                        binding.textTimer.text = createTimeFormat((remainingTime / 1000).toInt())
                    }

                    override fun onFinish() {
                        timeFinished = 0
                        setDefaultPatternTimer()
                    }
                }
            }
        }
        timer.start()
    }

    private fun pauseTimer() {
        timer.cancel()
        isTimerPaused = true
    }
    private fun resumeTimer() {
        timer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(remainingTime: Long) {
                this@TimerFragment.remainingTime = remainingTime
                binding.progressBarTimer.progress = (100 * (remainingTime / 1000) / (focusTime!!.toLong() / 1000)).toInt()
                binding.textTimer.text = createTimeFormat((remainingTime / 1000).toInt())
            }

            override fun onFinish() {
                timeFinished++
                setDefaultPatternTimer()
            }
        }
        timer?.start()
    }

    private fun createTimeFormat(time : Int): String {
        var timeFormat = ""
        val minutes = time / 60
        val seconds = time % 60

        if (minutes < 10) timeFormat += "0"
        timeFormat += "$minutes:"

        if (seconds < 10) timeFormat += "0"
        timeFormat += seconds

        return timeFormat
    }




    // Timer with Preference Utility Notification Utility(Background Timer) Not Working
    //TODO: Check WHY it doesn't work
    private fun startTimer(){
        timerState = TimerState.RUNNING

        if (focusTimeCount % cyclesBeforeLongBreak == 0) {
            nextBreakIsLong = true
        }

        timer = object : CountDownTimer(secondsRemaining!!.toLong() * 1000, 1000) {
            override fun onFinish() {
                onTimerFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

//    private fun initTimer(){
//        timerState = PreferenceUtility.getTimerState(requireContext())
//        timerMode = PreferenceUtility.getTimerMode(requireContext())
//
//        //we don't want to change the length of the timer which is already running
//        //if the length was changed in settings while it was backgrounded
//        if (timerState == TimerState.STOPPED)
//            setNewTimerLength()
//        else
//            setPreviousTimerLength()
//
//        // Add new variables to track the different timer lengths
//        val focusTimeLength = PreferenceUtility.getFocusTimeLength(requireContext()) * 60L
//        val shortBreakLength = PreferenceUtility.getShortBreakTimeLength(requireContext()) * 60L
//        val longBreakLength = PreferenceUtility.getLongBreakTimeLength(requireContext()) * 60L
//
//        when (timerMode) {
//            TimerMode.FOCUS_TIME -> {
//                timerLengthSeconds = focusTimeLength
//                secondsRemaining = focusTimeLength
//            }
//            TimerMode.SHORT_BREAK -> {
//                timerLengthSeconds = shortBreakLength
//                secondsRemaining = shortBreakLength
//            }
//            TimerMode.LONG_BREAK -> {
//                timerLengthSeconds = longBreakLength
//                secondsRemaining = longBreakLength
//            }else -> {
//            secondsRemaining = if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED)
//                PreferenceUtility.getSecondsRemaining(requireContext())
//            else
//                timerLengthSeconds
//        }
//        }

//        secondsRemaining = if (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED)
//            PreferenceUtility.getSecondsRemaining(requireContext())
//        else
//            timerLengthSeconds

//        val alarmSetTime = PreferenceUtility.getAlarmSetTime(requireContext())
//        if (alarmSetTime > 0) {
//            secondsRemaining -=  nowSeconds - alarmSetTime
//        }
//
//        if (secondsRemaining <= 0){
//            onTimerFinished()
//        } else if (timerState == TimerState.RUNNING){
//            startTimer()
//        }
//
//
//        updateButtons()
//        updateCountdownUI()
//    }

    private fun onTimerFinished(){
        timerState = TimerState.STOPPED
        timerMode = TimerMode.FOCUS_TIME

        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        setNewTimerLength()

        binding.progressBarTimer.progress = 0

        PreferenceUtility.setSecondsRemaining(timerLengthSeconds, requireContext())
        secondsRemaining = timerLengthSeconds

//        when (timerMode) {
//            TimerMode.FOCUS_TIME-> PreferenceUtility.setSecondsRemaining(focusTimeLength, requireContext())
//            TimerMode.SHORT_BREAK -> PreferenceUtility.setSecondsRemaining(shortBreakLength, requireContext())
//            TimerMode.LONG_BREAK-> PreferenceUtility.setSecondsRemaining(longBreakLength, requireContext())
//            else -> PreferenceUtility.setSecondsRemaining(timerLengthSeconds, requireContext())
//        }

        updateButtons()
        updateCountdownUI()
    }
    private fun setNewTimerLength(){
        val lengthInMinutes = PreferenceUtility.getTimerLength(requireContext())
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.progressBarTimer.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PreferenceUtility.getPreviousTimerLengthSeconds(requireContext())
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
        }
    }





    override fun onResume() {
        super.onResume()

//        initTimer()
//        removeAlarm(requireContext())
//        NotificationUtil.hideTimerNotification(requireContext())
    }

    override fun onPause() {
        super.onPause()
//        if (timerState ==TimerState.RUNNING){
//            timer.cancel()
//            val wakeUpTime = setAlarm(requireContext(), nowSeconds,secondsRemaining)
//            NotificationUtil.showTimerRunning(requireContext(),wakeUpTime)
//
//        }else if (timerState == TimerState.PAUSED){
//            NotificationUtil.showTimerPaused(requireContext())
//        }
//
//        PreferenceUtility.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext())
//        PreferenceUtility.setSecondsRemaining(secondsRemaining, requireContext())
//        PreferenceUtility.setTimerState(timerState, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


