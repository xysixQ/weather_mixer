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

internal object TemperatureUnitStore {
    private const val PrefName = "display_preferences"
    private const val UnitKey = "temperature_unit"

    fun load(context: Context): TemperatureUnit = context
        .getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        .getString(UnitKey, null)
        ?.toEnumOrNull<TemperatureUnit>()
        ?: TemperatureUnit.Celsius

    fun save(context: Context, unit: TemperatureUnit) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(UnitKey, unit.name)
            .apply()
    }
}

internal object TemperatureSwipeDirectionStore {
    private const val PrefName = "display_preferences"
    private const val ReverseKey = "reverse_temperature_swipe"

    fun load(context: Context): Boolean = context
        .getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        .getBoolean(ReverseKey, false)

    fun save(context: Context, reversed: Boolean) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(ReverseKey, reversed)
            .apply()
    }
}

internal object ThemeSwipeDirectionStore {
    private const val PrefName = "display_preferences"
    private const val ReverseKey = "reverse_theme_swipe"

    fun load(context: Context): Boolean = context
        .getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        .getBoolean(ReverseKey, false)

    fun save(context: Context, reversed: Boolean) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(ReverseKey, reversed)
            .apply()
    }
}

internal object HapticFeedbackStore {
    private const val PrefName = "display_preferences"
    private const val EnabledKey = "haptic_feedback_enabled"

    fun load(context: Context): Boolean = context
        .getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        .getBoolean(EnabledKey, false)

    fun save(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(EnabledKey, enabled)
            .apply()
    }
}

internal object DashboardOrderStore {
    private const val PrefName = "dashboard_block_order"
    private const val OrderKey = "order"

    val DefaultOrder = listOf(
        DashboardBlock.Daily,
        DashboardBlock.Hourly,
        DashboardBlock.Advice,
        DashboardBlock.Precipitation,
        DashboardBlock.Wind,
        DashboardBlock.AirQuality,
        DashboardBlock.Ultraviolet,
        DashboardBlock.Humidity,
        DashboardBlock.Pressure,
        DashboardBlock.Astronomy,
        DashboardBlock.Sources,
        DashboardBlock.Fusion,
    )

    fun load(context: Context): List<DashboardBlock> {
        val stored = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .getString(OrderKey, null)
            ?.split(',')
            .orEmpty()
            .mapNotNull { it.toEnumOrNull<DashboardBlock>() }
            .distinct()
        val migratedMetrics = MetricOrderStore.load(context).mapNotNull { it.toMetricBlock() }
        val expanded = stored.flatMap { block ->
            @Suppress("DEPRECATION")
            if (block == DashboardBlock.Metrics) migratedMetrics else listOf(block)
        }
        val base = if (expanded.any { it.isMetric }) {
            expanded
        } else if (stored.isNotEmpty()) {
            val insertAfter = expanded.indexOf(DashboardBlock.Advice).takeIf { it >= 0 }
                ?: expanded.indexOf(DashboardBlock.Hourly).takeIf { it >= 0 }
                ?: expanded.lastIndex
            expanded.toMutableList().apply { addAll(insertAfter + 1, migratedMetrics) }
        } else {
            DefaultOrder
        }
        @Suppress("DEPRECATION")
        return (base + DefaultOrder).distinct().filterNot { it == DashboardBlock.Metrics }
    }

    fun save(context: Context, order: List<DashboardBlock>) {
        @Suppress("DEPRECATION")
        val normalized = order.distinct().filterNot { it == DashboardBlock.Metrics }
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(OrderKey, normalized.joinToString(",") { it.name })
            .apply()
    }
}

internal object ThemeModeStore {
    private const val PrefName = "display_preferences"
    private const val ThemeModeKey = "theme_mode"

    fun load(context: Context): ThemeMode = context
        .getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        .getString(ThemeModeKey, null)
        ?.toEnumOrNull<ThemeMode>()
        ?: ThemeMode.System

    fun save(context: Context, mode: ThemeMode) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(ThemeModeKey, mode.name)
            .apply()
    }
}

internal object MetricOrderStore {
    private const val PrefName = "dashboard_metric_order"
    private const val OrderKey = "order"

