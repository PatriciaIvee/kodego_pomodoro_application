package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private final var LOGINFO = "TASK_FRAGMENT"
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val taskViewModel =
//            ViewModelProvider(this).get(TaskViewModel::class.java)

        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        binding.repetitionNumberPicker.apply {
            minValue = 1
            maxValue = 50
//
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveAndStart.setOnClickListener{
            val taskName = binding.taskNameInput.text.toString()
            val focusTime = binding.focusTimeInput.text.toString()
            val shortBreak = binding.shortBreakInput.text.toString()
            val longBreak = binding.longBreakInput.text.toString()
            val repetition = binding.repetitionNumberPicker.value.toString()

            if (focusTime.isNotEmpty() && shortBreak.isNotEmpty() && longBreak.isNotEmpty() && repetition.isNotEmpty()){

                //                val intent = Intent(this,TimerFragment ::class.java)
                var bundle = Bundle()
                bundle.putString("taskName", taskName)
                bundle.putInt("focusTime",focusTime.toInt())
                bundle.putInt("shortBreak",shortBreak.toInt())
                bundle.putInt("longBreak",longBreak.toInt())
                bundle.putInt("repetition",repetition.toInt())

//                val fragmentTimer = TimerFragment()
//                fragmentTimer.arguments = bundle
                Log.d(LOGINFO,"taskName value TO PASS is: $taskName")
                Log.d(LOGINFO,"focusTime value TO PASS is: $focusTime")
                Log.d(LOGINFO,"shortBreak value TO PASS is: $shortBreak")
                Log.d(LOGINFO,"longBreak value TO PASS is: $longBreak")
                Log.d(LOGINFO,"repetition value TO PASS is: $repetition")

                findNavController().navigate(R.id.action_taskfragment_to_timerfragment,bundle)

            }else{
                Toast.makeText(context,"Fill fields above", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}