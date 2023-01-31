package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private final var LOGINFO = "TIMER_FRAGMENT"

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
            Log.d(LOGINFO,"FocusTime value is: ${ args.getString("focusTime").toString() }")
            Log.d(LOGINFO,"ShortBreak value is: ${  args.getString("shortBreak").toString() }")
            Log.d(LOGINFO,"LongBreak value is: ${  args.getString("longBreak").toString() }")
            Log.d(LOGINFO,"LongBreak value is: ${  args.getString("interval").toString() }")

            val taskName = args.getString("taskName").toString()
            binding.textTaskName.text = taskName
            Log.d(LOGINFO,"taskName value is: $taskName")
            val focusTime  = args.getString("focusTime").toString()
            binding.textTimer.text =  focusTime
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//        val args = this.arguments
//        val taskName = args?.getString("taskName").toString()
//        binding.textTaskName.text = taskName
//        val focusTime = args?.getInt("focusTime").toString()
//        binding.textTimer.text =  focusTime



//
//
//        timerViewModel.taskName.observe(viewLifecycleOwner) {
//            taskName ->
//            val textView: TextView = binding.textTaskName
//            textView.text = taskName
//
//        }

//            textView.text = it

//        create a bundle to pass data from one fragment to another