    fun load(context: Context): List<DashboardDetail> {
        val stored = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .getString(OrderKey, null)
            ?.split(',')
            .orEmpty()
            .mapNotNull { it.toEnumOrNull<DashboardDetail>() }
            .filter { it in DefaultMetricOrder }
            .distinct()
        return (stored + DefaultMetricOrder).distinct()
    }

    fun save(context: Context, order: List<DashboardDetail>) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(OrderKey, order.filter { it in DefaultMetricOrder }.joinToString(",") { it.name })
            .apply()
    }
}

internal object ProfileStore {
    private const val PrefName = "weather_profile"
    private const val OccupationKey = "occupation"
    private const val CommuteKey = "commute"
    private const val AllergensKey = "allergens"
    private const val VehicleRestrictionKey = "vehicle_restriction"

    fun load(context: Context): UserProfile? {
        val prefs = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        val occupation = prefs.getString(OccupationKey, null)?.toEnumOrNull<Occupation>() ?: return null
        val commute = prefs.getString(CommuteKey, null)?.toEnumOrNull<CommuteMode>() ?: return null
        val allergens = prefs.getStringSet(AllergensKey, emptySet()).orEmpty()
            .mapNotNull { it.toEnumOrNull<Allergen>() }
            .toSet()
        return UserProfile(
            occupation = occupation,
            commuteMode = commute,
            allergens = allergens,
            vehicleRestrictionEnabled = prefs.getBoolean(VehicleRestrictionKey, false),
        )
    }

    fun save(context: Context, profile: UserProfile) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(OccupationKey, profile.occupation.name)
            .putString(CommuteKey, profile.commuteMode.name)
            .putStringSet(AllergensKey, profile.allergens.map { it.name }.toSet())
            .putBoolean(VehicleRestrictionKey, profile.vehicleRestrictionEnabled)
            .apply()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE).edit().clear().apply()
    }
}

internal object ApiConfigStore {
    private const val PrefName = "weather_api_config"
    private const val SchemaVersionKey = "config_schema_version"
    private const val CurrentSchemaVersion = 9
    private const val EndpointSuffix = "_endpoint"
    private const val KeySuffix = "_key"
    private const val UserAgentSuffix = "_user_agent"
    private const val ApiHostSuffix = "_api_host"
    private const val EnabledSuffix = "_enabled"

