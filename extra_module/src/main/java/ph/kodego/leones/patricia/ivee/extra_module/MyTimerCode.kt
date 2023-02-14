package ph.kodego.leones.patricia.ivee.extra_module

import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R

class MyTimerCode {

    //TIMER MY CODE
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

    private lateinit var timer: CountDownTimer

    //Play the timer
    binding.btnPlay.setOnClickListener {
        setDefaultPatternTimer()
        if (isTimerPaused) {
            resumeTimer()
        } else {
            // other code to start a new timer
        }
        isTimerPaused = false
    }

    //Pause the timer
    binding.btnPause.setOnClickListener{
        pauseTimer()
    }

    //Stop and reset Timer
    binding.btnStop.setOnClickListener {
        stopTimer()
    }

    private var timeFinished = 0 // time finished over the time repetition you need

    private var focusTimeCount = 0 // var for focus time interval repetition
    private var cyclesBeforeLongBreak = 4 // When (cycle) Long break to be inserted
    private var nextBreakIsLong = false
    var remainingTime: Long = 0
    var isTimerPaused = false
    var isTimerStopped = false


    //TODO: make a variable that will get all the time the user completed (to be added to the database)
    //TODO: Change pattern to not constant pattern
    //TODO: Add sound when the timer starts, pauses, runs and stops
    private fun setDefaultPatternTimer() {
        //current pattern = 4 focus time repeats before the long break comes

//        val timer: CountDownTimer
        when (timeFinished) {
            0, 2, 4, 6 -> {
                var timer = object : CountDownTimer(focusTime!!.toLong() + 500, 1000) {
                    override fun onTick(remainingTime: Long) {
                        binding.backgroundLayout.background =
                            context?.let {
                                ContextCompat.getDrawable(
                                    it,
                                    R.drawable.red_background_gradient
                                )
                            }
                        binding.progressBarTimer.progress =
                            (100 * (remainingTime / 1000) / (focusTime!!.toLong() / 1000)).toInt()
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
        remainingTime = focusTime!!.toLong() - System.currentTimeMillis()
        timer.cancel()
        isTimerPaused = true
    }
    private fun stopTimer() {
        timer.cancel()
        isTimerStopped = true
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
}