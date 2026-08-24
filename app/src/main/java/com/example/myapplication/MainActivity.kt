package com.weathermixer.sixq

import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val startupStarted = SystemClock.elapsedRealtime()
        super.onCreate(savedInstanceState)
        Process.setThreadPriority(Process.THREAD_PRIORITY_DISPLAY)
        enableEdgeToEdge()
        preferHighestRefreshRate()
        setContent {
            var themeMode by remember { mutableStateOf(ThemeModeStore.load(this)) }
            WeatherFusionTheme(themeMode = themeMode) {
                WeatherAdvisorApp(
                    themeMode = themeMode,
                    onThemeModeChanged = { mode ->
                        themeMode = mode
                        ThemeModeStore.save(this, mode)
                    },
                )
            }
        }
        val observer = window.decorView.viewTreeObserver
        observer.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (observer.isAlive) observer.removeOnPreDrawListener(this)
                if (BuildConfig.DEBUG) {
                    Log.i("WeatherStartup", "first frame ready: ${SystemClock.elapsedRealtime() - startupStarted} ms")
                }
                window.decorView.post { reportFullyDrawn() }
                return true
            }
        })
    }

    private fun preferHighestRefreshRate() {
        window.decorView.post {
            @Suppress("DEPRECATION")
            val activeDisplay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display
            } else {
                windowManager.defaultDisplay
            }
            val currentMode = activeDisplay?.mode
            val compatibleModes = activeDisplay?.supportedModes.orEmpty().filter { mode ->
                currentMode == null || (
                    mode.physicalWidth == currentMode.physicalWidth &&
                        mode.physicalHeight == currentMode.physicalHeight
                    )
            }
            compatibleModes.maxByOrNull { it.refreshRate }?.let { bestMode ->
                window.attributes = window.attributes.apply {
                    preferredDisplayModeId = bestMode.modeId
                    preferredRefreshRate = bestMode.refreshRate
                }
            }
        }
    }
}
