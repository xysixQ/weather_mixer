package com.weathermixer.sixq

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.graphics.Paint as AndroidPaint
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedImageDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.os.Looper
import android.os.VibrationEffect
import android.os.VibrationAttributes
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.ImageView
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.PredictiveBackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable as ComposeAnimatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.coroutines.resume

internal enum class AppPage {
    Dashboard,
    Tools,
    Settings,
    Profile,
    Sources,
    BlockOrder,
    About,
    Language,
    WeatherTest,
}

internal val AppPage.navigationDepth: Int
    get() = when (this) {
        AppPage.Dashboard -> 0
        AppPage.Tools, AppPage.Settings, AppPage.WeatherTest -> 1
        AppPage.Profile, AppPage.Sources, AppPage.BlockOrder, AppPage.About, AppPage.Language -> 2
    }

internal enum class TemperatureUnit(val symbol: String) {
    Celsius("℃"),
    Fahrenheit("℉"),
    Kelvin("°K");

    fun convert(celsius: Double): Double = when (this) {
        Celsius -> celsius
        Fahrenheit -> celsius * 9.0 / 5.0 + 32.0
        Kelvin -> celsius + 273.15
    }

    fun format(celsius: Double, decimal: Boolean = false): String {
        val value = convert(celsius)
        val number = if (decimal) oneDecimal(value) else value.roundToInt().toString()
        return "$number$symbol"
    }
}

internal val LocalTemperatureUnit = staticCompositionLocalOf { TemperatureUnit.Celsius }
internal val LocalWeatherDataReady = staticCompositionLocalOf { true }
internal val LocalHapticFeedbackEnabled = staticCompositionLocalOf { false }
internal val LocalSuppressEntryMotion = staticCompositionLocalOf { false }

internal enum class AppVibration {
    StrongImpact,
    ReorderBuzz,
    RainPourPulse,
}

internal fun Context.performAppVibration(
    pattern: AppVibration,
    repetitionCount: Int = 1,
    view: View? = null,
) {
    val platformFeedback = when (pattern) {
        AppVibration.StrongImpact -> HapticFeedbackConstants.LONG_PRESS
        AppVibration.ReorderBuzz -> HapticFeedbackConstants.CLOCK_TICK
        AppVibration.RainPourPulse -> null
    }
    if (platformFeedback != null && view?.performHapticFeedback(platformFeedback) == true) return

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    } ?: return
    if (!vibrator.hasVibrator()) return

    runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val repetitions = repetitionCount.coerceIn(1, 7)
            val effect = when (pattern) {
                AppVibration.StrongImpact -> VibrationEffect.createOneShot(54L, 185)
                AppVibration.RainPourPulse -> VibrationEffect.createOneShot(14L, 32)
                AppVibration.ReorderBuzz -> {
                    val timings = mutableListOf(0L)
                    val amplitudes = mutableListOf(0)
                    repeat(repetitions) { index ->
                        timings += listOf(8L, 6L, 10L)
                        amplitudes += listOf(52, 72, 48)
                        if (index < repetitions - 1) {
                            timings += 10L
                            amplitudes += 0
                        }
                    }
                    VibrationEffect.createWaveform(
                        timings.toLongArray(),
                        amplitudes.toIntArray(),
                        -1,
                    )
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                vibrator.vibrate(
                    effect,
                    VibrationAttributes.createForUsage(VibrationAttributes.USAGE_TOUCH),
                )
            } else {
                vibrator.vibrate(effect)
            }
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(
                when (pattern) {
                    AppVibration.StrongImpact -> 54L
                    AppVibration.ReorderBuzz -> 24L * repetitionCount.coerceIn(1, 7) +
                        10L * (repetitionCount.coerceIn(1, 7) - 1)
                    AppVibration.RainPourPulse -> 10L
                }
            )
        }
    }
}

internal suspend fun Context.performReorderMotionTicks(view: View, durationMillis: Int = 240) {
    val frameMillis = 10L
    var nextDisplacementTick = 0.04f
    var previousProgress = 0f
    var elapsed = 0L
    while (elapsed <= durationMillis) {
        val timeFraction = (elapsed / durationMillis.toFloat()).coerceIn(0f, 1f)
        val progress = FastOutSlowInEasing.transform(timeFraction)
        val frameSpeed = ((progress - previousProgress) / (frameMillis / 1000f)).coerceAtLeast(0f)
        while (progress >= nextDisplacementTick) {
            performScaleTick(
                view = view,
                strength = 0.16f + (frameSpeed / 5.2f).coerceIn(0f, 1f) * 0.2f,
            )
            nextDisplacementTick += 0.04f
        }
        previousProgress = progress
        delay(frameMillis)
        elapsed += frameMillis
    }
}

internal fun Context.performScaleTick(view: View, strength: Float) {
    val normalizedStrength = strength.coerceIn(0.16f, 0.52f)
    val isColorOs = Build.MANUFACTURER.contains("oppo", ignoreCase = true) ||
        Build.MANUFACTURER.contains("oneplus", ignoreCase = true) ||
        Build.MANUFACTURER.contains("realme", ignoreCase = true) ||
        Build.BRAND.contains("oppo", ignoreCase = true) ||
        Build.BRAND.contains("oneplus", ignoreCase = true) ||
        Build.BRAND.contains("realme", ignoreCase = true)

    // ColorOS maps its system scale ticks to O-Haptics. Keep this path on the
    // public View contract so an OS update cannot break the app through a hidden API.
    if (isColorOs) {
        val feedback = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            HapticFeedbackConstants.SEGMENT_FREQUENT_TICK
        } else {
            HapticFeedbackConstants.CLOCK_TICK
        }
        if (view.performHapticFeedback(feedback)) return
    }

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    } ?: return
    if (!vibrator.hasVibrator()) return

    runCatching {
        val effect = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                vibrator.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_LOW_TICK) -> {
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, normalizedStrength)
                    .compose()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_TICK) -> {
                VibrationEffect.startComposition()
                    .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, normalizedStrength)
                    .compose()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                VibrationEffect.createOneShot(7L, (46 + normalizedStrength * 62f).roundToInt())
            else -> null
        }
        if (effect != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                vibrator.vibrate(
                    effect,
                    VibrationAttributes.createForUsage(VibrationAttributes.USAGE_TOUCH),
                )
            } else {
                vibrator.vibrate(effect)
            }
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(7L)
        }
    }
}

