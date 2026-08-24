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

internal object WeatherFusionEngine {
    fun fuse(region: District, readings: List<WeatherReading>): FusionResult {
        val temp = weightedAverage(region, readings, Metric.Temperature) { it.temperatureC } ?: 0.0
        val feelsLike = weightedAverage(region, readings, Metric.Temperature) { it.feelsLikeC } ?: temp
        val rainProbability = weightedAverage(region, readings, Metric.Percent) { it.rainProbability } ?: 0.0
        val rainNextHourSample = weightedAverage(region, readings, Metric.Rain) { it.rainNextHourMm }
        val rainNextHour = rainNextHourSample ?: 0.0
        val wind = weightedAverage(region, readings, Metric.Wind) { it.windKph } ?: 0.0
        val aqi = weightedAverage(region, readings, Metric.Air) { it.aqi?.toDouble() }?.roundToInt() ?: 0
        val pm25 = weightedAverage(region, readings, Metric.Air) { it.pm25 } ?: 0.0
        val uvIndex = weightedAverage(region, readings, Metric.Uv) { it.uvIndex } ?: 0.0
        val humidity = weightedAverage(region, readings, Metric.Percent) { it.humidityPercent?.toDouble() }?.roundToInt() ?: 0
        val pressure = weightedAverage(region, readings, Metric.Pressure) { it.pressureHpa } ?: 1013.25
        val dewPoint = weightedAverage(region, readings, Metric.Temperature) { it.dewPointC }
        val pollen = weightedAverage(region, readings, Metric.Level) { it.pollenLevel?.toDouble() }?.roundToInt() ?: 0
        val spore = weightedAverage(region, readings, Metric.Level) { it.sporeLevel?.toDouble() }?.roundToInt() ?: 0
        val condition = chooseCondition(readings, region).refinedByHourlyPrecipitation(rainNextHourSample)
        val alert = readings.mapNotNull { it.alert }.maxByOrNull { it.level.severity }
            ?: WeatherAlert(AlertLevel.None, "无显著预警", "未发现高影响天气。")
        val sourceWeights = readings
            .map { reading ->
                val weight = baseWeight(region, reading.source.id, Metric.Temperature)
                SourceWeightView(
                    name = reading.source.displayName,
                    normalizedWeight = (weight / 2.0).toFloat().coerceIn(0.15f, 1f),
                    reason = weightReason(region, reading.source.id),
                )
            }
            .distinctBy { it.name }
        val anomalyNotes = buildList {
            anomalyNote(region, readings, Metric.Temperature, "温度") { it.temperatureC }?.let(::add)
            anomalyNote(region, readings, Metric.Rain, "短时雨量") { it.rainNextHourMm }?.let(::add)
            anomalyNote(region, readings, Metric.Air, "空气质量") { it.aqi?.toDouble() }?.let(::add)
        }
        val summary = if (region.isDomestic) {
            val liveNames = readings
                .filter { it.source.category.contains("实时") }
                .map { it.source.displayName }
                .distinct()
            if (liveNames.isNotEmpty()) {
                "${liveNames.joinToString("、")}已参与国内实况平均。"
            } else {
                "实时接口暂未返回，当前使用离线演示数据；请检查网络、城市 key 或天气源配置。"
            }
        } else {
            "国外源由MSN 与 OpenWeather 提供全球实况。"
        }
        return FusionResult(
            weather = FusedWeather(
                temperatureC = temp,
                feelsLikeC = feelsLike,
                rainProbability = rainProbability.coerceIn(0.0, 100.0),
                rainNextHourMm = rainNextHour.coerceAtLeast(0.0),
                windKph = wind.coerceAtLeast(0.0),
                aqi = aqi.coerceAtLeast(0),
                pm25 = pm25.coerceAtLeast(0.0),
                uvIndex = uvIndex.coerceAtLeast(0.0),
                humidityPercent = humidity.coerceIn(0, 100),
                pollenLevel = pollen.coerceIn(0, 5),
                sporeLevel = spore.coerceIn(0, 5),
                condition = condition,
                alert = alert,
                confidencePercent = confidence(readings),
                pressureHpa = pressure.coerceIn(870.0, 1085.0),
                dewPointC = dewPoint,
            ),
            sourceWeights = sourceWeights,
            summary = summary,
            anomalyNotes = anomalyNotes,
        )
    }

