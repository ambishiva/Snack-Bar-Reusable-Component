package com.geek.lib.snackbar

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class SnackBarParam : AlertParam() {
    internal var activity: Activity? = null
    internal var actionMessage: String = ""
    internal var actionMessageResId = -1
    internal var actionTextColorResId = -1
    internal var snackBarDuration = Snackbar.LENGTH_LONG
    internal var parentView: View? = null
    internal var view: View? = null
    internal var messageView: TextView? = null
    internal var actionView: TextView? = null
    internal var minHeight: Int = -1
    internal var layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    internal var onSnackBarActionListener: OnSnackBarActionClickListener? = null

    // This variable used to show snackbar on top of the application based on window manager
    internal var applicationOverlay: Boolean = false
}