internal fun Context.performSoftResistancePulse() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    } ?: return
    if (!vibrator.hasVibrator()) return
    runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createWaveform(
                longArrayOf(0L, 16L, 5L, 14L),
                intArrayOf(0, 34, 0, 30),
                -1,
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                vibrator.vibrate(effect, VibrationAttributes.createForUsage(VibrationAttributes.USAGE_TOUCH))
            } else {
                vibrator.vibrate(effect)
            }
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(22L)
        }
    }
}

internal enum class DashboardBlock(val label: String) {
    Advice("个性化建议"),
    Daily("每日预报"),
    Hourly("逐小时预报"),
    Precipitation("降雨"),
    Wind("风况"),
    AirQuality("空气质量"),
    Ultraviolet("紫外线"),
    Humidity("湿度"),
    Pressure("气压"),
    @Deprecated("Only retained for dashboard-order migration")
    Metrics("天气指标"),
    Astronomy("日月升落"),
    Sources("天气源状态"),
    Fusion("多源融合分析"),
}

internal val DashboardBlock.metricDetail: DashboardDetail?
    get() = when (this) {
        DashboardBlock.Precipitation -> DashboardDetail.Precipitation
        DashboardBlock.Wind -> DashboardDetail.Wind
        DashboardBlock.AirQuality -> DashboardDetail.AirQuality
        DashboardBlock.Ultraviolet -> DashboardDetail.Ultraviolet
        DashboardBlock.Humidity -> DashboardDetail.Humidity
        DashboardBlock.Pressure -> DashboardDetail.Pressure
        else -> null
    }

internal val DashboardBlock.isMetric: Boolean
    get() = metricDetail != null

internal fun DashboardDetail.toMetricBlock(): DashboardBlock? = when (this) {
    DashboardDetail.Precipitation -> DashboardBlock.Precipitation
    DashboardDetail.Wind -> DashboardBlock.Wind
    DashboardDetail.AirQuality -> DashboardBlock.AirQuality
    DashboardDetail.Ultraviolet -> DashboardBlock.Ultraviolet
    DashboardDetail.Humidity -> DashboardBlock.Humidity
    DashboardDetail.Pressure -> DashboardBlock.Pressure
    else -> null
}

internal fun moveDashboardBlock(
    source: List<DashboardBlock>,
    from: Int,
    to: Int,
): List<DashboardBlock> {
    if (from !in source.indices || to !in source.indices || from == to) return source
    return source.toMutableList().apply {
        add(to, removeAt(from))
    }
}

internal enum class DashboardDetail(val label: String) {
    Alert("天气提醒"),
    Advice("个性化建议"),
    Daily("每日预报"),
    Hourly("逐小时预报"),
    Precipitation("降雨"),
    Wind("风况"),
    AirQuality("空气质量"),
    Ultraviolet("紫外线"),
    Humidity("湿度"),
    Pressure("气压"),
    Astronomy("日月升落"),
    Sources("天气源状态"),
    Fusion("多源融合分析"),
}

internal val DefaultMetricOrder = listOf(
    DashboardDetail.Precipitation,
    DashboardDetail.Wind,
    DashboardDetail.AirQuality,
    DashboardDetail.Ultraviolet,
    DashboardDetail.Humidity,
    DashboardDetail.Pressure,
)

internal val DashboardDetail.sourceContainerAlpha: Float
    get() = when (this) {
        DashboardDetail.Alert -> 0.9f
        DashboardDetail.AirQuality -> 0.92f
        DashboardDetail.Ultraviolet -> 0.88f
        DashboardDetail.Precipitation, DashboardDetail.Pressure -> 0.86f
        DashboardDetail.Wind, DashboardDetail.Humidity -> 0.88f
        else -> 0.82f
    }

internal fun weatherSceneForegroundColor(
    condition: WeatherCondition,
    alertLevel: AlertLevel,
    isNight: Boolean,
): Color {
    if (isNight) return Color(0xFFF3F7FF)
    val sceneCondition = if (alertLevel == AlertLevel.Severe) WeatherCondition.Storm else condition
    return when (sceneCondition.visualFamily) {
        WeatherVisualFamily.Clear, WeatherVisualFamily.Cloud, WeatherVisualFamily.Atmosphere -> Color(0xFF102A3A)
        WeatherVisualFamily.Dust -> Color(0xFF2E261B)
        WeatherVisualFamily.Rain, WeatherVisualFamily.Thunder, WeatherVisualFamily.Snow -> Color.White
    }
}

@Composable
internal fun rememberIsOnline(): Boolean {
    val context = LocalContext.current
    val connectivityManager = remember(context) {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    fun currentState(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ||
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        val captivePortal = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)
        return hasInternet && !captivePortal
    }
    var isOnline by remember(connectivityManager) { mutableStateOf(currentState()) }
    DisposableEffect(connectivityManager) {
        val mainExecutor = ContextCompat.getMainExecutor(context)
        val callback = object : ConnectivityManager.NetworkCallback() {
            private fun update() {
                mainExecutor.execute { isOnline = currentState() }
            }

            override fun onAvailable(network: Network) = update()
            override fun onLost(network: Network) = update()
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) = update()
        }
        runCatching { connectivityManager.registerDefaultNetworkCallback(callback) }
        onDispose { runCatching { connectivityManager.unregisterNetworkCallback(callback) } }
    }
    return isOnline
}

