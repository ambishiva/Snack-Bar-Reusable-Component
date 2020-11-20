package com.geek.lib.snackbar

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import android.widget.TextView

object Alert {
    /**
     * Show SnackBar
     * @param activity: Activity
     * @param msg: Message to be displayed on the screen
     */
    @Deprecated(message = "This method need to be closed after SnackbarBuilder fully functional and will no more be used and that is going to replace with Alert.with().show()",
            replaceWith = ReplaceWith(
                    expression = "Alert.with(Activity, String).show()")
    )
    fun showSnackBar(activity: Activity?, msg: String) {
        if (activity != null) {
            Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Returns the [SnackBarBuilder] with calling [activity] object
     */
    fun with(activity: Activity, msg: String): SnackBarBuilder {
        return SnackBarBuilder(activity, msg)
    }

    /**
     * Returns the [SnackBarBuilder] with calling [activity] object
     */
    fun with(activity: Activity, @StringRes messageResId: Int): SnackBarBuilder {
        return SnackBarBuilder(activity, messageResId)
    }

    /*******
     * Helper methods*
     * ****/
    /**
     * Set the [typeface] from assets on [textView]
     */
    @SuppressWarnings("Typeface must be defined in assets")
    fun setTypeface(context: Context, textView: TextView, typeface: Typeface) {
        textView.typeface = typeface
    }
}