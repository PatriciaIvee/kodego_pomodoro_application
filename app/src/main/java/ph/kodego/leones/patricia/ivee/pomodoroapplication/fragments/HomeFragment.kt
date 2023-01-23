package ph.kodego.leones.patricia.ivee.pomodoroapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.leones.patricia.ivee.pomodoroapplication.adapter.HorizontalRecyclerAdapter
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var horizontalRecyclerAdapter: HorizontalRecyclerAdapter

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val demoData = arrayListOf(
            "Curabitur sit amet rutrum enim,",
            "Lorem ipsum dolor sit amet, ",
            "Praesent efficitur ",
        )

        horizontalRecyclerAdapter = HorizontalRecyclerAdapter(demoData)

        Log.d("CarouselAdapter", binding.horizontalRecyclerview.adapter.toString())

        with(binding.horizontalRecyclerview){
            adapter = horizontalRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }

        return root
    }

}