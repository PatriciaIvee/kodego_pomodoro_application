package ph.kodego.leones.patricia.ivee.pomodoroapplication.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.TaskItemBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.model.Task
import java.util.concurrent.TimeUnit

class TaskAdapter(var tasks: ArrayList<Task>)
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    fun addTask(task:Task){
        tasks.add(0,task)
        notifyItemInserted(0)
    }

    fun removeTask(position: Int){
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editTask(newTasks:ArrayList<Task>){
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()

    }

    fun editTaskStatus(task: Task){
        task.status = task.status
    }

    fun completedTask(task: Task){
        task.status = "Completed"

        var completedTasks: ArrayList<Task> = ArrayList()
        completedTasks.add(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTaskTimer(){

//        var focusTime = TimeUnit.MINUTES.toMillis()

    }

    override fun onCreateViewHolder(
//        Sets layout per line (row)
        parent: ViewGroup,
        viewtype: Int
    ):TaskAdapter.TaskViewHolder {
         val itemBinding = TaskItemBinding //ItemAccountBinding
             .inflate(
                 LayoutInflater.from(parent.context),
                 parent,false)
        return TaskViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder :TaskAdapter.TaskViewHolder,
    position:Int){

        holder.bindTask(tasks[position])
    }

    inner class TaskViewHolder(private val itemBinding :TaskItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

            private var task = Task()

            init {
                itemView.setOnClickListener(this)
            }

            fun bindTask(task:Task){
                this.task = task

                itemBinding.taskName.text ="${task.taskName}"
                itemBinding.taskStatus.text = "${task.status}"
                itemBinding.deleteRowButton.setOnClickListener {
                    Snackbar.make(itemBinding.root,
                        "Delete by Button",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    removeTask(adapterPosition)
//                   removeTask(bindingAdapterPosition)
                }
            }

        override fun onClick(v: View?) {
            Snackbar.make(itemBinding.root,
                "${task.taskName},${task.status}",
                Snackbar.LENGTH_SHORT
                ).show()
        }

    }


}