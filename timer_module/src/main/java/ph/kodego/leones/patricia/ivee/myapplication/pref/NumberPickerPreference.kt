package ph.kodego.leones.patricia.ivee.myapplication.pref

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.NumberPicker
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import ph.kodego.leones.patricia.ivee.myapplication.R

//class NumberPickerPreference : Preference {
//    private lateinit var mNumberPicker: NumberPicker
//    private var mValue = 0
////
////    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
////        init()
////    }
////
////    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
////        context,
////        attrs,
////        defStyleAttr
////    ) {
////        init()
////    }
////
////    private fun init() {
////        setWidgetLayoutResource(R.layout.preference_widget_number_picker)
////    }
////
////    fun onBindViewHolder(holder: PreferenceViewHolder) {
////        super.onBindViewHolder(holder)
////        mNumberPicker = holder.findViewById(R.id.number_picker)
////        mNumberPicker!!.minValue = 0
////        mNumberPicker!!.maxValue = 100
////        mNumberPicker!!.value = mValue
////        mNumberPicker!!.setOnValueChangedListener { picker, oldVal, newVal ->
////            mValue = newVal
////            persistInt(newVal)
////        }
////    }
////
////    protected fun onGetDefaultValue(a: TypedArray, index: Int): Any {
////        return a.getInt(index, 0)
////    }
////
////    protected fun onSetInitialValue(defaultValue: Any?) {
////        value = getPersistedInt(defaultValue as Int?)
////    }
////
////    var value: Int
////        get() = mValue
////        set(value) {
////            val changed = value != mValue
////            if (changed) {
////                mValue = value
////                persistInt(value)
////                notifyChanged()
////            }
////        }
//}