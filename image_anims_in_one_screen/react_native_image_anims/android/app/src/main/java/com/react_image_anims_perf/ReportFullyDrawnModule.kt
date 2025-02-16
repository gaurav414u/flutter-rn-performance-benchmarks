package com.react_image_anims_perf

import android.app.Activity
import android.os.Build
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class ReportFullyDrawnModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "ReportFullyDrawn"

    @ReactMethod
    fun reportFullyDrawn() {
        val activity: Activity? = currentActivity
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity.reportFullyDrawn()
        }
    }
}
