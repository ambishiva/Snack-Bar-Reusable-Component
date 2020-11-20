package com.trantor.sample.snackbar

import android.R
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trantor.lib.snackbar.Alert


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item)
//        showSnackBarWithBackGroundColor()
//        showSnackBarWithCustomTextColor()
//        showSnackBarWithCustomDuration()
//        showSnackBarWithCustomFont()
//        showSnackBarForErrorMessage()
//        showSnackBarForSuccessMessage()
        //showSnackBarWithCustomHeight()



    }

//    private fun showSnackBarWithCustomHeight() {
//        val snackBarBuilder = Alert.with(this, "Custom Snackbar Error Message")
//            .textColor(R.color.white)
//            .success()
//            .minHeight(R.dimen.notification_large_icon_width)
//        snackBarBuilder.show()
//    }

    private fun showSnackBarForErrorMessage() {
        val snackBarBuilder = Alert.with(this, "Custom Snackbar Error Message")
            .textColor(R.color.white)
            .error().show()
    }

    private fun showSnackBarForSuccessMessage() {
        val snackBarBuilder = Alert.with(this, "Custom Snackbar Error Message")
            .textColor(R.color.white)
            .success().show()
    }

    private fun showSnackBarWithCustomTextColor() {
        val snackBarBuilder = Alert.with(this, "Custom Snackbar with red text color")
            .textColor(android.R.color.holo_red_dark).show()
    }


    private fun showSnackBarWithBackGroundColor() {
        val snackBarBuilder = Alert.with(this, "Custom Snackbar with red background")
            .backgroundColor(android.R.color.holo_red_dark).show()
    }

    private fun showSnackBarWithCustomDuration() {
        val duration = 20000
        val snackBarBuilder = Alert.with(this, "Custom Snackbar with 20 seconds")
            .textColor(android.R.color.holo_red_dark)
            .duration(20000).show()
        snackBarBuilder.show()
    }

    private fun showSnackBarWithCustomFont() {
        val hindiFont = Typeface.createFromAsset(assets, "hind_font.ttf")
        val snackBarBuilder = Alert.with(this, "Custom Snackbar with hindi font")
            .textColor(android.R.color.holo_red_dark)
            .font(hindiFont)
        snackBarBuilder.show()
    }
}