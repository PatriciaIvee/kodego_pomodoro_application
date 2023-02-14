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

    private var focusTime: Int? = null
    private var shortBreak: Int? = null // breakTimer
    private var longBreak: Int? = null // breakTimer
    private var repetition: Int? = null //roundCount


    private lateinit var timer: CountDownTimer

    private final var LOGINFO = "TIMER_FRAGMENT"

    private enum class TimerState {
        FOCUS,
        SHORT_BREAK,
        LONG_BREAK
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var data = this.arguments
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

//            binding.textViewRepetition.text = "$timeFinished / $repetition "
        }

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
                    timer = object : CountDownTimer(focusTime!!.toLong() + 500, 1000) {
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
        timer?.cancel()
//        timer = null
//        remainingTime = ((binding.progressBarTimer.progress / 100f) *
//                (getCurrentTime()?.toLong() ?: 0)).toLong()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}













