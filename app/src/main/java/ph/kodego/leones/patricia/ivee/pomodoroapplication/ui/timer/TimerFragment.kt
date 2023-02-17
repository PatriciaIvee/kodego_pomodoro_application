package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
            if (!isTimerRunning) {
                startTimer()
            }
        }

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

    //        binding.btnPlay.visibility = View.GONE
    private var isTimerRunning = false

    private fun startTimer() {
        if (isTimerRunning) {
            return // If timer is running, don't start another one
//             return@setOnClickListener
        }

        isTimerRunning = true

        var cycleCount = 0

        val timer = object : CountDownTimer(focusTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.progressBarTimer.progress = (100 * millisUntilFinished / focusTimeInMillis).toInt()
                binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.red_background_gradient) }
                binding.textTaskStatus.text = "Focus Time!"
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
//                binding.progressBarCycles.progress = focusTimeCycle/cycles
//                binding.textViewCycles.text = (focusTimeCycle/cycles).toString()
            }

            override fun onFinish() {

                cycleCount++
                focusTimeCycle++
                isTimerRunning = false


                if (focusTimeCycle == cycles) {
                    binding.progressBarCycles.progress = binding.progressBarCycles.max
                    binding.textViewCycles.text = "$focusTimeCycle / $cycles "
                    return // Stop the timer if the cycle count reaches the maximum
                }

                if (focusTimeCycle == longBreakPlayAfterCycle) {
                    // reset cycle count and play long break timer
                    val longBreakTimer = object : CountDownTimer(longBreakInMillis, 1000) {
                        // implement onTick and onFinish methods as above
                        override fun onTick(millisUntilFinished: Long) {
                            binding.progressBarTimer.progress = (100 * millisUntilFinished / longBreakInMillis).toInt()
                            binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.green_gradient_background) }
                            binding.textTaskStatus.text = "Long Break!"
                            val seconds = (millisUntilFinished / 1000) % 60
                            val minutes = (millisUntilFinished / (1000 * 60)) % 60
                            binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
                        }

                        override fun onFinish() {
//                            isTimerRunning = false
//                            updateTimerTextUI(DEFAULT_TIME)
//                            timer = null
//                            startTimer()
                        }
                    }
                    longBreakTimer.start()
                    cycleCount = 0
                } else{
                    // switch to short break timer
                    val breakTimer = object : CountDownTimer(shortBreakInMillis, 1000) {
                        // implement onTick and onFinish methods as above
                        override fun onTick(millisUntilFinished: Long) {
                            binding.progressBarTimer.progress = (100 * millisUntilFinished / shortBreakInMillis).toInt()
                            binding.backgroundLayout.background = context?.let { ContextCompat.getDrawable(it, R.drawable.blue_gradient_background) }
                            binding.textTaskStatus.text = "Short Break!"
                            val seconds = (millisUntilFinished / 1000) % 60
                            val minutes = (millisUntilFinished / (1000 * 60)) % 60
                            binding.textTimer.text = String.format("%02d:%02d", minutes, seconds)
                        }

                        override fun onFinish() {
                            isTimerRunning = false
                            startTimer()
                        }
                    }
                    breakTimer.start()
                }
                // update cycle count display
                binding.progressBarCycles.progress = (focusTimeCycle.toFloat() / cycles.toFloat() * 100).toInt()
                binding.textViewCycles.text = "$focusTimeCycle/$cycles"
            }

        }
        timer.start()
    }



    //TODO: make a variable that will get all the time the user completed (to be added to the database)
    //TODO: Add sound when the timer starts, pauses, runs and stops

    //TODO: Add Pause and stop Button and function for the timer


    var isTimerPaused = false
    var isTimerStopped = false
    private var pausedTimeFinished = 0
    private enum class TimerState {
        FOCUS,
        SHORT_BREAK,
        LONG_BREAK
    }


    private fun pauseTimer() {
        timer?.cancel()

//        remainingTime = timer?.remainingTime ?: 0L
        isTimerPaused = true
    }
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








