package ph.kodego.leones.patricia.ivee.pomodoroapplication.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.leones.patricia.ivee.pomodoroapplication.adapter.HorizontalRecyclerAdapter

class SwipeCallBack (dragDirs: Int, swipeDirs:Int) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs){
    var horizontalRecyclerViewAdapter:HorizontalRecyclerAdapter? = null
    var background: ColorDrawable = ColorDrawable(Color.BLACK)

    override fun onMove(

        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        val position = viewHolder.adapterPosition
//        horizontalRecyclerViewAdapter!!.removeStudent(position)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(
            canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView= viewHolder.itemView

        if (dX >0){//Swipe to Right
            background = ColorDrawable(Color.LTGRAY)
            background.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
        } else if (dX < 0){ //Swipe to left
            background = ColorDrawable(Color.DKGRAY)
            background.setBounds(
                itemView.right, itemView.top,
                itemView.right + dX.toInt(),
                itemView.bottom
            )
        } else { // view is un swiped
            background.setBounds(0,0,0,0)
        }

        background.draw(canvas)
    }


}