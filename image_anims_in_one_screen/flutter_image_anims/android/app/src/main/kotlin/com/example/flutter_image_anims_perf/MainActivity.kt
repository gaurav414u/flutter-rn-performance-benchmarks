package com.example.flutter_image_anims_perf

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    val CHANNEL: String = "com.example.flutter_image_anims_perf/reportFullyDrawn"

    override fun onFlutterUiDisplayed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                if (call.method == "reportFullyDrawn") {
                    reportFullyDrawn()
                    result.success(null)
                } else {
                    result.notImplemented()
                }
            }
    }
}
