package ph.kodego.leones.patricia.ivee.extra_module

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PreferenceUtility
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PreferenceUtility.Companion.setAlarmSetTime

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //TODO: show notification

//        PreferenceUtility.setTimerState(TimerFragment.TimerState.Stopped, context)
//        PreferenceUtility.setAlarmSetTime(0, context)
        val prefs = context.getSharedPreferences("my_prefs_file", Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("timer_state", TimerFragment.TimerState.Stopped.ordinal)
            .putLong("alarm_set_time", 0L)
            .apply()
    }
}