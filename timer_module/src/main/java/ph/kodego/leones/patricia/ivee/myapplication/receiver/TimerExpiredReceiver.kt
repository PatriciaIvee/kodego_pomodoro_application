package ph.kodego.leones.patricia.ivee.myapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ph.kodego.leones.patricia.ivee.myapplication.MainActivity
import ph.kodego.leones.patricia.ivee.myapplication.util.NotificationUtil
import ph.kodego.leones.patricia.ivee.myapplication.util.PrefUtil


class TimerExpiredReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(MainActivity.TimerState.STOPPED, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}