    fun fuseDaily(region: District, readings: List<WeatherReading>): List<DailyForecast> {
        val dayFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return readings
            .flatMap { reading ->
                reading.dailyForecast.map { forecast ->
                    DailyForecastSample(reading.source.id, forecast)
                }
            }
            .groupBy { sample -> dayFormatter.format(Date(sample.forecast.timeMillis)) }
            .values
            .mapNotNull { samples ->
                val high = weightedDailyAverage(region, samples, Metric.Temperature) { it.highC }
                    ?: return@mapNotNull null
                val low = weightedDailyAverage(region, samples, Metric.Temperature) { it.lowC }
                    ?: return@mapNotNull null
                val representative = samples.maxByOrNull {
                    baseWeight(region, it.sourceId, Metric.Temperature)
                } ?: return@mapNotNull null
                val condition = chooseWeightedCondition(
                    samples.map { sample ->
                        sample.forecast.condition to baseWeight(region, sample.sourceId, Metric.Temperature)
                    }
                )
                    ?: representative.forecast.condition
                val sunrise = highestWeightedText(region, samples) { it.sunrise }
                val sunset = highestWeightedText(region, samples) { it.sunset }
                DailyForecast(
                    timeMillis = representative.forecast.timeMillis,
                    highC = high,
                    lowC = low,
                    condition = condition,
                    rainProbability = weightedDailyAverage(region, samples, Metric.Percent) { it.rainProbability }
                        ?.coerceIn(0.0, 100.0),
                    aqi = weightedDailyAverage(region, samples, Metric.Air) { it.aqi?.toDouble() }
                        ?.roundToInt(),
                    windKph = weightedDailyAverage(region, samples, Metric.Wind) { it.windKph }
                        ?.coerceAtLeast(0.0),
                    sunrise = sunrise,
                    sunset = sunset,
                    isYesterday = samples.any { it.forecast.isYesterday },
                )
            }
            .sortedBy { it.timeMillis }
    }

    private fun weightedDailyAverage(
        region: District,
        samples: List<DailyForecastSample>,
        metric: Metric,
        valueOf: (DailyForecast) -> Double?,
    ): Double? {
        val weighted = samples.mapNotNull { sample ->
            valueOf(sample.forecast)?.let { value ->
                WeightedSample(value, baseWeight(region, sample.sourceId, metric))
            }
        }
        if (weighted.isEmpty()) return null
        val median = weighted.map { it.value }.sorted().let { it[it.size / 2] }
        val filtered = weighted.filter { abs(it.value - median) <= metric.outlierTolerance }.ifEmpty { weighted }
        val totalWeight = filtered.sumOf { it.weight }
        return filtered.sumOf { it.value * it.weight } / totalWeight
    }

    private fun highestWeightedText(
        region: District,
        samples: List<DailyForecastSample>,
        valueOf: (DailyForecast) -> String?,
    ): String? = samples
        .mapNotNull { sample ->
            valueOf(sample.forecast)?.takeIf { it.isNotBlank() }?.let { value ->
                value to baseWeight(region, sample.sourceId, Metric.Temperature)
            }
        }
        .maxByOrNull { it.second }
        ?.first

