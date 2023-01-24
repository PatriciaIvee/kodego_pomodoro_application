package ph.kodego.leones.patricia.ivee.pomodoroapplication.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ph.kodego.leones.patricia.ivee.pomodoroapplication.adapter.HorizontalRecyclerAdapter
import ph.kodego.leones.patricia.ivee.pomodoroapplication.adapter.TaskAdapter
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentHomeBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.model.SwipeCallBack
import ph.kodego.leones.patricia.ivee.pomodoroapplication.model.Task

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var horizontalRecyclerAdapter: HorizontalRecyclerAdapter
    private lateinit var taskAdapter: TaskAdapter
    private var tasks: ArrayList<Task> = ArrayList()
    private lateinit var itemTouchHelper: ItemTouchHelper

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
        init()

        horizontalRecyclerAdapter = HorizontalRecyclerAdapter(demoData)
        taskAdapter = TaskAdapter(tasks)

        Log.d("HorizontalRecyclerAdapter", binding.horizontalRecyclerview.adapter.toString())

        with(binding.horizontalRecyclerview){
            adapter = horizontalRecyclerAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }
        with(binding.tasksMini){
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        }

        var swipeCallBack = SwipeCallBack(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        swipeCallBack.horizontalRecyclerViewAdapter = horizontalRecyclerAdapter
        itemTouchHelper = ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(binding.horizontalRecyclerview)

        return root
    }

    fun init() {
        tasks.add(Task("Task One","Completed"))
        tasks.add(Task("Task Two","Ongoing"))
        tasks.add(Task("Task Three","Ongoing"))
    }

}