package com.example.kmp_image_anims

import android.app.Activity
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

actual fun reportFullyDrawn(): Unit {
    currentActivity?.reportFullyDrawn()
}

var currentActivity: Activity? = null

// Add this in your Application class or a suitable place that's always active
fun registerActivityLifecycleCallbacks(application: android.app.Application) {
    application.registerActivityLifecycleCallbacks(object :
        android.app.Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: android.os.Bundle?) {
            if (activity is ComponentActivity) {
                currentActivity = activity
            }
        }

        override fun onActivityStarted(activity: Activity) {
            if (activity is ComponentActivity) {
                currentActivity = activity
            }
        }

        override fun onActivityResumed(activity: Activity) {
            if (activity is ComponentActivity) {
                currentActivity = activity
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (currentActivity == activity) {
                currentActivity = null
            }
        }

        override fun onActivityStopped(activity: Activity) {
            if (currentActivity == activity) {
                currentActivity = null
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: android.os.Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            if (currentActivity == activity) {
                currentActivity = null
            }
        }
    })
}