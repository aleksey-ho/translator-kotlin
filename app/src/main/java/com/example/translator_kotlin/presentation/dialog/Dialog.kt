package com.example.translator_kotlin.presentation.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.example.translator_kotlin.R


class Dialog {

    fun showErrorDialog(activity: Activity?) {
        val activity = activity ?: return
        prepareDialog(
            activity,
            titleRes = R.string.error_title,
            bodyRes = R.string.error_default,
            positiveButtonRes = R.string.ok,
        ).show()
    }

    fun showClearHistoryDialog(activity: Activity?,
                               clearHistory: Boolean,
                               onButtonClick: () -> Unit) {
        val activity = activity ?: return
        val title = if (clearHistory)
            R.string.view_pager_history
        else
            R.string.view_pager_favorites
        val message = if (clearHistory)
            R.string.delete_history_question
        else
            R.string.delete_favorites_question
        prepareDialog(
            activity,
            titleRes = title,
            bodyRes = message,
            positiveButtonRes = R.string.yes,
            onPositiveButtonClick = {
                onButtonClick()
            },
            negativeButtonRes = R.string.cancel,
        ).show()
    }

    fun showInitDialog(activity: Activity?,
                       onButtonClick: () -> Unit) {
        val activity = activity ?: return
        prepareDialog(
            activity,
            titleRes = R.string.init_langs_title,
            bodyRes = R.string.init_langs_description,
            positiveButtonRes = R.string.ok,
            onPositiveButtonClick = {
                onButtonClick()
            },
        ).show()
    }

    private fun prepareDialog(
        activity: Activity,
        @StringRes titleRes: Int?,
        @StringRes bodyRes: Int?,
        @StringRes positiveButtonRes: Int? = null,
        onPositiveButtonClick: (() -> Unit)? = null,
        @StringRes negativeButtonRes: Int? = null,
        onNegativeButtonClick: (() -> Unit)? = null,
    ): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        val dialogLayout = activity.layoutInflater.inflate(R.layout.layout_dialog, null)
        builder.setView(dialogLayout)
        val alertDialog = builder.create()

        titleRes?.let {
            val titleTextView = dialogLayout.findViewById<TextView>(R.id.title)
            titleTextView.setText(it)
            titleTextView.visibility = View.VISIBLE
        }

        bodyRes?.let {
            val bodyTextView = dialogLayout.findViewById<TextView>(R.id.body)
            bodyTextView.setText(it)
            bodyTextView.visibility = View.VISIBLE
        }

        positiveButtonRes?.let {
            val positiveButton = dialogLayout.findViewById<Button>(R.id.positiveButton)
            positiveButton.visibility = View.VISIBLE
            positiveButton.setText(it)
            positiveButton.setOnClickListener {
                onPositiveButtonClick?.invoke()
                alertDialog.dismiss()
            }
        }

        negativeButtonRes?.let {
            val negativeButton = dialogLayout.findViewById<Button>(R.id.negativeButton)
            negativeButton.visibility = View.VISIBLE
            negativeButton.setText(it)
            negativeButton.setOnClickListener {
                onNegativeButtonClick?.invoke()
                alertDialog.dismiss()
            }
        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        return alertDialog
    }

}