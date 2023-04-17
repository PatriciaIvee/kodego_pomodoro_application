package ph.kodego.leones.patricia.ivee.pomodoroapplication.model

data class TaskCompletionEvent(
    val taskName: String,
    val startTime: Long,
    val endTime: Long,
    val cyclesCompleted: Int
)

interface OnTaskCompleteListener {
    fun onTaskCompleted(event: TaskCompletionEvent)
}