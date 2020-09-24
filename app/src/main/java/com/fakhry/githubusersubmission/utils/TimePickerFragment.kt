package com.fakhry.githubusersubmission.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.fakhry.githubusersubmission.ui.SettingsFragment
import java.util.*

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private var dialogTimeListener: DialogTimeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val fragment = parentFragment
        //calling DialogFragment from fragment
        if(fragment is SettingsFragment){
            this.dialogTimeListener = fragment.dialogTimeListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (dialogTimeListener != null) {
            dialogTimeListener = null
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val formatHour24 = true

        return TimePickerDialog(activity, this, hour, minute, formatHour24)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        dialogTimeListener?.onDialogTimeSet(tag, hourOfDay, minute)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dialogTimeListener?.onCancel()
    }

    interface DialogTimeListener {
        fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int)
        fun onCancel()
    }
}