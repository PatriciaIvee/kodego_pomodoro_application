package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private val _taskName = MutableLiveData<String>().apply {
        value = "Task _Name_"
    }
    val taskName: LiveData<String> = _taskName

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}