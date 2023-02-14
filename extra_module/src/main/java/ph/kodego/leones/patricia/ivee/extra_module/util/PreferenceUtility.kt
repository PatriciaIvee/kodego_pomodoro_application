package ph.kodego.leones.patricia.ivee.extra_module.util

import android.content.Context
import ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer.TimerFragment

class PreferenceUtility {
    companion object {

        fun getTimerLength(context: Context): Int{
            //placeholder
            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.timer.previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }


        private const val TIMER_STATE_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.timer.timer_state"

        fun getTimerState(context: Context): TimerFragment.TimerState{
            val preferences = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerFragment.TimerState, context: Context){
            val editor = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }


        private const val SECONDS_REMAINING_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.timer.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val preferences = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)
            return  preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}