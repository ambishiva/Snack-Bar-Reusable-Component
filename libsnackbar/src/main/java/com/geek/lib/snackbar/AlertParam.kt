package com.geek.lib.snackbar

import android.content.Context
import android.graphics.Typeface

/**Base class for the all type of alert in builder classes*/
open class AlertParam {
    internal var context: Context? = null
    internal var message: String = ""
    internal var typeface: Typeface? = null
    internal var messageResId: Int = -1
    internal var alertTaskId: Int = 0
    internal var messageMaxLine: Int = -1
    internal var textColor: Int = -1
    internal var bgColorResId: Int = -1

    companion object {
        const val red = android.R.color.holo_red_light
        const val green = android.R.color.holo_green_light
    }
}