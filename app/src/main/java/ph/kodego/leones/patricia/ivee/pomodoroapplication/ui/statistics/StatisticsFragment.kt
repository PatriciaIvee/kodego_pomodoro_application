package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.statistics

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentHomeBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentStatisticsBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.home.HomeViewModel

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val statisticsViewModel =
            ViewModelProvider(this).get(StatisticsViewModel::class.java)

        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        return binding.root
    }


}