    fun load(context: Context): List<WeatherApiConfig> {
        val prefs = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        val schemaVersion = prefs.getInt(SchemaVersionKey, 1)
        val savedFailureStatus = ApiFailureStatusStore.load(context)
        val recoverSeniverse = schemaVersion < 4 &&
            savedFailureStatus?.let { status ->
                status.contains("心知天气已停用") && status.contains("403")
            } == true
        val recoverBuiltInNoKeyWeather = schemaVersion < 8 &&
            savedFailureStatus?.let { status ->
                status.contains("小米天气已停用") || status.contains("MSN 天气已停用")
            } == true
        val clearBuiltInWeatherEndpointFailure = schemaVersion < 9 &&
            savedFailureStatus?.let { status ->
                (status.contains("小米天气暂不可用") && status.contains("400")) ||
                    (status.contains("MSN 天气暂不可用") && status.contains("401"))
            } == true
        val configs = ApiConfigDefaults.defaultConfigs().map { default ->
            val prefix = default.sourceId.name
            val savedEndpoint = prefs.getString(prefix + EndpointSuffix, default.endpoint) ?: default.endpoint
            val legacyOpenMeteoEndpoint = "https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,rain,weather_code,wind_speed_10m&hourly=precipitation_probability,uv_index&timezone=auto"
            val legacyXiaomiCredentialEndpoint = "https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?latitude={lat}&longitude={lon}&isLocated=false&locationKey=weathercn:{locationKey}&days=15&appKey={key}&sign={sign}&isGlobal=false&locale=zh_CN"
            val legacyXiaomiNoCredentialEndpoint = "https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?latitude={lat}&longitude={lon}&isLocated=false&locationKey=weathercn:{locationKey}&days=15&isGlobal=false&locale=zh_CN"
            val legacyMsnCredentialEndpoint = "https://api.msn.com/weather/overview?locale=zh-cn&lat={lat}&lon={lon}&appId=9e21380c-ff19-4c78-b4ea-19558e93a5d3&apiKey={key}&ocid=msftweather&wrapOData=false&units=C&pastPeriods=1&days=10&hours=24"
            val legacyMsnNoCredentialEndpoint = "https://api.msn.com/weather/overview?locale=zh-cn&lat={lat}&lon={lon}&appId=9e21380c-ff19-4c78-b4ea-19558e93a5d3&ocid=msftweather&wrapOData=false&units=C&pastPeriods=1&days=10&hours=24"
            val endpoint = when {
                default.sourceId == SourceId.QWeather &&
                    savedEndpoint.contains("devapi.qweather.com", ignoreCase = true) -> default.endpoint
                schemaVersion < CurrentSchemaVersion &&
                    default.sourceId == SourceId.OpenMeteo &&
                    savedEndpoint == legacyOpenMeteoEndpoint -> default.endpoint
                schemaVersion < CurrentSchemaVersion &&
                    default.sourceId == SourceId.XiaomiWeather &&
                    savedEndpoint in setOf(legacyXiaomiCredentialEndpoint, legacyXiaomiNoCredentialEndpoint) -> default.endpoint
                schemaVersion < CurrentSchemaVersion &&
                    default.sourceId == SourceId.MsnWeather &&
                    savedEndpoint in setOf(legacyMsnCredentialEndpoint, legacyMsnNoCredentialEndpoint) -> default.endpoint
                else -> savedEndpoint
            }
            val savedHost = prefs.getString(prefix + ApiHostSuffix, default.apiHost).orEmpty()
            val migratedHost = savedHost.ifBlank {
                endpoint.substringAfter("://", "")
                    .substringBefore('/')
                    .takeIf { it.endsWith(".qweatherapi.com", ignoreCase = true) }
                    .orEmpty()
            }
            default.copy(
                endpoint = endpoint,
                apiKey = if (schemaVersion < CurrentSchemaVersion && default.sourceId in setOf(SourceId.XiaomiWeather, SourceId.MsnWeather)) {
                    default.apiKey
                } else {
                    prefs.getString(prefix + KeySuffix, default.apiKey) ?: default.apiKey
                },
                userAgent = prefs.getString(prefix + UserAgentSuffix, default.userAgent) ?: default.userAgent,
                enabled = when {
                    schemaVersion < 5 && default.sourceId == SourceId.Meteostat -> true
                    recoverSeniverse && default.sourceId == SourceId.Seniverse -> true
                    schemaVersion < 8 && default.sourceId in setOf(SourceId.XiaomiWeather, SourceId.MsnWeather) -> true
                    else -> prefs.getBoolean(prefix + EnabledSuffix, default.enabled)
                },
                apiHost = migratedHost,
            )
        }
        if (schemaVersion < CurrentSchemaVersion) {
            save(context, configs)
            if (recoverSeniverse || recoverBuiltInNoKeyWeather || clearBuiltInWeatherEndpointFailure) {
                ApiFailureStatusStore.clear(context)
            }
        }
        return configs
    }

    fun save(context: Context, configs: List<WeatherApiConfig>) {
        val editor = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE).edit().clear()
        editor.putInt(SchemaVersionKey, CurrentSchemaVersion)
        configs.forEach { config ->
            val prefix = config.sourceId.name
            editor
                .putString(prefix + EndpointSuffix, config.endpoint)
                .putString(prefix + KeySuffix, config.apiKey)
                .putString(prefix + UserAgentSuffix, config.userAgent)
                .putString(prefix + ApiHostSuffix, config.apiHost)
                .putBoolean(prefix + EnabledSuffix, config.enabled)
        }
        editor.apply()
    }
}

internal object ApiFailureStatusStore {
    private const val PrefName = "weather_api_failure_status"
    private const val MessageKey = "message"

    fun load(context: Context): String? = context
        .getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        .getString(MessageKey, null)
        ?.takeIf(String::isNotBlank)

    fun save(context: Context, message: String?) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(MessageKey, message)
            .apply()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE).edit().clear().apply()
    }
}

