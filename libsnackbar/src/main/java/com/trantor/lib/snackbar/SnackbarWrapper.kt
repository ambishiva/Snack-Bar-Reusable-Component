package com.veriownglobal.chspanel.alert.snackbar

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar
import com.trantor.lib.snackbar.Alert
import com.trantor.lib.snackbar.SnackBarParam

private const val TAG = "chs-snackbar-ui"

/**To display snackbar on top of the application layer*/
//TODO: this need to be removed when tab activity with view pager introduced
@SuppressWarnings(
    "Required permissions  " +
            "<uses-permission android:name=\"android.permission.ACTION_MANAGE_OVERLAY_PERMISSION\" />\n" +
            "    <uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\" />"
)
class SnackbarWrapper(private val snackBarParam: SnackBarParam) {
    private val windowManager: WindowManager =
        snackBarParam.activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var externalCallback: Snackbar.Callback? = null
    private var snackbar: Snackbar? = null

    fun show() {
        try {
            //Due to Permission issue we added this check
            var layoutParamsType: Int? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParamsType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParamsType = WindowManager.LayoutParams.TYPE_PHONE;
            }
            val layoutParams = createDefaultLayoutParams(layoutParamsType, null)
            windowManager.addView(object : FrameLayout(snackBarParam.activity!!) {
                override fun onAttachedToWindow() {
                    super.onAttachedToWindow()
                    //onRootViewAvailable(this)
                }
            }, layoutParams)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to show snackbar: ${e.message}")
        }
    }

