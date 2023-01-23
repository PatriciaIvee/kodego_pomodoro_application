package ph.kodego.leones.patricia.ivee.pomodoroapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentHomeBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentSetTaskBinding


class SetTaskFragment : Fragment() {

    private var _binding: FragmentSetTaskBinding? = null

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_task, container, false)
//        _binding = FragmentSetTaskBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}