package ph.kodego.leones.patricia.ivee.pomodoroapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentHomeBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentPomodoroTimerBinding

class PomodoroTimerFragment : Fragment() {
    private var _binding: FragmentPomodoroTimerBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pomodoro_timer, container, false)
//        _binding = FragmentPomodoroTimerBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        return root
    }


}