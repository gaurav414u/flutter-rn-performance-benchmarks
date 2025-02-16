package com.example.kmp_image_anims

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }
}