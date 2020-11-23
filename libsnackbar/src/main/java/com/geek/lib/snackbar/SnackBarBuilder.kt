package com.geek.lib.snackbar

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.trantor.lib.snackbar.*

private const val TAG = "chs-snackbar-ui"

class SnackBarBuilder {
    private var snackBarParam: SnackBarParam? = null
    private var snackbar: Snackbar? = null

    /**Constructor definition to create [SnackBarParam] based on [activity] and [messageResId]*/
    constructor(activity: Activity, messageResId: Int) {
        snackBarParam = SnackBarParam()
        snackBarParam?.activity = activity
        snackBarParam?.context = activity
        snackBarParam?.messageResId = messageResId
    }

    /**Constructor definition to create [SnackBarParam] based on [activity] and [message]*/
    constructor(activity: Activity, message: String) {
        snackBarParam = SnackBarParam()
        snackBarParam?.activity = activity
        snackBarParam?.context = activity
        snackBarParam?.message = message
    }

    /** To set [actionMessage] for [Snackbar]*/
    fun actionMessage(actionMessage: String) = apply {
        snackBarParam?.actionMessage = actionMessage
    }

    /** To set [actionMessageResId] for [Snackbar]*/
    fun actionMessage(@StringRes resId: Int) = apply {
        snackBarParam?.actionMessageResId = resId
    }

    /** To set [Snackbar] [view]*/
    fun parentView(view: View) = apply {
        snackBarParam?.parentView = view
        snackBarParam?.parentView?.isFocusable = false
    }

    /** To set custom view of [Snackbar] based on [view] as custom view and [messageView] and [actionView]*/
    fun view(view: View, messageView: TextView, actionView: TextView?) = apply {
        snackBarParam?.view = view
        snackBarParam?.messageView = messageView
        snackBarParam?.actionView = actionView
    }

    /** To set text color of action view of [Snackbar]*/
    fun actionTextColor(@ColorRes resId: Int) = apply {
        snackBarParam?.actionTextColorResId = resId
    }

    /** To set text color of [Snackbar] message*/
    fun textColor(@ColorRes resId: Int) = apply {
        snackBarParam?.textColor = resId
    }

    /** To set [duration] of [Snackbar]*/
    fun duration(duration: Int) = apply {
        snackBarParam?.snackBarDuration = duration
    }

    /** To set background color of [Snackbar]*/
    fun backgroundColor(@ColorRes color: Int) = apply {
        snackBarParam?.bgColorResId = color
    }

    /** To display snackbar on top of the application ui layer*/
    fun applicationOverlay() = apply {
        snackBarParam?.applicationOverlay = true
    }

    /** To identify [Snackbar] action click event with [uniqueId]*/
    fun id(uniqueId: Int) = apply {
        snackBarParam?.alertTaskId = uniqueId
    }

    /** To set [Snackbar] message maximum lines to be displayed*/
    fun messageMaxLines(maxLines: Int) = apply {
        snackBarParam?.messageMaxLine = maxLines
    }

    /** To set the message font of [Snackbar] with [typeface] that mus be defined in asset folder*/
    @SuppressWarnings("typeface must be defined in asset folder of application")
    fun font(@NonNull typeface: Typeface) = apply {
        snackBarParam?.typeface = typeface
    }

    /** To set [Snackbar] view minHeight*/
    fun minHeight(minHeight: Int) = apply {
        snackBarParam?.minHeight = minHeight
    }

    /** To display success [Snackbar] with [AlertParam.green] color*/
    fun success() = apply {
        snackBarParam?.bgColorResId = AlertParam.green
    }

    /** To display error [Snackbar] with [AlertParam.red] color*/
    fun error() = apply {
        snackBarParam?.bgColorResId = AlertParam.red
    }

    /** To listen the action click event of [Snackbar]*/
    fun actionClickListener(listener: OnSnackBarActionClickListener) = apply {
        snackBarParam?.onSnackBarActionListener = listener
    }


    /**To set margin of [Snackbar] view*/
    fun margin(left: Int, top: Int, right: Int, bottom: Int) = apply {
        snackBarParam?.layoutParams?.leftMargin = left
        snackBarParam?.layoutParams?.topMargin = top
        snackBarParam?.layoutParams?.rightMargin = right
        snackBarParam?.layoutParams?.bottomMargin = bottom
    }

    /**To set width of [Snackbar] view*/
    fun width(width: Int) = apply {
        snackBarParam?.layoutParams?.width = width
    }

    /**To set height of [Snackbar] view*/
    fun height(height: Int) = apply {
        snackBarParam?.layoutParams?.height = height
    }

