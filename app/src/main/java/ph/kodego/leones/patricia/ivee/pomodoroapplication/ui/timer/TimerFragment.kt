package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private final var LOGINFO = "TIMER_FRAGMENT"

//    private var studyMinute: Int? = null
//    private var breakMinute: Int? = null
//    private var roundCount: Int? = null
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val timerViewModel =
//            ViewModelProvider(this).get(TimerViewModel::class.java)

        _binding = FragmentTimerBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var data = this.arguments
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
//            val focusTime  = args.getString("focusTime").toString()
//            binding.textTimer.text =  focusTime



//            Get the Info Needed for Timer
            focusTime = args.getInt("focusTime", 0).toInt() * 60 * 1000
            shortBreak = args.getInt("shortBreak", 0).toInt()* 60 * 1000
            longBreak = args.getInt("longBreak", 0).toInt()* 60 * 1000
            repetition = args.getInt("repetition", 0) //Round

//            studyMinute = intent.getIntExtra("study", 0) * 60 * 1000
//            breakMinute = intent.getIntExtra("break", 0) * 60 * 1000
//            roundCount = intent.getIntExtra("round", 0)
            binding.textViewRepetition.text = "$timeFinished / $repetition "
        }



//        binding.btnPause.setOnClickListener{
//            focusTimer!!.cancel()
//            timeLeft = binding.textTimer.text.toString().toLong() * 1000
//        }

        binding.btnPlay.setOnClickListener {
            setDefaultPatternTimer()
        }

    }

    private var timeFinished = 0 // time finished over the time repetition you need

    private fun setDefaultPatternTimer() {
        val timer: CountDownTimer
        when (timeFinished) {
            0, 2, 4, 6 -> {
                timer = object : CountDownTimer(focusTime!!.toLong() + 500, 1000) {
                    override fun onTick(remainingTime: Long) {
                        binding.progressBarTimer.progress = (remainingTime / 1000).toInt()
                        binding.textTaskStatus.text = "Focus Time Break"
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
                        binding.progressBarTimer.progress = (remainingTime / 1000).toInt()
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
                        binding.progressBarTimer.progress = (remainingTime / 1000).toInt()
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


