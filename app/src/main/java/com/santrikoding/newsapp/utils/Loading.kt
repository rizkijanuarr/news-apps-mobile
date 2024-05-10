package com.santrikoding.newsapp.utils

import android.app.Dialog
import android.content.Context
import com.santrikoding.newsapp.R

private lateinit var dialog: Dialog

fun showLoading(context: Context) {
    dialog = Dialog(context)
    dialog.setContentView(R.layout.layout_loading)
    dialog.setCancelable(false)
    dialog.show()
}

fun closeLoading() {
    dialog.dismiss()
}