internal object WeatherRefreshStore {
    private const val PrefName = "weather_refresh_cache"
    private const val RegionKey = "region_key"
    private const val ConfigSignatureKey = "config_signature"
    private const val LastRequestKey = "last_request_at"
    private const val CachedAtKey = "cached_at"
    private const val SnapshotKey = "snapshot"

    fun lastRequestAt(context: Context, regionKey: String, apiConfigSignature: Int): Long {
        val prefs = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        if (prefs.getString(RegionKey, null) != regionKey) return 0L
        if (prefs.getInt(ConfigSignatureKey, Int.MIN_VALUE) != apiConfigSignature) return 0L
        return prefs.getLong(LastRequestKey, 0L)
    }

    fun markRequest(
        context: Context,
        regionKey: String,
        apiConfigSignature: Int,
        requestedAtMillis: Long,
    ) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(RegionKey, regionKey)
            .putInt(ConfigSignatureKey, apiConfigSignature)
            .putLong(LastRequestKey, requestedAtMillis)
            .commit()
    }

    fun save(context: Context, entry: WeatherSnapshotCacheEntry) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(RegionKey, entry.snapshot.region.storageKey)
            .putInt(ConfigSignatureKey, entry.apiConfigSignature)
            .putLong(CachedAtKey, entry.cachedAtMillis)
            .putString(SnapshotKey, entry.snapshot.toCacheJson().toString())
            .apply()
    }

    fun load(
        context: Context,
        region: District,
        profile: UserProfile?,
        repository: WeatherRepository,
        apiConfigSignature: Int,
    ): WeatherSnapshotCacheEntry? {
        val prefs = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        if (prefs.getString(RegionKey, null) != region.storageKey) return null
        if (prefs.getInt(ConfigSignatureKey, Int.MIN_VALUE) != apiConfigSignature) return null
        val raw = prefs.getString(SnapshotKey, null) ?: return null
        return runCatching {
            val snapshot = JSONObject(raw).toCachedSnapshot(region)
            WeatherSnapshotCacheEntry(
                snapshot = repository.rebuildSnapshotForProfile(snapshot, profile),
                apiConfigSignature = apiConfigSignature,
                cachedAtMillis = prefs.getLong(CachedAtKey, snapshot.updatedAtMillis),
            )
        }.getOrNull()
    }

    private fun WeatherSnapshot.toCacheJson(): JSONObject = JSONObject()
        .put("updatedAtMillis", updatedAtMillis)
        .put("fused", fused.toCacheJson())
        .put("readings", JSONArray().apply { readings.forEach { put(it.toCacheJson()) } })
        .put("sourceWeights", JSONArray().apply { sourceWeights.forEach { put(it.toCacheJson()) } })
        .put("fusionSummary", fusionSummary)
        .put("anomalyNotes", JSONArray().apply { anomalyNotes.forEach(::put) })
        .put("dailyForecast", JSONArray().apply { dailyForecast.forEach { put(it.toCacheJson()) } })
        .put("hourlyForecast", JSONArray().apply { hourlyForecast.forEach { put(it.toCacheJson()) } })
        .put("astronomy", astronomy.toCacheJson())

    private fun WeatherReading.toCacheJson(): JSONObject = JSONObject()
        .put("sourceId", source.id.name)
        .put("sourceName", source.displayName)
        .put("sourceCategory", source.category)
        .putNullable("temperatureC", temperatureC)
        .putNullable("feelsLikeC", feelsLikeC)
        .putNullable("rainProbability", rainProbability)
        .putNullable("rainNextHourMm", rainNextHourMm)
        .putNullable("windKph", windKph)
        .putNullable("aqi", aqi)
        .putNullable("pm25", pm25)
        .putNullable("uvIndex", uvIndex)
        .putNullable("humidityPercent", humidityPercent)
        .putNullable("pressureHpa", pressureHpa)
        .putNullable("dewPointC", dewPointC)
        .putNullable("pollenLevel", pollenLevel)
        .putNullable("sporeLevel", sporeLevel)
        .putNullable("condition", condition?.name)
        .apply { alert?.let { put("alert", it.toCacheJson()) } }

    private fun FusedWeather.toCacheJson(): JSONObject = JSONObject()
        .put("temperatureC", temperatureC)
        .put("feelsLikeC", feelsLikeC)
        .put("rainProbability", rainProbability)
        .put("rainNextHourMm", rainNextHourMm)
        .put("windKph", windKph)
        .put("aqi", aqi)
        .put("pm25", pm25)
        .put("uvIndex", uvIndex)
        .put("humidityPercent", humidityPercent)
        .put("pressureHpa", pressureHpa)
        .putNullable("dewPointC", dewPointC)
        .put("pollenLevel", pollenLevel)
        .put("sporeLevel", sporeLevel)
        .put("condition", condition.name)
        .put("alert", alert.toCacheJson())
        .put("confidencePercent", confidencePercent)

    private fun WeatherAlert.toCacheJson(): JSONObject = JSONObject()
        .put("level", level.name)
        .put("title", title)
        .put("detail", detail)

    private fun SourceWeightView.toCacheJson(): JSONObject = JSONObject()
        .put("name", name)
        .put("normalizedWeight", normalizedWeight.toDouble())
        .put("reason", reason)

    private fun DailyForecast.toCacheJson(): JSONObject = JSONObject()
        .put("timeMillis", timeMillis)
        .put("highC", highC)
        .put("lowC", lowC)
        .put("condition", condition.name)
        .putNullable("rainProbability", rainProbability)
        .putNullable("aqi", aqi)
        .putNullable("windKph", windKph)
        .putNullable("sunrise", sunrise)
        .putNullable("sunset", sunset)
        .put("isYesterday", isYesterday)

    private fun HourlyForecast.toCacheJson(): JSONObject = JSONObject()
        .put("timeMillis", timeMillis)
        .put("temperatureC", temperatureC)
        .put("condition", condition.name)
        .putNullable("rainProbability", rainProbability)
        .putNullable("aqi", aqi)
        .putNullable("windKph", windKph)
        .putNullable("windDirection", windDirection)

    private fun AstronomyInfo.toCacheJson(): JSONObject = JSONObject()
        .putNullable("sunrise", sunrise)
        .putNullable("sunset", sunset)
        .putNullable("moonrise", moonrise)
        .putNullable("moonset", moonset)
        .putNullable("moonPhase", moonPhase)

    private fun JSONObject.toCachedSnapshot(region: District): WeatherSnapshot = WeatherSnapshot(
        region = region,
        readings = optJSONArray("readings").toWeatherReadings(),
        fused = getJSONObject("fused").toFusedWeather(),
        advice = emptyList(),
        sourceWeights = optJSONArray("sourceWeights").toSourceWeights(),
        fusionSummary = optString("fusionSummary"),
        anomalyNotes = optJSONArray("anomalyNotes").toStringList(),
        dailyForecast = optJSONArray("dailyForecast").toDailyForecasts(),
        hourlyForecast = optJSONArray("hourlyForecast").toHourlyForecasts(),
        astronomy = optJSONObject("astronomy")?.toAstronomyInfo() ?: AstronomyInfo.Empty,
        updatedAtMillis = getLong("updatedAtMillis"),
    )

    private fun JSONArray?.toWeatherReadings(): List<WeatherReading> = mapObjects { json ->
        val sourceId = json.optString("sourceId").toEnumOrNull<SourceId>() ?: return@mapObjects null
        WeatherReading(
            source = WeatherSource(
                id = sourceId,
                displayName = json.optString("sourceName"),
                category = json.optString("sourceCategory"),
            ),
            temperatureC = json.optDoubleOrNull("temperatureC"),
            feelsLikeC = json.optDoubleOrNull("feelsLikeC"),
            rainProbability = json.optDoubleOrNull("rainProbability"),
            rainNextHourMm = json.optDoubleOrNull("rainNextHourMm"),
            windKph = json.optDoubleOrNull("windKph"),
            aqi = json.optIntOrNull("aqi"),
            pm25 = json.optDoubleOrNull("pm25"),
            uvIndex = json.optDoubleOrNull("uvIndex"),
            humidityPercent = json.optIntOrNull("humidityPercent"),
            pressureHpa = json.optDoubleOrNull("pressureHpa"),
            dewPointC = json.optDoubleOrNull("dewPointC"),
            pollenLevel = json.optIntOrNull("pollenLevel"),
            sporeLevel = json.optIntOrNull("sporeLevel"),
            condition = json.optString("condition").toEnumOrNull<WeatherCondition>(),
            alert = json.optJSONObject("alert")?.toWeatherAlert(),
        )
    }

    private fun JSONObject.toFusedWeather(): FusedWeather = FusedWeather(
        temperatureC = getDouble("temperatureC"),
        feelsLikeC = getDouble("feelsLikeC"),
        rainProbability = getDouble("rainProbability"),
        rainNextHourMm = getDouble("rainNextHourMm"),
        windKph = getDouble("windKph"),
        aqi = getInt("aqi"),
        pm25 = getDouble("pm25"),
        uvIndex = getDouble("uvIndex"),
        humidityPercent = getInt("humidityPercent"),
        pressureHpa = optDouble("pressureHpa", 1013.25),
        dewPointC = optDoubleOrNull("dewPointC"),
        pollenLevel = getInt("pollenLevel"),
        sporeLevel = getInt("sporeLevel"),
        condition = optString("condition").toEnumOrNull<WeatherCondition>() ?: WeatherCondition.Cloudy,
        alert = getJSONObject("alert").toWeatherAlert(),
        confidencePercent = getInt("confidencePercent"),
    )

    private fun JSONObject.toWeatherAlert(): WeatherAlert = WeatherAlert(
        level = optString("level").toEnumOrNull<AlertLevel>() ?: AlertLevel.None,
        title = optString("title"),
        detail = optString("detail"),
    )

    private fun JSONArray?.toSourceWeights(): List<SourceWeightView> = mapObjects { json ->
        SourceWeightView(
            name = json.optString("name"),
            normalizedWeight = json.optDouble("normalizedWeight").toFloat(),
            reason = json.optString("reason"),
        )
    }

    private fun JSONArray?.toDailyForecasts(): List<DailyForecast> = mapObjects { json ->
        DailyForecast(
            timeMillis = json.getLong("timeMillis"),
            highC = json.getDouble("highC"),
            lowC = json.getDouble("lowC"),
            condition = json.optString("condition").toEnumOrNull<WeatherCondition>() ?: WeatherCondition.Cloudy,
            rainProbability = json.optDoubleOrNull("rainProbability"),
            aqi = json.optIntOrNull("aqi"),
            windKph = json.optDoubleOrNull("windKph"),
            sunrise = json.optNullableString("sunrise"),
            sunset = json.optNullableString("sunset"),
            isYesterday = json.optBoolean("isYesterday"),
        )
    }

    private fun JSONArray?.toHourlyForecasts(): List<HourlyForecast> = mapObjects { json ->
        HourlyForecast(
            timeMillis = json.getLong("timeMillis"),
            temperatureC = json.getDouble("temperatureC"),
            condition = json.optString("condition").toEnumOrNull<WeatherCondition>() ?: WeatherCondition.Cloudy,
            rainProbability = json.optDoubleOrNull("rainProbability"),
            aqi = json.optIntOrNull("aqi"),
            windKph = json.optDoubleOrNull("windKph"),
            windDirection = json.optDoubleOrNull("windDirection"),
        )
    }

    private fun JSONObject.toAstronomyInfo(): AstronomyInfo = AstronomyInfo(
        sunrise = optNullableString("sunrise"),
        sunset = optNullableString("sunset"),
        moonrise = optNullableString("moonrise"),
        moonset = optNullableString("moonset"),
        moonPhase = optNullableString("moonPhase"),
    )

    private fun JSONArray?.toStringList(): List<String> = buildList {
        if (this@toStringList == null) return@buildList
        repeat(length()) { index -> optString(index).takeIf(String::isNotBlank)?.let(::add) }
    }

    private inline fun <T : Any> JSONArray?.mapObjects(transform: (JSONObject) -> T?): List<T> = buildList {
        if (this@mapObjects == null) return@buildList
        repeat(length()) { index -> optJSONObject(index)?.let(transform)?.let(::add) }
    }

    private fun JSONObject.putNullable(name: String, value: Any?): JSONObject = apply {
        if (value != null) put(name, value)
    }

    private fun JSONObject.optIntOrNull(name: String): Int? =
        if (!has(name) || isNull(name)) null else optInt(name)

    private fun JSONObject.optNullableString(name: String): String? =
        if (!has(name) || isNull(name)) null else optString(name).takeIf(String::isNotBlank)
}

