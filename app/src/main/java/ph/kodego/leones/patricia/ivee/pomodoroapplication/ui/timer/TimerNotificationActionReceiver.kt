package ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.NotificationUtil
import ph.kodego.leones.patricia.ivee.pomodoroapplication.util.PreferenceUtility

class TimerNotificationActionReceiver : BroadcastReceiver()  {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            AppConstants.ACTION_STOP -> {
                TimerFragment.removeAlarm(context)
                PreferenceUtility.setTimerState(TimerFragment.TimerState.STOPPED, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PreferenceUtility.getSecondsRemaining(context)
                val alarmSetTime = PreferenceUtility.getAlarmSetTime(context)
                val nowSeconds = TimerFragment.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PreferenceUtility.setSecondsRemaining(secondsRemaining, context)

                TimerFragment.removeAlarm(context)
                PreferenceUtility.setTimerState(TimerFragment.TimerState.PAUSED, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PreferenceUtility.getSecondsRemaining(context)
                val wakeUpTime = TimerFragment.setAlarm(context, TimerFragment.nowSeconds, secondsRemaining)
                PreferenceUtility.setTimerState(TimerFragment.TimerState.RUNNING, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PreferenceUtility.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = TimerFragment.setAlarm(context, TimerFragment.nowSeconds, secondsRemaining)
                PreferenceUtility.setTimerState(TimerFragment.TimerState.RUNNING, context)
                PreferenceUtility.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}