    fun layoutParams(params: FrameLayout.LayoutParams) = apply {
        snackBarParam?.layoutParams = params
    }

    /**
     * Finally show the [Snackbar]
     */
    fun show(): Snackbar? {
        if (snackbar != null)
            snackbar?.dismiss()
        if (snackBarParam?.activity == null) {
            return snackbar
        }

        if (snackBarParam?.parentView == null) {
            snackBarParam?.parentView = snackBarParam?.activity?.findViewById(android.R.id.content)
        }

        snackbar = Snackbar.make(snackBarParam?.parentView!!, "", snackBarParam?.snackBarDuration!!)

        // check for custom view
        setCustomView()
        //check for layout params
        setLayoutParams()
        // Checked for Message and set on view
        setMessage()
        // checked for ActionMessage and set on view
        setActionMessage()
        setMessageMaxLine()
        setMessageTextColor()
        setBgColor()
        setFont()
        setHeight()
        //TODO: this need to be removed when tab activity with view pager introduced
        if (snackBarParam?.applicationOverlay!!) {
            //check if user has granted permission for this app to manage overlay
            if (isPermissionGranted()) {
                showWrapper()
            } else {
                Log.d(
                    TAG,
                    "Required permission not granted and so displaying normal snackbar with attributes" +
                            "\n android.permission.ACTION_MANAGE_OVERLAY_PERMISSION and android.permission.SYSTEM_ALERT_WINDOW"
                )
                snackbar?.show()
            }
        } else {
            snackbar?.show()
        }

        return snackbar
    }

    /**To apply transparent background as requested */
    private fun isTransparentBgColorApplied(): Boolean {
        //check if requested bg color is equivalent to android.R.transparent
        if (ContextCompat.getColor(
                snackBarParam?.activity!!,
                snackBarParam?.bgColorResId!!
            )
            == ContextCompat.getColor(
                snackBarParam?.activity!!,
                android.R.color.transparent
            )
        ) {
            // then need to set all view's bg color that means both (snackbar itself and custom view if added)
            snackBarParam?.view?.setBackgroundColor(
                ContextCompat.getColor(
                    snackBarParam?.activity!!,
                    snackBarParam?.bgColorResId!!
                )
            )
            snackbar?.view?.setBackgroundColor(snackBarParam?.bgColorResId!!)
            return true
        }
        return false
    }

    private fun setCustomView() {
        if (snackBarParam!!.view != null) {
            val layout = snackbar?.view as Snackbar.SnackbarLayout
            if (snackBarParam?.view?.parent != null)
                (snackBarParam?.view?.parent as ViewGroup).removeView(snackBarParam?.view)
            layout.addView(snackBarParam?.view)
            layout.setPadding(0, 0, 0, 0)
        }
    }

    private fun setMessage() {
        //prepare message
        val message: String
        when {
            snackBarParam?.messageResId != -1 -> message =
                snackBarParam?.context?.getString(snackBarParam?.messageResId!!)!!
            !snackBarParam?.message.isNullOrEmpty() -> message = snackBarParam?.message!!
            else -> message = ""
        }
        //set message on component
        when {
            snackBarParam?.messageView != null -> snackBarParam?.messageView?.text = message
            else -> snackbar?.setText(message)
        }
    }

    private fun setActionMessage() {
        //prepare action message
        val message: String
        when {
            snackBarParam?.actionMessageResId != -1 -> message =
                snackBarParam?.context?.getString(snackBarParam?.actionMessageResId!!)!!
            !snackBarParam?.actionMessage.isNullOrEmpty() -> message =
                snackBarParam?.actionMessage!!
            else -> message = ""
        }

        //set action message on to component
        when {
            message.isEmpty() -> snackbar?.setAction("", null)
            snackBarParam?.actionView != null -> {
                snackBarParam?.actionView?.text = message
                snackBarParam?.actionView?.setOnClickListener {
                    snackBarParam?.onSnackBarActionListener?.onSnackBarActionClicked(
                        snackBarParam?.alertTaskId!!,
                        it
                    )
                }
            }
            else -> {
                snackbar?.setAction(message) {
                    snackBarParam?.onSnackBarActionListener?.onSnackBarActionClicked(
                        snackBarParam?.alertTaskId!!,
                        it
                    )
                }
            }
        }
    }

    private fun setMessageMaxLine() {
        if (snackBarParam?.messageMaxLine != -1) {
            when {
                snackBarParam?.messageView != null -> snackBarParam?.messageView?.maxLines =
                    snackBarParam?.messageMaxLine!!
                else -> {
                    //This is the text view inside inbuilt snackbar
                    val tv = snackbar?.view?.findViewById<View>(R.id.snackbar_text) as TextView
                    tv.maxLines = snackBarParam?.messageMaxLine!!
                }
            }
        }
    }