internal object RegionStore {
    private const val PrefName = "selected_weather_region"

    fun load(context: Context): District? {
        val raw = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .getString("region", null) ?: return null
        return runCatching {
            val json = JSONObject(raw)
            District(
                countryCode = json.optString("countryCode", "CN"),
                province = json.optString("province"),
                city = json.optString("city"),
                district = json.optString("district"),
                latitude = json.getDouble("latitude"),
                longitude = json.getDouble("longitude"),
                locationKey = json.optString("locationKey"),
            )
        }.getOrNull()
    }

    fun save(context: Context, region: District) {
        val json = JSONObject()
            .put("countryCode", region.countryCode)
            .put("province", region.province)
            .put("city", region.city)
            .put("district", region.district)
            .put("latitude", region.latitude)
            .put("longitude", region.longitude)
            .put("locationKey", region.locationKey)
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString("region", json.toString())
            .apply()
    }
}

internal object SavedRegionsStore {
    private const val PrefName = "saved_weather_regions"
    private const val RegionsKey = "regions"

    fun load(context: Context): List<District> {
        val raw = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .getString(RegionsKey, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            buildList {
                repeat(array.length()) { index ->
                    array.optJSONObject(index)?.toDistrictOrNull()?.let(::add)
                }
            }.distinctBy(District::storageKey)
        }.getOrDefault(emptyList())
    }

