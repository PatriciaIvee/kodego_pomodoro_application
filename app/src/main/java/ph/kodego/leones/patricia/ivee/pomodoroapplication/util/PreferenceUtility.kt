package ph.kodego.leones.patricia.ivee.pomodoroapplication.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ph.kodego.leones.patricia.ivee.pomodoroapplication.databinding.FragmentTimerBinding
import ph.kodego.leones.patricia.ivee.pomodoroapplication.ui.timer.TimerFragment

class PreferenceUtility {
    private lateinit var prefs: SharedPreferences
    companion object {

        private const val TIMER_LENGTH_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.previous_timer_length"
        fun getTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH_ID,10)
        }
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }


        private const val TIMER_STATE_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.timer_state"

        fun getTimerState(context: Context): TimerFragment.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerFragment.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private const val TIMER_MODE_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.timer_mode"

        fun getTimerMode(context: Context): TimerFragment.TimerMode{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_MODE_ID, 0)
            return TimerFragment.TimerMode.values()[ordinal]
        }

        fun setTimerMode(state: TimerFragment.TimerMode, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_MODE_ID, ordinal)
            editor.apply()
        }

        private const val FOCUS_TIME_LENGTH_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.focus_time_length_seconds"

        fun getFocusTimeLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(FOCUS_TIME_LENGTH_ID, 0)
        }

        fun setFocusTimeLength(seconds: Int, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(FOCUS_TIME_LENGTH_ID, seconds)
            editor.apply()
        }

        private const val SHORT_BREAK_TIME_LENGTH_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.short_break_time_length_seconds"

        fun getShortBreakTimeLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(SHORT_BREAK_TIME_LENGTH_ID, 0)
        }

        fun setShortBreakTimeLength(seconds: Int, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(SHORT_BREAK_TIME_LENGTH_ID, seconds)
            editor.apply()
        }

        private const val LONG_BREAK_TIME_LENGTH_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.long_break_time_length_seconds"

        fun getLongBreakTimeLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(LONG_BREAK_TIME_LENGTH_ID, 0)
        }

        fun setLongBreakTimeLength(seconds: Int, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(LONG_BREAK_TIME_LENGTH_ID, seconds)
            editor.apply()
        }

        private const val REPETITION_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.repetition"

        fun getRepetitionLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(REPETITION_ID, 0)
        }

        fun setRepetitionLength(seconds: Int, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(REPETITION_ID, seconds)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val focusTimeLength = preferences.getInt(FOCUS_TIME_LENGTH_ID, 0)
            val shortBreakLength = preferences.getInt(SHORT_BREAK_TIME_LENGTH_ID, 0)
            val longBreakLength = preferences.getInt(LONG_BREAK_TIME_LENGTH_ID, 0)
            val repetition = preferences.getInt(REPETITION_ID, 0)

            val focusTimeInSeconds = focusTimeLength * 60
            val shortBreakInSeconds = shortBreakLength * 60
            val longBreakInSeconds = longBreakLength * 60
            val totalTimeInSeconds = focusTimeInSeconds + shortBreakInSeconds + longBreakInSeconds + (repetition - 1) * (focusTimeInSeconds + shortBreakInSeconds) + (if (repetition % 4 == 0) longBreakInSeconds else 0)
            val editor = preferences.edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "ph.kodego.leones.patricia.ivee.pomodoroapplication.util.backgrounded_time"

        fun getAlarmSetTime(context: Context):Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0 )
        }

        fun setAlarmSetTime(time:Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}