internal fun apiFailureStatusForRegion(status: String?, region: District): String? {
    if (status.isNullOrBlank() || region.isDomestic) return status
    val domesticOnlyNames = listOf("彩云天气", "\u5c0f\u7c73\u5929\u6c14", "高德天气", "中国环境监测总站", "心知天气")
    return status
        .split(" · ")
        .filterNot { entry -> domesticOnlyNames.any(entry::startsWith) }
        .joinToString(" · ")
        .takeIf(String::isNotBlank)
}

internal fun shouldAutoDisableApiSource(config: WeatherApiConfig): Boolean =
    config.requiresKey || !config.hasBuiltInDefault

internal fun isBaiduIpLocationReady(configs: List<WeatherApiConfig>): Boolean =
    configs.firstOrNull { it.sourceId == SourceId.BaiduIpLocation }?.isReady == true

@Composable
internal fun DetailCardAnchor(
    detail: DashboardDetail,
    onOpenDetail: (DashboardDetail, Rect) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (onClick: () -> Unit) -> Unit,
) {
    var bounds by remember(detail) { mutableStateOf(Rect.Zero) }
    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            bounds = coordinates.boundsInRoot()
        },
    ) {
        content {
            if (bounds.width > 0f && bounds.height > 0f) onOpenDetail(detail, bounds)
        }
    }
}

