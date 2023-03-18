package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

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

class TimerFragment : Fragment() {


    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private var focusTime: Long = 0L
    private var shortBreak: Long = 0L // breakTimer
    private var longBreak: Long = 0L // breakTimer
    private var focusTimeCycle:Int = 0
    private var cycles: Int = 0 //roundCount
    private var longBreakPlayAfterCycle: Int = 0
    private var focusTimeInMillis :Long= 0L
    private var shortBreakInMillis: Long = 0L
    private var longBreakInMillis : Long = 0L
    private var remainingTimeInMillis: Long = 0L
    private var currentTimerState: TimerState = TimerState.FOCUS
    private var currentTimer: CountDownTimer? = null
    private var isTimerRunning = false
//    private var taskPriority: java.io.Serializable = ""

    private lateinit var timer: CountDownTimer

    private final var LOGINFO = "TIMER_FRAGMENT"





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        return binding.root
    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var data = this.arguments
        //Arguments from the taskFragment
        arguments?.let { args ->

            Log.d(LOGINFO,"taskName value is: ${ args.getString("taskName") }")
            Log.d(LOGINFO,"FocusTime value is: ${ args.getInt("focusTime").toString() }")
            Log.d(LOGINFO,"ShortBreak value is: ${  args.getInt("shortBreak").toString() }")
            Log.d(LOGINFO,"LongBreak value is: ${  args.getInt("longBreak").toString() }")
            Log.d(LOGINFO,"Cycles value is: ${  args.getInt("cycles").toString() }")
            Log.d(LOGINFO,"longBreakPlayAfterCycle value is: ${  args.getInt("longBreakPlayAfterCycle").toString() } ")
            Log.d(LOGINFO,"taskPriority value is: ${args.get("taskPriority")}")

            //Get task Name from the task fragment
            val taskName = args.getString("taskName").toString()
            binding.textTaskName.text = taskName
            Log.d(LOGINFO,"taskName value is: $taskName")



            // Get the Info Needed for Timer
            focusTime = args.getInt("focusTime", 0).toLong()
            shortBreak = args.getInt("shortBreak", 0).toLong()
            longBreak = args.getInt("longBreak", 0).toLong()
            cycles = args.getInt("cycles", 0).toInt()
            longBreakPlayAfterCycle = args.getInt("longBreakPlayAfterCycle",0)


            focusTimeInMillis = args.getInt("focusTime").toLong() * 60 * 1000 // convert minutes to milliseconds
            shortBreakInMillis = args.getInt("shortBreak").toLong() * 60 * 1000 // convert minutes to milliseconds
            longBreakInMillis = args.getInt("longBreak").toLong() * 60 * 1000 // convert minutes to milliseconds
            cycles = args.getInt("cycles")
            longBreakPlayAfterCycle = args.getInt("longBreakPlayAfterCycle",0)

            binding.textViewCycles.text = "$focusTimeCycle / $cycles "
            binding.progressBarCycles.progress = (focusTimeCycle.toFloat() / cycles.toFloat() * 100).toInt()
        }


        //Play the timer
        binding.btnPlay.setOnClickListener {
            startCountdownTimer()
        }

//    if (!isTimerRunning) {
//        // Timer is not running, so start it from scratch
//        startTimer(TimerState.FOCUS, focusTimeInMillis, cycles)
//    } else {
//        // Timer is paused, so resume it with the remaining time
//        resumeTimer()
//    }



        //Pause the timer
        binding.btnPause.setOnClickListener{
            pauseTimer()
        }