    fun save(context: Context, regions: List<District>) {
        val array = JSONArray()
        regions.distinctBy(District::storageKey).take(8).forEach { region ->
            array.put(
                JSONObject()
                    .put("countryCode", region.countryCode)
                    .put("province", region.province)
                    .put("city", region.city)
                    .put("district", region.district)
                    .put("latitude", region.latitude)
                    .put("longitude", region.longitude)
                    .put("locationKey", region.locationKey)
            )
        }
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(RegionsKey, array.toString())
            .apply()
    }

    private fun JSONObject.toDistrictOrNull(): District? = runCatching {
        District(
            countryCode = optString("countryCode", "CN"),
            province = optString("province"),
            city = optString("city"),
            district = optString("district"),
            latitude = getDouble("latitude"),
            longitude = getDouble("longitude"),
            locationKey = optString("locationKey"),
        )
    }.getOrNull()
}

internal object LocationMethodStore {
    private const val PrefName = "location_method"
    private const val MethodKey = "method"

    fun load(context: Context): LocationMethod {
        val prefs = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        return prefs.getString(MethodKey, null)?.toEnumOrNull<LocationMethod>()
            ?: LocationMethod.Device
    }

    fun save(context: Context, method: LocationMethod) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit()
            .putString(MethodKey, method.name)
            .apply()
    }
}