    private fun weightedAverage(
        region: District,
        readings: List<WeatherReading>,
        metric: Metric,
        valueOf: (WeatherReading) -> Double?,
    ): Double? {
        val samples = readings.mapNotNull { reading ->
            valueOf(reading)?.let { value -> WeightedSample(value, baseWeight(region, reading.source.id, metric)) }
        }
        if (samples.isEmpty()) return null
        val median = samples.map { it.value }.sorted().let { sorted -> sorted[sorted.size / 2] }
        val filtered = samples.filter { abs(it.value - median) <= metric.outlierTolerance }.ifEmpty { samples }
        val totalWeight = filtered.sumOf { it.weight }
        return filtered.sumOf { it.value * it.weight } / totalWeight
    }

    private fun baseWeight(region: District, sourceId: SourceId, metric: Metric): Double = when {
        region.isDomestic && sourceId == SourceId.XiaomiWeather -> 1.35
        region.isDomestic && sourceId == SourceId.Cnemc && metric == Metric.Air -> 2.0
        region.countryCode == "US" && sourceId == SourceId.Nws -> 1.28
        sourceId == SourceId.MsnWeather -> 1.18
        !region.isDomestic && sourceId == SourceId.OpenWeather -> 1.15
        !region.isDomestic && sourceId == SourceId.OpenMeteo -> 1.10
        sourceId == SourceId.Meteostat -> 0.7
        else -> 1.0
    }

    private fun weightReason(region: District, sourceId: SourceId): String = when {
        region.isDomestic && sourceId == SourceId.XiaomiWeather -> "国内优先"
        region.isDomestic && sourceId == SourceId.Cnemc -> "空气质量优先"
        sourceId == SourceId.MsnWeather -> "全球综合"
        !region.isDomestic && sourceId == SourceId.OpenWeather -> "国外增强"
        !region.isDomestic && sourceId == SourceId.OpenMeteo -> "免费基线"
        region.countryCode == "US" && sourceId == SourceId.Nws -> "美国官方优先"
        sourceId == SourceId.Meteostat -> "历史源降权"
        else -> "平均参与"
    }

    private fun chooseCondition(readings: List<WeatherReading>, region: District): WeatherCondition =
        chooseWeightedCondition(
            readings.mapNotNull { reading ->
            reading.condition?.let { condition -> condition to baseWeight(region, reading.source.id, Metric.Temperature) }
        }
        )
            ?: WeatherCondition.Cloudy

    private fun chooseWeightedCondition(
        samples: List<Pair<WeatherCondition, Double>>,
    ): WeatherCondition? {
        val winningFamily = samples
            .groupBy { it.first.visualFamily }
            .maxByOrNull { (_, values) -> values.sumOf { it.second } }
            ?.key
            ?: return null
        return samples
            .filter { it.first.visualFamily == winningFamily }
            .groupBy { it.first }
            .map { (condition, values) -> condition to values.sumOf { it.second } }
            .maxWithOrNull(
                compareBy<Pair<WeatherCondition, Double>> { it.second }
                    .thenBy { it.first.intensityRank }
            )
            ?.first
    }

    private fun confidence(readings: List<WeatherReading>): Int {
        val participating = readings.count { it.temperatureC != null || it.aqi != null || it.rainProbability != null }
        val coverage = (participating * 9).coerceAtMost(72)
        val quality = if (readings.any { it.source.id in setOf(SourceId.XiaomiWeather, SourceId.MsnWeather, SourceId.OpenMeteo, SourceId.Nws) }) 12 else 6
        val air = if (readings.any { it.aqi != null }) 9 else 0
        return (coverage + quality + air).coerceIn(42, 93)
    }

    private fun anomalyNote(
        region: District,
        readings: List<WeatherReading>,
        metric: Metric,
        label: String,
        valueOf: (WeatherReading) -> Double?,
    ): String? {
        val values = readings.mapNotNull { reading -> valueOf(reading)?.let { reading.source.displayName to it } }
        if (values.size < 3) return null
        val median = values.map { it.second }.sorted().let { it[it.size / 2] }
        val outliers = values.filter { abs(it.second - median) > metric.outlierTolerance }
            .filter { (name, _) -> name != "小米天气" || !region.isDomestic }
        if (outliers.isEmpty()) return null
        return "$label 发现 ${outliers.joinToString { it.first }} 与多数源偏离，已在融合时降权。"
    }
}

