package com.reschikov.crocodilemobile.testtask.utils

import android.app.Activity
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.reschikov.crocodilemobile.testtask.R

private const val START_INDEX = 0

fun TextView.createShowAsUrl(){
    val string = text.toString()
    val policy =  SpannableString(string)
    policy.setSpan(URLSpan(string), START_INDEX, string.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
    text = policy
}

fun Activity.showAlertDialog(title: Int, message: String) {
    AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
        .setTitle(title)
        .setIcon(R.drawable.ic_warning)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(R.string.but_ok) { dialog, which ->
            dialog.dismiss()
        }
        .create()
        .show()
}