    private fun setMessageTextColor() {
        if (snackBarParam?.textColor != -1) {
            when {
                snackBarParam?.messageView != null -> {
                    snackBarParam?.messageView?.setTextColor(
                        ContextCompat.getColorStateList(
                            snackBarParam?.context!!,
                            snackBarParam?.textColor!!
                        )
                    )
                }
                else -> {
                    val tv = snackbar?.view?.findViewById<View>(R.id.snackbar_text) as TextView
                    tv.setTextColor(
                        ContextCompat.getColorStateList(
                            snackBarParam?.context!!,
                            snackBarParam?.textColor!!
                        )
                    )
                }
            }
        }
    }

    private fun setBgColor() {
        if (snackBarParam?.bgColorResId != -1) {
            //check for transparent color
            if (!isTransparentBgColorApplied()) {
                when {
                    snackBarParam?.view != null -> {
                        snackBarParam?.view?.setBackgroundColor(
                            ContextCompat.getColor(
                                snackBarParam?.activity!!,
                                snackBarParam?.bgColorResId!!
                            )
                        )
                    }
                    else -> {
                        snackbar?.view?.setBackgroundColor(
                            ContextCompat.getColor(
                                snackBarParam?.activity!!,
                                snackBarParam?.bgColorResId!!
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setFont() {
        val typeface: Typeface
        if (snackBarParam?.typeface != null) {
            typeface = snackBarParam?.typeface!!
            //find message view
            val messageView = if (snackBarParam?.messageView != null)
                snackBarParam?.messageView!!
            else
                snackbar?.view?.findViewById<View>(R.id.snackbar_text) as TextView


            //find action view
            val actionView = if (snackBarParam?.actionView != null)
                snackBarParam?.actionView!!
            else
                snackbar?.view?.findViewById<View>(R.id.snackbar_action) as TextView

            //set typeface
            Alert.setTypeface(snackBarParam?.context!!, messageView, typeface)
            Alert.setTypeface(snackBarParam?.context!!, actionView, typeface)
        }
    }

    private fun setHeight() {
        if (snackBarParam?.minHeight != -1) {
            when {
                snackBarParam?.view != null -> {
                    snackbar?.view?.minimumHeight = snackBarParam?.minHeight!!
                }
                else -> {
                    val layout = snackbar?.view as Snackbar.SnackbarLayout
                    layout.minimumHeight = snackBarParam?.minHeight!!
                }
            }
        }
    }

    private fun setLayoutParams() {
        snackbar?.view?.apply {
            layoutParams?.apply {
                width = snackBarParam?.layoutParams?.width ?: ViewGroup.LayoutParams.MATCH_PARENT
                (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = snackBarParam?.layoutParams?.leftMargin ?: leftMargin
                    marginEnd = snackBarParam?.layoutParams?.rightMargin ?: rightMargin
                    setMargins(
                        marginStart,
                        snackBarParam?.layoutParams?.topMargin ?: topMargin,
                        marginEnd,
                        snackBarParam?.layoutParams?.bottomMargin ?: bottomMargin
                    )
                }
            }
            snackBarParam?.context?.let { setBackgroundColor(it.getColor(android.R.color.transparent)) }
        }

        // Below code is only required to set the bottom margin which is not working with snackbar?.view.setMargin()
        snackBarParam?.view?.apply {
            layoutParams?.apply {
                width = snackBarParam?.layoutParams?.width ?: ViewGroup.LayoutParams.MATCH_PARENT
                (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    setMargins(
                        leftMargin,
                        topMargin,
                        rightMargin,
                        snackBarParam?.layoutParams?.bottomMargin ?: bottomMargin
                    )
                }
            }
        }
    }

    private fun showWrapper() {
        val snackbarWrapper = SnackbarWrapper(snackBarParam ?: SnackBarParam())
        snackbarWrapper.show()
    }

    /**check if permission already granted to use manage overlay snackbar
    on top of the application ui layer*/
    private fun isPermissionGranted(): Boolean {
        val permissionSystemAlertWindow = android.Manifest.permission.SYSTEM_ALERT_WINDOW
        val permissionManageOverlay = Settings.ACTION_MANAGE_OVERLAY_PERMISSION

        return ((ContextCompat.checkSelfPermission(
            snackBarParam?.context!!,
            permissionSystemAlertWindow
        )
                == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(
            snackBarParam?.context!!,
            permissionManageOverlay
        )
                == PackageManager.PERMISSION_GRANTED))
    }

    fun dismissSnackBar() {
        snackbar?.dismiss()
    }
}