        //Stop and reset Timer
        binding.btnStop.setOnClickListener {
            timer.cancel()
//            stopTimer()
        }
    }
    enum class TimerState {
        FOCUS,
        SHORT_BREAK,
        LONG_BREAK
    }





    //TODO: make a variable that will get all the time the user completed (to be added to the database)
    //TODO: Add sound when the timer starts, pauses, runs and stops

    //TODO: Add Pause and stop Button and function for the timer

    private fun startTimer2(timerState: TimerState, remainingTimeInMillis: Long? = 0L, cycleCount: Int = 0) {
        if (currentTimerState == timerState && currentTimer != null) {
            return // If timer is already running for the same timer state, don't start another one
        }

        // Cancel the current timer if it is running
        currentTimer?.cancel()

        currentTimerState = timerState

        val durationInMillis = when (timerState) {
            TimerState.FOCUS -> focusTimeInMillis
            TimerState.SHORT_BREAK -> shortBreakInMillis
            TimerState.LONG_BREAK -> longBreakInMillis
        }

        currentTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                when (currentTimerState) {
                    TimerState.FOCUS -> {
                        binding.textTaskStatus.text = "Focus Time!"
                        binding.progressBarTimer.progress = (100 * millisUntilFinished / focusTimeInMillis).toInt()
                        binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.red_background_gradient) }
                        val seconds = (millisUntilFinished / 1000) % 60
                        val minutes = (millisUntilFinished / (1000 * 60)) % 60
                        binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
                    }
                    TimerState.SHORT_BREAK -> {
                        binding.textTaskStatus.text = "Short Break!"
                        binding.progressBarTimer.progress = (100 * millisUntilFinished / shortBreakInMillis).toInt()
                        binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.blue_gradient_background) }
                        val seconds = (millisUntilFinished / 1000) % 60
                        val minutes = (millisUntilFinished / (1000 * 60)) % 60
                        binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
                    }
                    TimerState.LONG_BREAK -> {
                        binding.textTaskStatus.text = "Long Break!"
                        binding.progressBarTimer.progress = (100 * millisUntilFinished / longBreakInMillis).toInt()
                        binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.green_gradient_background) }

                        val seconds = (millisUntilFinished / 1000) % 60
                        val minutes = (millisUntilFinished / (1000 * 60)) % 60
                        binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
                    }
                }
            }

            override fun onFinish() {
                // Switch to next timer state or stop the timer if the cycle count reaches the maximum
                when (currentTimerState) {
                    TimerState.FOCUS -> {
                        focusTimeCycle++
                        if (focusTimeCycle == longBreakPlayAfterCycle) {
                            // If 3 focus times have been completed, reset focusTimeCycle and switch to long break timer
                            focusTimeCycle = 0
                            currentTimerState = TimerState.LONG_BREAK
                        } else {
                            // Otherwise switch to short break timer
                            currentTimerState = TimerState.SHORT_BREAK
                        }
                    }
                    TimerState.SHORT_BREAK, TimerState.LONG_BREAK -> {
                        currentTimerState = TimerState.FOCUS
                    }
                }

//                // Update remaining time if available
//                remainingTimeInMillis = when (currentTimerState) {
//                    TimerState.FOCUS -> focusTimeInMillis
//                    TimerState.SHORT_BREAK -> shortBreakInMillis
//                    TimerState.LONG_BREAK -> longBreakInMillis
//                }

//                // Update UI
//                updateUI(currentTimerState, remainingTimeInMillis)

                // Start the next timer with the remaining time if available
                if (remainingTimeInMillis != null && remainingTimeInMillis > 0) {
                    startTimer2(currentTimerState, remainingTimeInMillis, cycleCount)
                } else {
                    if (currentTimerState == TimerState.FOCUS && cycleCount > 1) {
                        // If there are more cycles to complete, start the next cycle with focus timer
                        startTimer2(TimerState.FOCUS, null, cycleCount - 1)
                    } else {
                        // Otherwise stop the timer
                        stopTimer()
                    }
                }
            }
        }

        currentTimer?.start()
    }



    private fun startFocusTimer(remainingTimeInMillis: Long? = null) {
        val timer = object : CountDownTimer(remainingTimeInMillis ?: focusTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBarTimer.progress = (100 * millisUntilFinished / focusTimeInMillis).toInt()
                binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.red_background_gradient) }
                binding.textTaskStatus.text = "Focus Time!"
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
                binding.progressBarCycles.progress = focusTimeCycle/cycles
                binding.textViewCycles.text = (focusTimeCycle/cycles).toString()
                }
                override fun onFinish() {
                // Switch to short break or long break timer
                    focusTimeCycle++ / cycles
                    binding.textTaskStatus.text = "Focus Time Finished!"

                }

            }
        currentTimer = timer
        timer.start()
    }

    private fun startShortBreakTimer(remainingTimeInMillis: Long? = null) {
        val timer = object : CountDownTimer(remainingTimeInMillis ?: shortBreakInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBarTimer.progress = (100 * millisUntilFinished / shortBreakInMillis).toInt()
                binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.blue_gradient_background) }
                binding.textTaskStatus.text = "Short Break!"
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.textTaskStatus.text = "Short Break Finished!"
            }
        }
        currentTimer = timer
        timer.start()
    }
    private fun startLongBreakTimer(remainingTimeInMillis: Long? = null) {
        val timer = object : CountDownTimer(remainingTimeInMillis ?: longBreakInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBarTimer.progress = (100 * millisUntilFinished / longBreakInMillis).toInt()
                binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.green_gradient_background) }
                binding.textTaskStatus.text = "Long Break!"
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // Switch to short break or long break timer
                binding.textTaskStatus.text = "Long Break Finished!"
            }
        }
        currentTimer = timer
        timer.start()
    }

    private fun pauseTimer() {
        currentTimer?.cancel()
        currentTimer = null
        // Save remaining time
    }

    private fun resumeTimer() {
        when (currentTimerState) {
            TimerState.FOCUS -> startFocusTimer(remainingTimeInMillis)
            TimerState.SHORT_BREAK -> startShortBreakTimer(remainingTimeInMillis)
            TimerState.LONG_BREAK -> startLongBreakTimer(remainingTimeInMillis)
        }
    }

    private fun startCountdownTimer() {
        var cycleCount = 0
        while (cycleCount != cycles){
            startFocusTimer()
            cycleCount++
            if (cycleCount == longBreakPlayAfterCycle){
                startLongBreakTimer()
            } else {
                startShortBreakTimer()
            }
        }
    }