internal inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? = runCatching { enumValueOf<T>(this) }.getOrNull()

internal fun hasLocationPermission(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
@Suppress("DEPRECATION")
internal suspend fun LocationManager.awaitFreshLocation(): Location? {
    val providers = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)
        .filter { provider -> runCatching { isProviderEnabled(provider) }.getOrDefault(false) }
    if (providers.isEmpty()) return null

    return withTimeoutOrNull(12_000) {
        suspendCancellableCoroutine { continuation ->
            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    if (continuation.isActive) {
                        runCatching { removeUpdates(this) }
                        continuation.resume(location)
                    }
                }
            }

            providers.forEach { provider ->
                runCatching {
                    requestSingleUpdate(provider, listener, Looper.getMainLooper())
                }
            }

            continuation.invokeOnCancellation {
                runCatching { removeUpdates(listener) }
            }
        }
    }
}

@SuppressLint("MissingPermission")
internal fun LocationManager.latestKnownLocation(): Location? {
    return listOf(
        LocationManager.GPS_PROVIDER,
        LocationManager.NETWORK_PROVIDER,
        LocationManager.PASSIVE_PROVIDER,
    )
        .mapNotNull { provider -> runCatching { getLastKnownLocation(provider) }.getOrNull() }
        .maxByOrNull { it.time }
}