//    private fun onRootViewAvailable(rootView: FrameLayout) {
//        val snackbarContainer = object :
//            CoordinatorLayout(ContextThemeWrapper(snackBarParam.activity!!, null)) {
//            override fun onAttachedToWindow() {
//                super.onAttachedToWindow()
//                onSnackbarContainerAttached(rootView, this)
//            }
//        }
//        windowManager.addView(
//            snackbarContainer,
//            createDefaultLayoutParams(
//                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
//                rootView.windowToken
//            )
//        )
//    }

    private fun onSnackbarContainerAttached(rootView: View, snackbarContainer: CoordinatorLayout) {
        snackbar = Snackbar.make(rootView, "", snackBarParam.snackBarDuration)
        snackbar?.setCallback(object : Snackbar.Callback() {
            override fun onDismissed(snackbar: Snackbar?, event: Int) {
                super.onDismissed(snackbar, event)
                // Clean up (NOTE! This callback can be called multiple times)
                if (snackbarContainer.parent != null && rootView.getParent() != null) {
                    windowManager.removeView(snackbarContainer)
                    windowManager.removeView(rootView)
                }
                if (externalCallback != null) {
                    externalCallback!!.onDismissed(snackbar, event)
                }
            }

            override fun onShown(snackbar: Snackbar?) {
                super.onShown(snackbar)
                if (externalCallback != null) {
                    externalCallback!!.onShown(snackbar)
                }
            }
        })

        setCustomView()
        //check for layout params
        setLayoutParams()
        //should be displayed with full width as per screen width
        snackbar?.view?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        // Checked for Message and set on view
        setMessage()
        // checked for ActionMessage and set on view
        setActionMessage()
        setMessageMaxLine()
        setMessageTextColor()
        setBgColor()
        setFont()

        snackbar?.show()
    }

    private fun createDefaultLayoutParams(
        type: Int,
        windowToken: IBinder?
    ): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.format = PixelFormat.TRANSLUCENT
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = GravityCompat.getAbsoluteGravity(
            Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,
            ViewCompat.LAYOUT_DIRECTION_LTR
        )
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        layoutParams.type = type
        layoutParams.token = windowToken
        return layoutParams
    }

    private fun setCustomView() {
        if (snackBarParam.view != null) {
            val layout = snackbar?.view as Snackbar.SnackbarLayout
            if (snackBarParam.view?.parent != null)
                (snackBarParam.view?.parent as ViewGroup).removeView(snackBarParam.view)
            layout.addView(snackBarParam.view)
            layout.setPadding(0, 0, 0, 0)
        }
    }

    private fun setMessage() {
        //prepare message
        val message: String
        when {
            snackBarParam.messageResId != -1 -> message =
                snackBarParam.context?.getString(snackBarParam.messageResId)!!
            !snackBarParam.message.isNullOrEmpty() -> message = snackBarParam.message
            else -> message = ""
        }
        //set message on component
        when {
            snackBarParam.messageView != null -> snackBarParam.messageView?.text = message
            else -> snackbar?.setText(message)
        }
    }

    private fun setActionMessage() {
        //prepare action message
        val message: String
        when {
            snackBarParam.actionMessageResId != -1 -> message =
                snackBarParam.context?.getString(snackBarParam.actionMessageResId)!!
            !snackBarParam.actionMessage.isNullOrEmpty() -> message = snackBarParam.actionMessage
            else -> message = ""
        }

        //set action message on to component
        when {
            message.isEmpty() -> snackbar?.setAction("", null)
            snackBarParam.actionView != null -> {
                snackBarParam.actionView?.text = message
                snackBarParam.actionView?.setOnClickListener {
                    snackBarParam.onSnackBarActionListener?.onSnackBarActionClicked(
                        snackBarParam.alertTaskId,
                        it
                    )
                }
            }
            else -> {
                snackbar?.setAction(message) {
                    snackBarParam.onSnackBarActionListener?.onSnackBarActionClicked(
                        snackBarParam.alertTaskId,
                        it
                    )
                }
            }
        }
    }

    private fun setMessageMaxLine() {
        if (snackBarParam.messageMaxLine != -1 && snackBarParam.messageView != null) {
            snackBarParam.messageView?.maxLines = snackBarParam.messageMaxLine
        }
    }

    private fun setMessageTextColor() {
        if (snackBarParam.textColor != -1) {
            snackBarParam.messageView?.setTextColor(
                ContextCompat.getColorStateList(
                    snackBarParam.context!!,
                    snackBarParam.textColor!!
                )
            )
        }
    }

    private fun setBgColor() {
        if (snackBarParam.bgColorResId != -1 && snackBarParam.view != null) {
            snackbar?.view?.setBackgroundColor(
                ContextCompat.getColor(
                    snackBarParam.activity!!,
                    snackBarParam.bgColorResId
                )
            )
        }
    }

    private fun setFont() {
        val typeface: Typeface
        if (snackBarParam.typeface != null) {
            typeface = snackBarParam.typeface!!
            //find message view
            val messageView =
                snackBarParam.messageView!!


            //find action view
            val actionView = snackBarParam.actionView!!


            //set typeface
            Alert.setTypeface(snackBarParam.context!!, messageView, typeface)
            Alert.setTypeface(snackBarParam.context!!, actionView, typeface)
        }
    }

    private fun setLayoutParams() {
        snackbar?.view?.apply {
            layoutParams?.apply {
                width = snackBarParam.layoutParams.width ?: ViewGroup.LayoutParams.MATCH_PARENT
                (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = snackBarParam.layoutParams.leftMargin ?: leftMargin
                    marginEnd = snackBarParam.layoutParams.rightMargin ?: rightMargin
                    setMargins(
                        marginStart,
                        snackBarParam.layoutParams.topMargin ?: topMargin,
                        marginEnd,
                        snackBarParam.layoutParams.bottomMargin ?: bottomMargin
                    )
                }
            }
            snackBarParam.context?.let { setBackgroundColor(it.getColor(android.R.color.transparent)) }
        }

        // Below code is only required to set the bottom margin which is not working with snackbar?.view.setMargin()
        snackBarParam.view?.apply {
            layoutParams?.apply {
                width = snackBarParam.layoutParams.width ?: ViewGroup.LayoutParams.MATCH_PARENT
                (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    setMargins(
                        leftMargin,
                        topMargin,
                        rightMargin,
                        snackBarParam.layoutParams.bottomMargin ?: bottomMargin
                    )
                }
            }
        }
    }

    private class Action(val text: CharSequence, val listener: View.OnClickListener)

}