//    private fun updateUI() {
//        when (currentTimerState) {
//            TimerState.FOCUS -> startFocusTimer(remainingTimeInMillis){
//            TimerState.SHORT_BREAK -> startShortBreakTimer(remainingTimeInMillis)
//            TimerState.LONG_BREAK -> startLongBreakTimer(remainingTimeInMillis)
//        }
//    }

    private fun stopTimer() {
        timer.cancel()
        remainingTime = 0L
        isTimerStopped = true
    }
    var remainingTime: Long? = null


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

var isTimerPaused = false
var isTimerStopped = false
private var pausedTimeFinished = 0



//    private fun pauseTimer() {
//        isTimerRunning = false
//
//        timer?.cancel()
//
//        // save remaining time
//        remainingTimeInMillis = binding.textTimer.text.toString().toLong() * 1000
//    }

//        binding.btnPlay.visibility = View.GONE
//private var isTimerRunning = false

//    private fun startTimer() {
//        if (isTimerRunning) {
//            return // If timer is running, don't start another one
////             return@setOnClickListener
//        }
//
//        isTimerRunning = true
//
//        var cycleCount = 0
//
//        val timer = object : CountDownTimer(focusTimeInMillis, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                binding.progressBarTimer.progress = (100 * millisUntilFinished / focusTimeInMillis).toInt()
//                binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.red_background_gradient) }
//                binding.textTaskStatus.text = "Focus Time!"
//                val seconds = (millisUntilFinished / 1000) % 60
//                val minutes = (millisUntilFinished / (1000 * 60)) % 60
//                binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
////                binding.progressBarCycles.progress = focusTimeCycle/cycles
////                binding.textViewCycles.text = (focusTimeCycle/cycles).toString()
//            }
//
//            override fun onFinish() {
//
//                cycleCount++
//                focusTimeCycle++
//                isTimerRunning = false
//
//
//                if (focusTimeCycle == cycles) {
//                    binding.progressBarCycles.progress = binding.progressBarCycles.max
//                    binding.textViewCycles.text = "$focusTimeCycle / $cycles "
//                    return // Stop the timer if the cycle count reaches the maximum
//                }
//
//                if (focusTimeCycle == longBreakPlayAfterCycle) {
//                    // reset cycle count and play long break timer
//                    val longBreakTimer = object : CountDownTimer(longBreakInMillis, 1000) {
//                        // implement onTick and onFinish methods as above
//                        override fun onTick(millisUntilFinished: Long) {
//                            binding.progressBarTimer.progress = (100 * millisUntilFinished / longBreakInMillis).toInt()
//                            binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.green_gradient_background) }
//                            binding.textTaskStatus.text = "Long Break!"
//                            val seconds = (millisUntilFinished / 1000) % 60
//                            val minutes = (millisUntilFinished / (1000 * 60)) % 60
//                            binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
//                        }
//
//                        override fun onFinish() {
////                            isTimerRunning = false
////                            updateTimerTextUI(DEFAULT_TIME)
////                            timer = null
////                            startTimer()
//                        }
//                    }
//                    longBreakTimer.start()
//                    cycleCount = 0
//                } else{
//                    // switch to short break timer
//                    val breakTimer = object : CountDownTimer(shortBreakInMillis, 1000) {
//                        // implement onTick and onFinish methods as above
//                        override fun onTick(millisUntilFinished: Long) {
//                            binding.progressBarTimer.progress = (100 * millisUntilFinished / shortBreakInMillis).toInt()
//                            binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.blue_gradient_background) }
//                            binding.textTaskStatus.text = "Short Break!"
//                            val seconds = (millisUntilFinished / 1000) % 60
//                            val minutes = (millisUntilFinished / (1000 * 60)) % 60
//                            binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
//                        }
//
//                        override fun onFinish() {
//                            isTimerRunning = false
//                            startTimer()
//                        }
//                    }
//                    breakTimer.start()
//                }
//                // update cycle count display
//                binding.progressBarCycles.progress = (focusTimeCycle.toFloat() / cycles.toFloat() * 100).toInt()
//                binding.textViewCycles.text = "$focusTimeCycle/$cycles"
//            }
//
//        }
//        timer.start()
//    }


// if (!isTimerRunning) {
//                startTimer()
//            }



