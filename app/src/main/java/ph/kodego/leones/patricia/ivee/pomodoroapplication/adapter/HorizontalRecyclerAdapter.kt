package ph.kodego.leones.patricia.ivee.pomodoroapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.leones.patricia.ivee.pomodoroapplication.R

class HorizontalRecyclerAdapter(private val horizontalRecyclerDataList: ArrayList<String>) :
    RecyclerView.Adapter<HorizontalRecyclerAdapter.HorizontalRecyclerItemViewHolder>() {
    class HorizontalRecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalRecyclerItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_recycler_item, parent, false)
        return HorizontalRecyclerItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: HorizontalRecyclerItemViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.textview)
        textView.text = horizontalRecyclerDataList[position]
    }

    override fun getItemCount(): Int {
        return horizontalRecyclerDataList.size
    }

}