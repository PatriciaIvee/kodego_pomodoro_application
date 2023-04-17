package ph.kodego.leones.patricia.ivee.pomodoroapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer.TimerFragment
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.NotificationUtil
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PrefUtil


class TimerExpiredReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(TimerFragment.TimerState.STOPPED, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}