@Composable
internal fun WeatherAdvisorApp(
    themeMode: ThemeMode = ThemeMode.System,
    onThemeModeChanged: (ThemeMode) -> Unit = {},
    initialPage: AppPage = AppPage.Dashboard,
) {
    val context = LocalContext.current
    val repository = remember { WeatherRepository() }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val restoredRegion = remember { RegionStore.load(context) }
    var profile by remember { mutableStateOf(ProfileStore.load(context)) }
    var apiConfigs by remember { mutableStateOf(ApiConfigStore.load(context)) }
    var languageOverrides by remember { mutableStateOf(LanguageOverrideStore.load(context)) }
    var locationMethod by remember { mutableStateOf(LocationMethodStore.load(context)) }
    var reverseThemeSwipe by remember { mutableStateOf(ThemeSwipeDirectionStore.load(context)) }
    var selectedRegion by remember { mutableStateOf(restoredRegion ?: repository.defaultRegion) }
    var initialLocationPending by remember { mutableStateOf(restoredRegion == null) }
    var savedRegions by remember {
        mutableStateOf(
            SavedRegionsStore.load(context).ifEmpty {
                restoredRegion?.let(::listOf).orEmpty()
            }
        )
    }
    var locationMessage by remember { mutableStateOf<String?>(null) }
    var disabledApiStatus by remember { mutableStateOf(ApiFailureStatusStore.load(context)) }
    var citySearchResults by remember { mutableStateOf(emptyList<District>()) }
    var citySearchMessage by remember { mutableStateOf<String?>(null) }
    var isCitySearching by remember { mutableStateOf(false) }
    var citySearchRequestId by remember { mutableStateOf(0) }
    val weatherStore = remember {
        WeatherStore(
            WeatherContract.UiState(
                currentPage = initialPage,
                isRefreshing = false,
                backgroundMotionEnabled = true,
                dashboardOrder = DashboardOrderStore.load(context),
                metricOrder = MetricOrderStore.load(context),
                temperatureUnit = TemperatureUnitStore.load(context),
                reverseTemperatureSwipe = TemperatureSwipeDirectionStore.load(context),
                hapticFeedbackEnabled = HapticFeedbackStore.load(context),
            )
        )
    }
    val uiState = weatherStore.state
    val currentPage = uiState.currentPage
    val isRefreshing = uiState.isRefreshing
    val backgroundMotionEnabled = uiState.backgroundMotionEnabled
    val dashboardOrder = uiState.dashboardOrder
    val metricOrder = uiState.metricOrder
    val temperatureUnit = uiState.temperatureUnit
    val reverseTemperatureSwipe = uiState.reverseTemperatureSwipe
    val hapticFeedbackEnabled = uiState.hapticFeedbackEnabled
    var appBackProgress by remember { mutableFloatStateOf(0f) }
    var appBackDirection by remember { mutableFloatStateOf(1f) }
    var appBackExitProgress by remember { mutableFloatStateOf(0f) }
    var appBackCommitting by remember { mutableStateOf(false) }
    var suppressDashboardEntryMotion by remember { mutableStateOf(false) }
    var dashboardRevealAllowed by remember { mutableStateOf(true) }
    var themeRevealOldBackground by remember { mutableStateOf(Color.Transparent) }
    var themeRevealActive by remember { mutableStateOf(false) }
    var themeRevealProgress by remember { mutableFloatStateOf(1f) }
    var themeRevealRunId by remember { mutableIntStateOf(0) }
    var refreshRequest by remember { mutableStateOf(0) }
    var previousHapticPage by remember { mutableStateOf(currentPage) }
    var startupLocationRequest by remember { mutableIntStateOf(0) }
    val startupPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            locationMethod = LocationMethod.Device
            LocationMethodStore.save(context, LocationMethod.Device)
            startupLocationRequest += 1
        } else {
            initialLocationPending = false
            locationMessage = "请授权系统定位，或搜索城市选择。"
        }
    }
    var snapshot by remember {
        mutableStateOf(repository.buildSnapshot(selectedRegion, profile))
    }
    var initialWeatherDataReady by remember { mutableStateOf(false) }
    val weatherCache = remember { mutableMapOf<String, WeatherSnapshotCacheEntry>() }
    var handledRefreshRequest by remember { mutableStateOf(refreshRequest) }
    var adviceProfile by remember { mutableStateOf(profile) }
    val latestProfile by rememberUpdatedState(profile)
    val activeProfile = profile ?: DefaultUserProfile
    var sceneTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var weatherTestEnabled by remember { mutableStateOf(false) }
    var weatherTestOriginalSnapshot by remember { mutableStateOf<WeatherSnapshot?>(null) }
    var weatherTestCondition by remember { mutableStateOf(snapshot.fused.condition) }
    var weatherTestAlertPreset by remember { mutableStateOf(WeatherTestAlertPreset.None) }
    var weatherTestCode by remember { mutableStateOf("") }
    var weatherTestClock by remember { mutableStateOf("12:00") }
    var weatherTestTimeZone by remember { mutableStateOf(TimeZone.getDefault().id) }
    var skipRefreshAfterWeatherTest by remember { mutableStateOf(false) }
    val weatherTestTimeMillis = if (weatherTestEnabled) {
        resolveWeatherTestTimeMillis(weatherTestClock, weatherTestTimeZone, sceneTimeMillis)
    } else {
        null
    }
    val weatherTestIsNight = if (weatherTestEnabled) weatherTestNightForClock(weatherTestClock) else null
    val latestLanguageOverrides by rememberUpdatedState(languageOverrides)

    LaunchedEffect(weatherStore) {
        weatherStore.effects.collect { effect ->
            when (effect) {
                is WeatherContract.Effect.Snackbar -> {
                    snackbarHostState.showSnackbar(localizeUiString(effect.message, latestLanguageOverrides))
                }
            }
        }
    }

    LaunchedEffect(snapshot.region.storageKey, snapshot.astronomy) {
        while (true) {
            sceneTimeMillis = System.currentTimeMillis()
            delay(60_000L)
        }
    }


    LaunchedEffect(currentPage) {
        if (currentPage != previousHapticPage && hapticFeedbackEnabled) {
            context.performAppVibration(AppVibration.StrongImpact)
        }
        previousHapticPage = currentPage
    }

    val systemDarkTheme = isSystemInDarkTheme()
    val currentThemeBackground = MaterialTheme.colorScheme.background
    SideEffect {
        if (!themeRevealActive && themeRevealProgress >= 1f) {
            themeRevealOldBackground = currentThemeBackground
        }
    }
    LaunchedEffect(themeRevealRunId) {
        if (themeRevealRunId > 0 && themeRevealActive) {
            val animation = ComposeAnimatable(themeRevealProgress)
            animation.animateTo(
                targetValue = 1f,
                animationSpec = tween(620, easing = FastOutSlowInEasing),
            ) { themeRevealProgress = value }
            themeRevealProgress = 1f
            themeRevealActive = false
        }
    }

    fun changeThemeModeWithReveal(mode: ThemeMode) {
        val currentDarkTheme = themeMode.resolvesToDarkTheme(systemDarkTheme)
        val nextDarkTheme = mode.resolvesToDarkTheme(systemDarkTheme)
        if (mode != themeMode && currentDarkTheme != nextDarkTheme) {
            themeRevealOldBackground = currentThemeBackground
            themeRevealProgress = 0f
            themeRevealActive = true
            themeRevealRunId += 1
        }
        onThemeModeChanged(mode)
    }
    fun selectAndPersistRegion(region: District) {
        selectedRegion = region
        RegionStore.save(context, region)
        savedRegions = (listOf(region) + savedRegions)
            .distinctBy(District::storageKey)
            .take(8)
        SavedRegionsStore.save(context, savedRegions)
        if (region.countryCode == "US") {
            val updated = apiConfigs.map { config ->
                if (config.sourceId == SourceId.Nws) config.copy(enabled = true) else config
            }
            if (updated != apiConfigs) {
                apiConfigs = updated
                ApiConfigStore.save(context, updated)
            }
        }
    }

    LaunchedEffect(restoredRegion, startupLocationRequest) {
        if (restoredRegion != null) return@LaunchedEffect
        if (!hasLocationPermission(context)) {
            startupPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
            locationMessage = "请授权系统定位，或搜索城市选择。"
            initialLocationPending = false
            return@LaunchedEffect
        }
        locationMethod = LocationMethod.Device
        LocationMethodStore.save(context, LocationMethod.Device)
        locationMessage = "正在通过系统定位识别城市..."
        val result = repository.locateCurrentPosition(
            context = context,
            apiConfigs = apiConfigs,
            method = LocationMethod.Device,
        )
        val resolvedRegion = result.position?.region
        if (resolvedRegion != null) {
            selectAndPersistRegion(resolvedRegion)
            citySearchResults = listOf(resolvedRegion)
            citySearchMessage = "系统定位识别到 ${resolvedRegion.displayName}。"
            locationMessage = "系统定位已由小米识别为 ${resolvedRegion.displayName}。"
        } else {
            locationMessage = result.failureMessage.ifBlank { "系统定位失败，请搜索并选择城市。" }
        }
        initialLocationPending = false
    }
    fun parentPage(page: AppPage): AppPage = when (page) {
            AppPage.Profile -> AppPage.Settings
            AppPage.Sources -> AppPage.Settings
            AppPage.BlockOrder -> AppPage.Settings
            AppPage.About -> AppPage.Settings
            AppPage.Language -> AppPage.Settings
            AppPage.WeatherTest -> AppPage.Dashboard
            AppPage.Tools -> AppPage.Dashboard
            AppPage.Settings -> AppPage.Dashboard
            AppPage.Dashboard -> AppPage.Dashboard
    }

    fun showDashboardWithoutEntryMotion(revealDelayMillis: Long) {
        suppressDashboardEntryMotion = true
        dashboardRevealAllowed = false
        weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Dashboard))
        scope.launch {
            if (revealDelayMillis > 0L) delay(revealDelayMillis)
            dashboardRevealAllowed = true
        }
    }

    fun applyWeatherTestCondition(condition: WeatherCondition) {
        weatherTestCondition = condition
        if (weatherTestEnabled) {
            snapshot = snapshot.copy(
                fused = snapshot.fused.copy(
                    condition = condition,
                    alert = weatherTestAlertPreset.alert,
                )
            )
        }
    }
    fun applyWeatherTestAlertPreset(preset: WeatherTestAlertPreset) {
        weatherTestAlertPreset = preset
        if (weatherTestEnabled) {
            snapshot = snapshot.copy(
                fused = snapshot.fused.copy(alert = preset.alert)
            )
        }
    }

    fun setWeatherTestEnabled(enabled: Boolean) {
        if (enabled == weatherTestEnabled) return
        if (enabled) {
            weatherTestOriginalSnapshot = snapshot
            weatherTestCondition = snapshot.fused.condition
            weatherTestAlertPreset = WeatherTestAlertPreset.None
            skipRefreshAfterWeatherTest = false
            weatherTestEnabled = true
            applyWeatherTestCondition(weatherTestCondition)
        } else {
            weatherTestEnabled = false
            weatherTestOriginalSnapshot?.let { snapshot = it }
            weatherTestOriginalSnapshot = null
            skipRefreshAfterWeatherTest = true
            weatherStore.accept(WeatherContract.Intent.SetRefreshing(false))
        }
    }

    fun completePredictiveBack() {
        when (val destination = parentPage(currentPage)) {
            AppPage.Dashboard -> showDashboardWithoutEntryMotion(0L)
            else -> weatherStore.accept(WeatherContract.Intent.Navigate(destination))
        }
    }

    PredictiveBackHandler(enabled = currentPage != AppPage.Dashboard) { progress ->
        try {
            progress.collect { backEvent ->
                appBackProgress = backEvent.progress
                appBackDirection = if (backEvent.swipeEdge == BackEventCompat.EDGE_RIGHT) -1f else 1f
                suppressDashboardEntryMotion = true
            }
            appBackCommitting = true
            if (appBackDirection > 0f) {
                val exitAnimation = ComposeAnimatable(0f)
                exitAnimation.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(280, easing = CubicBezierEasing(0.16f, 0f, 0.12f, 1f)),
                ) { appBackExitProgress = value }
                completePredictiveBack()
            } else {
                val settleAnimation = ComposeAnimatable(appBackProgress)
                settleAnimation.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessMediumLow),
                ) { appBackProgress = value }
                appBackProgress = 0f
                appBackDirection = 1f
                val exitAnimation = ComposeAnimatable(0f)
                exitAnimation.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(300, easing = CubicBezierEasing(0.16f, 0f, 0.12f, 1f)),
                ) { appBackExitProgress = value }
                completePredictiveBack()
            }
            delay(24L)
        } catch (_: CancellationException) {
            withContext(NonCancellable) {
                val rollback = ComposeAnimatable(appBackProgress)
                rollback.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(dampingRatio = 0.9f, stiffness = Spring.StiffnessMediumLow),
                ) { appBackProgress = value }
            }
        } finally {
            appBackProgress = 0f
            appBackExitProgress = 0f
            appBackCommitting = false
        }
    }

    LaunchedEffect(selectedRegion.countryCode) {
        if (selectedRegion.countryCode == "US" && apiConfigs.any { it.sourceId == SourceId.Nws && !it.enabled }) {
            apiConfigs = apiConfigs.map { config ->
                if (config.sourceId == SourceId.Nws) config.copy(enabled = true) else config
            }
            ApiConfigStore.save(context, apiConfigs)
        }
    }

    LaunchedEffect(profile) {
        if (profile != adviceProfile) {
            adviceProfile = profile
            snapshot = repository.rebuildSnapshotForProfile(snapshot, profile)
            weatherCache.keys.toList().forEach { key ->
                weatherCache[key]?.let { entry ->
                    weatherCache[key] = entry.copy(
                        snapshot = repository.rebuildSnapshotForProfile(entry.snapshot, profile),
                    )
                }
            }
        }
    }

    LaunchedEffect(selectedRegion, apiConfigs, refreshRequest, initialLocationPending, weatherTestEnabled) {
        if (weatherTestEnabled) {
            weatherStore.accept(WeatherContract.Intent.SetRefreshing(false))
            return@LaunchedEffect
        }
        if (skipRefreshAfterWeatherTest) {
            skipRefreshAfterWeatherTest = false
            weatherStore.accept(WeatherContract.Intent.SetRefreshing(false))
            return@LaunchedEffect
        }
        if (initialLocationPending || selectedRegion == repository.defaultRegion) {
            weatherStore.accept(WeatherContract.Intent.SetRefreshing(initialLocationPending))
            return@LaunchedEffect
        }
        val forceRefresh = refreshRequest != handledRefreshRequest
        handledRefreshRequest = refreshRequest
        val now = System.currentTimeMillis()
        val configSignature = apiConfigs.hashCode()
        val memoryCached = weatherCache[selectedRegion.storageKey]
        val cacheReadStarted = SystemClock.elapsedRealtime()
        val (persistedCached, lastRequestAtMillis) = withContext(Dispatchers.IO) {
            WeatherRefreshStore.load(
                context = context,
                region = selectedRegion,
                profile = latestProfile,
                repository = repository,
                apiConfigSignature = configSignature,
            ) to WeatherRefreshStore.lastRequestAt(
                context = context,
                regionKey = selectedRegion.storageKey,
                apiConfigSignature = configSignature,
            )
        }
        if (BuildConfig.DEBUG) {
            Log.d("WeatherStartup", "cache decode: ${SystemClock.elapsedRealtime() - cacheReadStarted} ms")
        }
        val requestAgeMillis = now - lastRequestAtMillis
        val refreshIsThrottled = lastRequestAtMillis > 0L &&
            requestAgeMillis in 0 until MinimumWeatherRefreshIntervalMillis
        if (refreshIsThrottled) {
            if (forceRefresh) {
                weatherStore.accept(WeatherContract.Intent.ShowMessage("请稍后再试试吧"))
            }
            val cached = memoryCached ?: persistedCached
            if (cached != null) {
                snapshot = cached.snapshot
            }
            initialWeatherDataReady = true
            weatherStore.accept(WeatherContract.Intent.SetRefreshing(false))
            return@LaunchedEffect
        }
        val automaticRefreshCache = listOfNotNull(memoryCached, persistedCached)
            .filter { it.apiConfigSignature == configSignature }
            .maxByOrNull { it.cachedAtMillis }
        if (
            !forceRefresh &&
            automaticRefreshCache != null &&
            now - automaticRefreshCache.cachedAtMillis < AutomaticWeatherRefreshIntervalMillis
        ) {
            snapshot = automaticRefreshCache.snapshot
            initialWeatherDataReady = true
            weatherStore.accept(WeatherContract.Intent.SetRefreshing(false))
            return@LaunchedEffect
        }
        weatherStore.accept(WeatherContract.Intent.SetRefreshing(true))
        withContext(Dispatchers.IO) {
            WeatherRefreshStore.markRequest(
                context = context,
                regionKey = selectedRegion.storageKey,
                apiConfigSignature = configSignature,
                requestedAtMillis = now,
            )
        }
        try {
            var detectedFailures = emptyList<ApiSourceFailure>()
            val updatedSnapshot = repository.buildSnapshot(
                region = selectedRegion,
                profile = latestProfile,
                apiConfigs = apiConfigs,
                onXiaomiSnapshot = { xiaomiSnapshot ->
                    val xiaomiAdjustedSnapshot = repository.rebuildSnapshotForProfile(xiaomiSnapshot, latestProfile)
                    snapshot = xiaomiAdjustedSnapshot
                    initialWeatherDataReady = true
                    val xiaomiCacheEntry = WeatherSnapshotCacheEntry(
                        snapshot = xiaomiAdjustedSnapshot,
                        apiConfigSignature = configSignature,
                        cachedAtMillis = System.currentTimeMillis(),
                    )
                    weatherCache[selectedRegion.storageKey] = xiaomiCacheEntry
                    scope.launch(Dispatchers.IO) { WeatherRefreshStore.save(context, xiaomiCacheEntry) }
                },
                onSourceFailures = { detectedFailures = it },
            )
            val profileAdjustedSnapshot = repository.rebuildSnapshotForProfile(updatedSnapshot, latestProfile)
            snapshot = profileAdjustedSnapshot
            initialWeatherDataReady = true
            val cacheEntry = WeatherSnapshotCacheEntry(
                snapshot = profileAdjustedSnapshot,
                apiConfigSignature = configSignature,
                cachedAtMillis = System.currentTimeMillis(),
            )
            weatherCache[selectedRegion.storageKey] = cacheEntry
            withContext(Dispatchers.IO) { WeatherRefreshStore.save(context, cacheEntry) }
            if (detectedFailures.isNotEmpty()) {
                val configById = apiConfigs.associateBy(WeatherApiConfig::sourceId)
                val autoDisabledFailures = detectedFailures.filter { failure ->
                    configById[failure.sourceId]?.let(::shouldAutoDisableApiSource) == true
                }
                val autoDisabledIds = autoDisabledFailures.map(ApiSourceFailure::sourceId).toSet()
                val transientFailures = detectedFailures.filterNot { failure ->
                    failure.sourceId in autoDisabledIds
                }
                val statusParts = autoDisabledFailures.map { failure ->
                    "${failure.displayName}已停用（${failure.reason}）"
                } + transientFailures.map { failure ->
                    "${failure.displayName}暂不可用（${failure.reason}）"
                }
                disabledApiStatus = statusParts.joinToString(" · ").takeIf(String::isNotBlank)
                ApiFailureStatusStore.save(context, disabledApiStatus)
                if (autoDisabledFailures.isNotEmpty()) {
                    val failedIds = autoDisabledFailures.map(ApiSourceFailure::sourceId).toSet()
                    val updatedConfigs = apiConfigs.map { config ->
                        if (config.sourceId in failedIds) config.copy(enabled = false) else config
                    }
                    ApiConfigStore.save(context, updatedConfigs)
                    apiConfigs = updatedConfigs
                }
            } else if (disabledApiStatus != null) {
                disabledApiStatus = null
                ApiFailureStatusStore.clear(context)
            }
        } finally {
            weatherStore.accept(WeatherContract.Intent.SetRefreshing(false))
        }
    }

    @Composable
    fun DashboardPage(modifier: Modifier = Modifier) {
        CompositionLocalProvider(
            LocalSuppressEntryMotion provides suppressDashboardEntryMotion,
        ) {
            DashboardScreen(
                modifier = modifier,
                profile = activeProfile,
                selectedRegion = selectedRegion,
                snapshot = snapshot,
                isRefreshing = isRefreshing,
                testTimeMillis = weatherTestTimeMillis,
                testIsNight = weatherTestIsNight,
                disabledApiStatus = disabledApiStatus,
                blockOrder = dashboardOrder,
                metricOrder = metricOrder,
                onBlockOrderChanged = { updated ->
                    weatherStore.accept(WeatherContract.Intent.SetDashboardOrder(updated))
                    DashboardOrderStore.save(context, updated)
                },
                onMetricOrderChanged = { updated ->
                    weatherStore.accept(WeatherContract.Intent.SetMetricOrder(updated))
                    MetricOrderStore.save(context, updated)
                },
                locationMethod = locationMethod,
                locationMessage = locationMessage,
                citySearchResults = citySearchResults,
                savedRegions = savedRegions,
                citySearchMessage = citySearchMessage,
                isCitySearching = isCitySearching,
                onRegionSelected = ::selectAndPersistRegion,
                onSavedRegionRemoved = { region ->
                    if (region.storageKey != selectedRegion.storageKey) {
                        savedRegions = savedRegions.filterNot { it.storageKey == region.storageKey }
                        SavedRegionsStore.save(context, savedRegions)
                    }
                },
                onOpenSettings = {
                    suppressDashboardEntryMotion = false
                    weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Settings))
                },
                onOpenWeatherTest = {
                    if (BuildConfig.DEBUG) {
                        suppressDashboardEntryMotion = false
                        weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.WeatherTest))
                    }
                },
                onOpenTools = {
                    suppressDashboardEntryMotion = false
                    weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Tools))
                },
                onRefresh = {
                    if (weatherTestEnabled) {
                        weatherStore.accept(WeatherContract.Intent.ShowMessage("测试模式中不会刷新天气数据。"))
                    } else {
                        refreshRequest += 1
                    }
                },
                onBackgroundMotionChange = {
                    weatherStore.accept(WeatherContract.Intent.SetBackgroundMotion(it))
                },
                onCitySearch = { query ->
                    val keyword = query.trim()
                    val requestId = citySearchRequestId + 1
                    citySearchRequestId = requestId
                    if (keyword.isBlank()) {
                        citySearchResults = emptyList()
                        citySearchMessage = null
                        isCitySearching = false
                    } else {
                        scope.launch {
                            isCitySearching = true
                            citySearchMessage = "正在搜索城市..."
                            val result = repository.searchCities(keyword, apiConfigs)
                            if (citySearchRequestId == requestId) {
                                citySearchResults = result.regions
                                citySearchMessage = result.message
                                isCitySearching = false
                            }
                        }
                    }
                },
                onLocationMethodChanged = { method ->
                    locationMethod = method
                    LocationMethodStore.save(context, method)
                },
                onUseCurrentLocation = {
                    scope.launch {
                        locationMessage = "正在请求当前位置..."
                        val result = repository.locateCurrentPosition(
                            context = context,
                            apiConfigs = apiConfigs,
                            method = locationMethod,
                        )
                        val position = result.position
                        if (position == null) {
                            locationMessage = result.failureMessage
                            weatherStore.accept(
                                WeatherContract.Intent.ShowMessage("未能读取当前位置，请检查定位开关，或搜索城市选择。")
                            )
                        } else {
                            val resolvedRegion = position.region
                            if (resolvedRegion == null) {
                                locationMessage = result.failureMessage.ifBlank {
                                    "${position.providerLabel} 已定位 ${position.coordinateText}，但小米坐标反查没有返回城市。"
                                }
                                weatherStore.accept(
                                    WeatherContract.Intent.ShowMessage("已获取坐标，但小米没有返回城市。")
                                )
                            } else {
                                selectAndPersistRegion(resolvedRegion)
                                citySearchResults = listOf(resolvedRegion)
                                citySearchMessage = "定位识别到 ${resolvedRegion.displayName}。"
                                val keyStatus = if (resolvedRegion.locationKey.isBlank()) "" else "，城市 key 已就绪"
                                locationMessage = "${position.providerLabel} 已定位 ${position.coordinateText}，小米识别为 ${resolvedRegion.displayName}$keyStatus。"
                                weatherStore.accept(
                                    WeatherContract.Intent.ShowMessage("已切换到 ${resolvedRegion.shortName}")
                                )
                            }
                        }
                    }
                },
                onPermissionDenied = {
                    weatherStore.accept(
                        WeatherContract.Intent.ShowMessage("需要定位权限后才能读取当前位置。")
                    )
                },
            )
        }
    }

    CompositionLocalProvider(
        LocalTemperatureUnit provides temperatureUnit,
        LocalWeatherDataReady provides initialWeatherDataReady,
        LocalHapticFeedbackEnabled provides hapticFeedbackEnabled,
        LocalUiStringOverrides provides languageOverrides,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
            WeatherSceneBackground(
                condition = snapshot.fused.condition,
                alertLevel = snapshot.fused.alert.level,
                isNight = weatherTestIsNight ?: isNightTime(
                    weatherTestTimeMillis ?: sceneTimeMillis,
                    snapshot.astronomy,
                    snapshot.region.longitude,
                ),
                animationEnabled = currentPage == AppPage.Dashboard && backgroundMotionEnabled,
                modifier = Modifier.fillMaxSize(),
            )
            val dashboardLayerAlpha by animateFloatAsState(
                targetValue = if (
                    currentPage == AppPage.Dashboard &&
                    dashboardRevealAllowed &&
                    !appBackCommitting
                ) 1f else 0f,
                animationSpec = tween(if (currentPage == AppPage.Dashboard) 150 else 90),
                label = "persistentDashboardLayer",
            )
            DashboardPage(
                modifier = Modifier.graphicsLayer {
                    alpha = dashboardLayerAlpha
                },
            )
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        val gestureOffset = appBackProgress * 0.16f
                        val exitOffset = gestureOffset + (1.04f - gestureOffset) * appBackExitProgress
                        translationX = size.width * exitOffset * appBackDirection
                        scaleX = 1f - appBackProgress * 0.022f
                        scaleY = 1f - appBackProgress * 0.022f
                        alpha = (1f - appBackProgress * 0.08f) * (1f - appBackExitProgress)
                        transformOrigin = TransformOrigin.Center
                    },
                targetState = currentPage,
                transitionSpec = {
                    if (appBackCommitting) {
                        EnterTransition.None togetherWith ExitTransition.None
                    } else if (targetState.navigationDepth < initialState.navigationDepth) {
                        (fadeIn(tween(260)) + slideInHorizontally { -it / 4 }) togetherWith
                            (fadeOut(tween(180)) + slideOutHorizontally { it })
                    } else {
                        (fadeIn(tween(280)) + slideInHorizontally { it }) togetherWith
                            (fadeOut(tween(180)) + slideOutHorizontally { -it / 4 })
                    }
                },
                label = "appContent",
            ) { page ->
                when (page) {
                    AppPage.Profile -> ProfileSettingsScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f)).statusBarsPadding().navigationBarsPadding(),
                        profile = profile,
                        onBack = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Settings)) },
                        onSave = { completedProfile ->
                            ProfileStore.save(context, completedProfile)
                            profile = completedProfile
                            weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Settings))
                            weatherStore.accept(WeatherContract.Intent.ShowMessage("用户画像已保存。"))
                        },
                    )

                    AppPage.Settings -> SettingsHomeScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f)).statusBarsPadding().navigationBarsPadding(),
                        themeMode = themeMode,
                        reverseThemeSwipe = reverseThemeSwipe,
                        temperatureUnit = temperatureUnit,
                        reverseTemperatureSwipe = reverseTemperatureSwipe,
                        hapticFeedbackEnabled = hapticFeedbackEnabled,
                        onBack = { showDashboardWithoutEntryMotion(190L) },
                        onThemeModeChanged = ::changeThemeModeWithReveal,
                        onReverseThemeSwipeChanged = { reversed ->
                            reverseThemeSwipe = reversed
                            ThemeSwipeDirectionStore.save(context, reversed)
                        },
                        onTemperatureUnitChanged = { unit ->
                            weatherStore.accept(WeatherContract.Intent.SetTemperatureUnit(unit))
                            TemperatureUnitStore.save(context, unit)
                        },
                        onReverseTemperatureSwipeChanged = { reversed ->
                            weatherStore.accept(WeatherContract.Intent.SetReverseTemperatureSwipe(reversed))
                            TemperatureSwipeDirectionStore.save(context, reversed)
                        },
                        onHapticFeedbackChanged = { enabled ->
                            if (hapticFeedbackEnabled || enabled) {
                                context.performAppVibration(AppVibration.StrongImpact)
                            }
                            weatherStore.accept(WeatherContract.Intent.SetHapticFeedback(enabled))
                            HapticFeedbackStore.save(context, enabled)
                        },
                        onOpenProfile = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Profile)) },
                        onOpenSources = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Sources)) },
                        onOpenBlockOrder = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.BlockOrder)) },
                        onOpenAbout = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.About)) },
                        onOpenLanguage = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Language)) },
                    )

                    AppPage.Tools -> ToolsScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f)).statusBarsPadding().navigationBarsPadding(),
                        onBack = { showDashboardWithoutEntryMotion(190L) },
                    )

                    AppPage.Sources -> SourceSettingsScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f)).statusBarsPadding().navigationBarsPadding(),
                        configs = apiConfigs,
                        onBack = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Settings)) },
                        onSave = { updatedConfigs ->
                            apiConfigs = updatedConfigs
                            ApiConfigStore.save(context, updatedConfigs)
                            disabledApiStatus = null
                            ApiFailureStatusStore.clear(context)
                            weatherStore.accept(WeatherContract.Intent.ShowMessage("天气源配置已保存。"))
                        },
                        onReset = {
                            val defaults = ApiConfigDefaults.defaultConfigs()
                            apiConfigs = defaults
                            ApiConfigStore.save(context, defaults)
                            disabledApiStatus = null
                            ApiFailureStatusStore.clear(context)
                            weatherStore.accept(WeatherContract.Intent.ShowMessage("已恢复默认 API 配置。"))
                        },
                    )

                    AppPage.BlockOrder -> BlockOrderScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f)).statusBarsPadding().navigationBarsPadding(),
                        order = dashboardOrder,
                        onBack = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Settings)) },
                        onOrderChanged = { updated ->
                            weatherStore.accept(WeatherContract.Intent.SetDashboardOrder(updated))
                            DashboardOrderStore.save(context, updated)
                        },
                    )

                    AppPage.About -> AboutScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f)).statusBarsPadding().navigationBarsPadding(),
                        onBack = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Settings)) },
                    )

                    AppPage.Language -> LanguageSettingsScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.96f)).statusBarsPadding().navigationBarsPadding(),
                        activeOverrides = languageOverrides,
                        onBack = { weatherStore.accept(WeatherContract.Intent.Navigate(AppPage.Settings)) },
                        onSave = { overrides ->
                            val cleaned = LanguageOverrideStore.clean(overrides)
                            languageOverrides = cleaned
                            LanguageOverrideStore.save(context, cleaned)
                            weatherStore.accept(WeatherContract.Intent.ShowMessage("语言文字已保存。"))
                        },
                        onImportSaved = { imported ->
                            LanguageOverrideStore.save(context, imported)
                        },
                        onMessage = { message ->
                            weatherStore.accept(WeatherContract.Intent.ShowMessage(message))
                        },
                    )

                    AppPage.WeatherTest -> WeatherTestScreen(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.98f)).statusBarsPadding().navigationBarsPadding(),
                        enabled = weatherTestEnabled,
                        weatherCode = weatherTestCode,
                        selectedCondition = weatherTestCondition,
                        selectedAlertPreset = weatherTestAlertPreset,
                        clockText = weatherTestClock,
                        timeZoneId = weatherTestTimeZone,
                        onEnabledChange = ::setWeatherTestEnabled,
                        onWeatherCodeChange = { code ->
                            weatherTestCode = code
                            xiaomiWeatherCondition(code)?.let(::applyWeatherTestCondition)
                        },
                        onConditionSelected = { condition ->
                            weatherTestCode = ""
                            applyWeatherTestCondition(condition)
                        },
                        onAlertPresetSelected = ::applyWeatherTestAlertPreset,
                        onClockTextChange = { weatherTestClock = it },
                        onTimeZoneIdChange = { weatherTestTimeZone = it },
                        onBack = { showDashboardWithoutEntryMotion(0L) },
                    )

                    AppPage.Dashboard -> Box(Modifier.fillMaxSize())
                }
                }
                if (themeRevealActive || themeRevealProgress < 1f) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(40f)
                            .graphicsLayer {
                                compositingStrategy = CompositingStrategy.Offscreen
                            },
                    ) {
                        val radius = sqrt(size.width * size.width + size.height * size.height) *
                            themeRevealProgress
                        drawRect(themeRevealOldBackground)
                        drawCircle(
                            color = Color.Transparent,
                            radius = radius,
                            center = Offset(size.width, 0f),
                            blendMode = BlendMode.Clear,
                        )
                    }
                }
            }
        }
    }
}