internal enum class Metric(val outlierTolerance: Double) {
    Temperature(2.8),
    Percent(28.0),
    Rain(6.0),
    Wind(18.0),
    Air(32.0),
    Uv(2.2),
    Level(1.8),
    Pressure(18.0),
}

internal data class WeightedSample(val value: Double, val weight: Double)

internal data class DailyForecastSample(
    val sourceId: SourceId,
    val forecast: DailyForecast,
)

internal data class FusionResult(
    val weather: FusedWeather,
    val sourceWeights: List<SourceWeightView>,
    val summary: String,
    val anomalyNotes: List<String>,
)

internal class AdviceProvider(
    aiApiKey: String,
    private val offlineEngine: OfflineAdviceEngine,
) {
    private val aiGateway = AiAdviceGateway(aiApiKey)

    fun buildAdvice(profile: UserProfile, region: District, weather: FusedWeather): List<PersonalizedAdvice> =
        aiGateway.requestAdviceOrNull(profile, region, weather) ?: offlineEngine.buildAdvice(profile, region, weather)
}

internal class AiAdviceGateway(private val apiKey: String) {
    fun requestAdviceOrNull(
        profile: UserProfile,
        region: District,
        weather: FusedWeather,
    ): List<PersonalizedAdvice>? {
        if (apiKey.isBlank()) return null

        // Real AI requests will be wired here after the target API and schema are chosen.
        return null
    }
}

internal object OfflineAdviceEngine {
    fun buildAdvice(profile: UserProfile, region: District, weather: FusedWeather): List<PersonalizedAdvice> = buildList {
        add(clothingAdvice(weather))
        add(commuteAdvice(profile, weather))
        add(activityAdvice(profile, weather))
        add(airQualityAdvice(profile, weather))
        add(sunProtectionAdvice(profile, weather))
        add(rainAdvice(weather))
        weather.alert.takeIf { it.level.severity >= AlertLevel.Severe.severity }?.let {
            add(extremeAdvice(profile, region, it))
        }
        allergenAdvice(profile, weather)?.let(::add)
    }
//各种提示和预警
    private fun clothingAdvice(weather: FusedWeather): PersonalizedAdvice {
        val detail = when {
            weather.feelsLikeC >= 32 -> "体感偏热，建议短袖透气衣物；户外停留时间长时准备替换衣物。"
            weather.feelsLikeC <= 10 -> "体感偏冷，建议外套叠穿，注意手部和颈部保暖。"
            weather.feelsLikeC >= 27 -> "体感偏暖，建议选择轻薄透气衣物，并根据室内空调准备薄外搭。"
            weather.feelsLikeC <= 16 -> "体感偏凉，建议长袖搭配轻薄外套，早晚注意温差。"
            weather.rainProbability >= 60 -> "温度适中但雨意明显，建议轻薄外套搭配便携雨具。"
            else -> "体感舒适，选择透气的日常衣物即可。"
        }
        return PersonalizedAdvice("穿衣", detail, Icons.Filled.Thermostat, AdviceLevel.Info)
    }

