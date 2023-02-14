package ph.kodego.leones.patricia.ivee.extra_module

import android.os.CountDownTimer
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PreferenceUtility

class TimerFragExtra {


    //onViewCreated the buttons
    //Play the timer
    binding.btnPlay.setOnClickListener {

//
//            startTimer()
//            timerState = TimerState.RUNNING
//            updateButtons()
    }

    //Pause the timer
    binding.btnPause.setOnClickListener{
//            timer.cancel()
//            timerState = TimerState.PAUSED
//            updateButtons()
    }
    // Gets an error when you apply it

    //Stop and reset Timer
    binding.btnStop.setOnClickListener {
        stopTimer()
//            timer.cancel()
//            timerState = TimerState.STOPPED
//            onTimerFinished()

    }
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
    // Timer with Preference Utility Notification Utility(Background Timer) Not Working
    //TODO: Check WHY it doesn't work
    private fun startTimer(){
        timerState = TimerState.RUNNING



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
}