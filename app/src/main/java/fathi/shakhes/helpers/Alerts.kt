package fathi.shakhes.helpers

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import shakhes.R

fun showExitAppDialog(activity: Activity) {
    showAlert(activity,
        "exitApp",
        activity.getString(R.string.confirmation_close_app_title),
        activity.getString(R.string.confirmation_close_app_description),
        "خروج",
        "انصراف",
        { ActivityCompat.finishAffinity(activity) }
    )
}

private fun showAlert(
    context: Context,
    dialogName: String,
    title: String,
    description: String,
    positiveButtonText: String,
    negativeButtonText: String?,
    listener: DialogListener = DialogListener {},
    isCancelable: Boolean = true,
) {
    val layout = LayoutInflater.from(context).inflate(R.layout.dialog, null)
    val negativeButton: MaterialButton = layout.findViewById(R.id.negativeButton)
    val positiveButton: MaterialButton = layout.findViewById(R.id.positiveButton)
    val dialogTitle: MaterialTextView = layout.findViewById(R.id.dialogTitle)
    val dialogDescription: MaterialTextView = layout.findViewById(R.id.dialogDescription)
    dialogTitle.text = title
    dialogDescription.text = description
    positiveButton.text = positiveButtonText
    if (negativeButtonText != null) {
        negativeButton.text = negativeButtonText
        negativeButton.isVisible = true
    } else {
        negativeButton.isVisible = false
    }
    val alertDialog =
        MaterialAlertDialogBuilder(context, R.style.MaterialComponents_MaterialAlertDialog)
            .setView(layout).create()
    positiveButton.setOnClickListener {
        alertDialog.dismiss()
        listener.onPositiveButtonClick()
    }
    negativeButton.setOnClickListener {
        alertDialog.dismiss()
        listener.onNegativeButtonClick()
    }
    alertDialog.setCancelable(isCancelable)
    alertDialog.setOnCancelListener { obj: DialogInterface -> obj.dismiss() }
    alertDialog.show()
}