    private fun commuteAdvice(profile: UserProfile, weather: FusedWeather): PersonalizedAdvice {
        val rainHigh = weather.rainProbability >= 70 || weather.rainNextHourMm >= 6
        val detail = when (profile.commuteMode) {
            CommuteMode.Taxi -> if (rainHigh) "雨量会推高叫车等待时间，建议提前 15-25 分钟下单，并避开低洼上车点。" else "打车通勤压力不高，出门前确认目的地附近道路情况。"
            CommuteMode.Bike -> when {
                rainHigh -> "骑车风险偏高，建议改用公共交通或打车；必须骑行时准备雨衣和防滑装备。"
                weather.windKph >= 30 -> "风力会影响骑行稳定性，经过路口和高架附近时注意横风。"
                else -> "骑行条件尚可，出发前检查胎压和照明即可。"
            }
            CommuteMode.Walk -> when {
                rainHigh -> "步行会明显受雨影响，建议预留绕行时间，鞋子选防滑款。"
                weather.uvIndex >= 6 -> "步行通勤可行，午间尽量选择阴凉路线并做好防晒。"
                else -> "步行通勤条件平稳，按日常时间出门即可。"
            }
            CommuteMode.Car -> when {
                rainHigh -> "开车需降低车速并拉大车距，提前查看涉水和拥堵路段。"
                weather.condition.visualFamily in setOf(WeatherVisualFamily.Atmosphere, WeatherVisualFamily.Dust) ->
                    "能见度可能受影响，开车时合理使用灯光并增加跟车距离。"
                else -> "开车通勤条件正常，按实时路况安排出发时间。"
            }
            CommuteMode.PublicTransit -> if (rainHigh) "公交和地铁站口可能拥堵，建议提前出门并准备防水包。" else "公共交通通勤正常，留意站外短时阵雨。"
            CommuteMode.Other -> "请结合实时路况和降雨提醒安排出门时间。"
        }
        return PersonalizedAdvice("通勤", detail, profile.commuteMode.icon, if (rainHigh) AdviceLevel.Caution else AdviceLevel.Info)
    }

    private fun activityAdvice(profile: UserProfile, weather: FusedWeather): PersonalizedAdvice {
        val detail = when (profile.occupation) {
            Occupation.Outdoor -> when {
                weather.alert.level == AlertLevel.Severe -> "户外作业不建议继续安排，需等待属地停工通知和现场安全评估。"
                weather.uvIndex >= 7 -> "户外作业要分时段轮换，补水、防晒和降温设备都要提前准备。"
                weather.rainProbability >= 60 -> "户外作业建议把高风险项目提前或改期，并检查防雨用电安全。"
                else -> "户外工作条件可控，仍建议定时补水并关注短临预报。"
            }
            Occupation.Office -> "办公室人群重点关注通勤时段降雨和室内外温差，备一件薄外套更稳。"
            Occupation.Student -> "上学放学时段留意降雨和预警，雨天书包做好防水。"
            Occupation.Homebody -> "居家以通风和空气质量为主，AQI 走高时减少开窗时间。"
            Occupation.Other -> "按出门时长调整计划，优先关注降雨、空气质量和极端天气预警。"
        }
        val level = if (weather.alert.level.severity >= AlertLevel.Severe.severity) AdviceLevel.Warning else AdviceLevel.Info
        return PersonalizedAdvice("日程安排", detail, profile.occupation.icon, level)
    }

    private fun airQualityAdvice(profile: UserProfile, weather: FusedWeather): PersonalizedAdvice {
        val exposureHigh = profile.occupation == Occupation.Outdoor ||
            profile.commuteMode == CommuteMode.Bike || profile.commuteMode == CommuteMode.Walk
        val detail = when {
            weather.aqi >= 150 -> "空气质量较差，建议减少户外暴露；敏感人群佩戴防护口罩。"
            weather.aqi >= 100 && exposureHigh -> "空气质量一般，户外活动建议缩短时长，运动强度降一档。"
            weather.aqi >= 100 -> "空气质量一般，长时间外出可准备口罩。"
            else -> "空气质量可接受，正常活动即可。"
        }
        val level = if (weather.aqi >= 150) AdviceLevel.Warning else if (weather.aqi >= 100) AdviceLevel.Caution else AdviceLevel.Info
        return PersonalizedAdvice("空气质量", detail, Icons.Filled.HealthAndSafety, level)
    }

