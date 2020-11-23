package com.geek.lib.snackbar

import android.view.View

/**Works with [SnackBarBuilder] to listen snackbar action click events*/
interface OnSnackBarActionClickListener {
    /**
     * On snack bar action clicked.
     *
     * @param uniqueId the unique id to identified the snackbar
     * @param view     the clicked view
     */
    fun onSnackBarActionClicked(uniqueId: Int, view: View)
}
