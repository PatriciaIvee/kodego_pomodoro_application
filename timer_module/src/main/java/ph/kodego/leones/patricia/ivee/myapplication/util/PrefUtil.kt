package ph.kodego.leones.patricia.ivee.myapplication.util

import android.content.Context
import android.preference.PreferenceManager
import ph.kodego.leones.patricia.ivee.myapplication.MainActivity

class PrefUtil {
    companion object {
        //GET AND SET TASK NAME
        private const val TIMER_TASK_NAME_ID = "ph.kodego.leones.patricia.ivee.timer.task_name"
        fun getTaskName(context: Context): String? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(TIMER_TASK_NAME_ID, null)
        }
        fun setTaskName(taskName: String, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putString(TIMER_TASK_NAME_ID, taskName)
            editor.apply()
        }

        //GET AND SET TASK DESCRIPTION
        private const val TIMER_TASK_DESCRIPTION_ID = "ph.kodego.leones.patricia.ivee.timer.task_description"
        fun getTaskDescription(context: Context): String? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(TIMER_TASK_DESCRIPTION_ID, null)
        }


        //GET AND TIMER LENGTH (FOCUS)
        private const val TIMER_LENGTH_ID = "ph.kodego.leones.patricia.ivee.timer.timer_length"
        fun getTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH_ID, 10)
        }

        //GET AND TIMER LENGTH (SHORT)
        private const val SHORT_BREAK_TIMER_LENGTH_ID = "ph.kodego.leones.patricia.ivee.timer.timer_short_break_length"
        fun getShortBreakTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(SHORT_BREAK_TIMER_LENGTH_ID, 10)
        }

        //GET AND TIMER LENGTH (LONG BREAK)
        private const val LONG_BREAK_TIMER_LENGTH_ID = "ph.kodego.leones.patricia.ivee.timer.timer_long_break_length"
        fun getLongBreakTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(LONG_BREAK_TIMER_LENGTH_ID, 10)
        }

        // GET AND SET CYCLES
        private const val TIMER_CYCLES_ID = "ph.kodego.leones.patricia.ivee.timer.cycles"
        fun getTimerCycles(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_CYCLES_ID, 0)
        }
        fun setTimerCycles(cycles: Int, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putString(TIMER_CYCLES_ID, cycles.toString())
            editor.apply()
        }

        //GET AND SET LONG BREAK REPETITION

        private const val LONG_BREAK_REPETITION_ID = "ph.kodego.leones.patricia.ivee.timer.long_break_repetition"
        fun getLongBreakRepetition(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(LONG_BREAK_REPETITION_ID, 0)
        }
        fun setLongBreakRepetition(cycles: Int, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putString(LONG_BREAK_REPETITION_ID, cycles.toString())
            editor.apply()
        }

        //GET AND SET PREVIOUS TIMER LENGTH
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "ph.kodego.leones.patricia.ivee.timer.previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        //GET AND SET TIMER STATE
        private const val TIMER_STATE_ID = "ph.kodego.leones.patricia.ivee.timer.timer_state"

        fun getTimerState(context: Context): MainActivity.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return MainActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(state: MainActivity.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }




        //GET AND SET TIMER MODE
        private const val TIMER_MODE_ID = "ph.kodego.leones.patricia.ivee.timer.timer_mode"

        fun getTimerMode(context: Context): MainActivity.TimerMode {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_MODE_ID, 0)
            return MainActivity.TimerMode.values()[ordinal]
        }

        fun setTimerMode(state: MainActivity.TimerMode, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_MODE_ID, ordinal)
            editor.apply()
        }

        //GET AND SET SECONDS REMAINING
        private const val SECONDS_REMAINING_ID = "ph.kodego.leones.patricia.ivee.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        //GET AND SET ALARM IN BACKGROUND
        private const val ALARM_SET_TIME_ID = "ph.kodego.leones.patricia.ivee.timer.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}