    private fun sunProtectionAdvice(profile: UserProfile, weather: FusedWeather): PersonalizedAdvice {
        val uvSensitive = Allergen.Uv in profile.allergens
        val detail = when {
            weather.uvIndex >= 8 && profile.occupation == Occupation.Outdoor ->
                "紫外线风险高，户外作业建议使用 SPF30+ 防晒、帽子或遮阳装备，并按产品说明及时补涂。"
            weather.uvIndex >= 8 && profile.occupation == Occupation.Student ->
                "紫外线风险高，上学前做好防晒；体育课或长时间户外活动后按需补涂。"
            weather.uvIndex >= 8 && profile.occupation == Occupation.Homebody ->
                "紫外线风险高；若今天不外出无需额外安排，靠近强日照窗边时可拉帘或避开直晒。"
            weather.uvIndex >= 8 -> "紫外线风险高，外出建议使用 SPF30+ 防晒，并搭配帽子或遮阳伞。"
            uvSensitive && profile.occupation == Occupation.Homebody ->
                "你对紫外线较敏感；居家无需套用户外防晒流程，靠近强日照窗边时注意遮挡。"
            uvSensitive -> "你对紫外线较敏感，外出时建议提前做好温和防晒并减少长时间直晒。"
            weather.uvIndex >= 6 -> "紫外线偏强，午间外出建议做好基础防晒。"
            else -> "紫外线压力不高，按实际外出时长决定是否使用基础防晒。"
        }
        val level = if (weather.uvIndex >= 8 || uvSensitive) AdviceLevel.Caution else AdviceLevel.Info
        return PersonalizedAdvice("防晒", detail, Icons.Filled.WbSunny, level)
    }

    private fun rainAdvice(weather: FusedWeather): PersonalizedAdvice {
        val detail = when {
            weather.rainNextHourMm >= 10 -> "短时雨量较大，建议推迟非必要外出，避开地下通道和低洼路段。"
            weather.rainProbability >= 65 -> "降雨概率高，雨伞、雨衣和防水袋建议随身。"
            weather.rainProbability >= 35 -> "存在阵雨可能，出门前再看一次短临更新。"
            else -> "降雨风险低，正常安排即可。"
        }
        val level = if (weather.rainNextHourMm >= 10) AdviceLevel.Warning else if (weather.rainProbability >= 65) AdviceLevel.Caution else AdviceLevel.Info
        return PersonalizedAdvice("降雨提醒", detail, Icons.Filled.Umbrella, level)
    }

    private fun extremeAdvice(profile: UserProfile, region: District, alert: WeatherAlert): PersonalizedAdvice {
        val affectedGroup = when (profile.occupation) {
            Occupation.Student -> "停课"
            Occupation.Outdoor -> "停工"
            else -> "停工停学"
        }
        return PersonalizedAdvice(
            title = "极端天气",
            detail = "${region.shortName} 已触发「${alert.title}」。请减少非必要外出，并及时查看属地官方发布的 $affectedGroup 通知。",
            icon = Icons.Filled.Warning,
            level = AdviceLevel.Warning,
        )
    }

    private fun allergenAdvice(profile: UserProfile, weather: FusedWeather): PersonalizedAdvice? {
        val airborneAllergens = profile.allergens.filter { it == Allergen.Pollen || it == Allergen.Spore }
        if (airborneAllergens.isEmpty()) return null
        val watched = airborneAllergens.joinToString("、") { it.label }
        val risk = weather.pollenLevel >= 3 || weather.sporeLevel >= 3
        return PersonalizedAdvice(
            title = "过敏原",
            detail = if (risk) "你关注 $watched，今天花粉/孢子风险偏高，建议外出后清洗面部和外套。" else "你关注 $watched，当前过敏原风险不高，持续关注空气质量即可。",
            icon = Icons.Filled.HealthAndSafety,
            level = if (risk) AdviceLevel.Caution else AdviceLevel.Info,
        )
    }
}

