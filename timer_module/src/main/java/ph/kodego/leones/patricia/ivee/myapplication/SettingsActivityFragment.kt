package ph.kodego.leones.patricia.ivee.myapplication

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.h6ah4i.android.preference.NumberPickerPreferenceCompat
import com.h6ah4i.android.preference.NumberPickerPreferenceDialogFragmentCompat


class SettingsActivityFragment: PreferenceFragmentCompat() {
    private val DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG"

    override fun onDisplayPreferenceDialog(preference: Preference) {
        // check if dialog is already showing
        if (parentFragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return
        }
        val dialogFragment: DialogFragment?
        dialogFragment = if (preference is NumberPickerPreferenceCompat) {
            NumberPickerPreferenceDialogFragmentCompat.newInstance(preference.getKey())
        } else {
            null
        }
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, DIALOG_FRAGMENT_TAG)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}