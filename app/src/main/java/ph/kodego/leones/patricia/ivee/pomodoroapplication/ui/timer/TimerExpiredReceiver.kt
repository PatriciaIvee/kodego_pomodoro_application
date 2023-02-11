package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PreferenceUtility
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PreferenceUtility.Companion.setAlarmSetTime

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       //TODO: Show notification

        PreferenceUtility.setTimerState(TimerFragment.TimerState.STOPPED, context)
        PreferenceUtility.setAlarmSetTime(0, context)

    }
}