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

internal fun oneDecimal(value: Double): String = "${(value * 10).roundToInt() / 10.0}"

internal fun formatRefreshAge(updatedAtMillis: Long, nowMillis: Long): String {
    val elapsedMinutes = ((nowMillis - updatedAtMillis).coerceAtLeast(0L) / 60_000L).toInt()
    return when {
        elapsedMinutes < 1 -> "上次刷新 · 刚刚"
        elapsedMinutes < 60 -> "上次刷新 · ${elapsedMinutes}分钟前"
        elapsedMinutes < 24 * 60 -> "上次刷新 · ${elapsedMinutes / 60}小时前"
        else -> "上次刷新 · ${elapsedMinutes / (24 * 60)}天前"
    }
}

internal fun windDirectionLabel(direction: Double): String {
    val labels = listOf("北风", "东北风", "东风", "东南风", "南风", "西南风", "西风", "西北风")
    val normalized = ((direction % 360.0) + 360.0) % 360.0
    return labels[((normalized + 22.5) / 45.0).toInt() % labels.size]
}

internal fun formatCoordinate(value: Double): String = String.format(Locale.US, "%.5f", value)

internal fun encodeUrlParameter(value: String): String = URLEncoder.encode(value, Charsets.UTF_8.name())

internal fun parseAmapWeather(body: String): WeatherReading? {
    val root = JSONObject(body)
    if (root.optString("status") != "1") return null
    val live = root.optJSONArray("lives")?.optJSONObject(0) ?: return null
    return WeatherReading(
        source = WeatherSource(SourceId.Amap, "高德天气", "国内 · 实时接口"),
        temperatureC = live.firstValueDouble("temperature"),
        feelsLikeC = live.firstValueDouble("temperature"),
        rainProbability = null,
        rainNextHourMm = null,
        windKph = amapWindPowerToKph(live.optCleanString("windpower")),
        aqi = null,
        pm25 = null,
        uvIndex = null,
        humidityPercent = live.firstValueDouble("humidity")?.roundToInt(),
        pollenLevel = null,
        sporeLevel = null,
        condition = xiaomiWeatherCondition(live.firstValueString("weather")),
        alert = null,
    )
}

internal fun amapWindPowerToKph(value: String): Double? {
    val level = Regex("[0-9]+").find(value)?.value?.toIntOrNull() ?: return null
    return when (level) {
        0 -> 1.0
        1 -> 4.0
        2 -> 9.0
        3 -> 15.0
        4 -> 23.0
        5 -> 32.0
        6 -> 43.0
        7 -> 55.0
        8 -> 68.0
        else -> 80.0
    }
}

internal fun parseMsnWeather(body: String): WeatherReading? {
    val response = JSONObject(body).optJSONArray("responses")?.optJSONObject(0) ?: return null
    val weather = response.optJSONArray("weather")?.optJSONObject(0) ?: return null
    val current = weather.optJSONObject("current") ?: return null
    val forecastDays = weather.optJSONObject("forecast")?.optJSONArray("days")
    val currentTime = parseIsoTime(current.optCleanString("created")) ?: System.currentTimeMillis()
    val currentDay = current.optCleanString("created").take(10).replace("-", "")
        .ifBlank { SimpleDateFormat("yyyyMMdd", Locale.US).format(Date(currentTime)) }
    val dailyForecast = mutableListOf<DailyForecast>()
    val hourlyForecast = mutableListOf<HourlyForecast>()
    var astronomy = AstronomyInfo.Empty

    if (forecastDays != null) {
        for (index in 0 until forecastDays.length()) {
            val dayContainer = forecastDays.optJSONObject(index) ?: continue
            val daily = dayContainer.optJSONObject("daily")
            val almanac = dayContainer.optJSONObject("almanac")
            val valid = daily?.optCleanString("valid").orEmpty()
            val dayTime = parseIsoTime(valid)
            if (daily != null && dayTime != null) {
                val high = daily.firstValueDouble("tempHi") ?: continue
                val low = daily.firstValueDouble("tempLo") ?: continue
                val dayCondition = daily.optJSONObject("day")?.firstValueString("cap", "summary")
                    .orEmpty().ifBlank { daily.firstValueString("pvdrCap") }
                dailyForecast += DailyForecast(
                    timeMillis = dayTime,
                    highC = high,
                    lowC = low,
                    condition = xiaomiWeatherCondition(dayCondition) ?: WeatherCondition.Cloudy,
                    rainProbability = daily.firstValueDouble("precip"),
                    aqi = daily.firstValueDouble("aqi")?.roundToInt(),
                    windKph = daily.firstValueDouble("windMax"),
                    sunrise = formatClock(almanac?.optCleanString("sunrise").orEmpty()),
                    sunset = formatClock(almanac?.optCleanString("sunset").orEmpty()),
                    isYesterday = valid.take(10).replace("-", "") < currentDay,
                )
                if (valid.take(10).replace("-", "") == currentDay) {
                    astronomy = AstronomyInfo(
                        sunrise = formatClock(almanac?.optCleanString("sunrise").orEmpty()),
                        sunset = formatClock(almanac?.optCleanString("sunset").orEmpty()),
                        moonrise = formatClock(almanac?.optCleanString("moonrise").orEmpty()),
                        moonset = formatClock(almanac?.optCleanString("moonset").orEmpty()),
                        moonPhase = almanac?.firstValueString("moonPhase")?.takeIf { it.isNotBlank() },
                    )
                }
            }
            val hours = dayContainer.optJSONArray("hourly") ?: continue
            for (hourIndex in 0 until hours.length()) {
                val hour = hours.optJSONObject(hourIndex) ?: continue
                val time = parseIsoTime(hour.optCleanString("valid")) ?: continue
                val temperature = hour.firstValueDouble("temp") ?: continue
                hourlyForecast += HourlyForecast(
                    timeMillis = time,
                    temperatureC = temperature,
                    condition = xiaomiWeatherCondition(hour.firstValueString("cap", "summary")) ?: WeatherCondition.Cloudy,
                    rainProbability = hour.firstValueDouble("precip"),
                    aqi = hour.firstValueDouble("aqi")?.roundToInt(),
                    windKph = hour.firstValueDouble("windSpd"),
                    windDirection = hour.firstValueDouble("windDir"),
                )
            }
        }
    }
    if (astronomy == AstronomyInfo.Empty) {
        val today = dailyForecast.firstOrNull { !it.isYesterday }
        astronomy = buildAstronomyInfo(today)
    }
    val firstHour = hourlyForecast.firstOrNull()
    val rainNextHour = weather.optJSONObject("nowcasting")
        ?.optJSONArray("precipitationAccumulation")
        ?.optDouble(0)
    return WeatherReading(
        source = WeatherSource(SourceId.MsnWeather, "MSN 天气", "全球 · 实时与预报"),
        temperatureC = current.firstValueDouble("temp"),
        feelsLikeC = current.firstValueDouble("feels", "utci"),
        rainProbability = firstHour?.rainProbability,
        rainNextHourMm = rainNextHour,
        windKph = current.firstValueDouble("windSpd"),
        aqi = current.firstValueDouble("aqi")?.roundToInt(),
        pm25 = null,
        uvIndex = current.firstValueDouble("uv"),
        humidityPercent = current.firstValueDouble("rh")?.roundToInt(),
        pollenLevel = null,
        sporeLevel = null,
        condition = xiaomiWeatherCondition(current.firstValueString("cap", "pvdrCap"), rainNextHour),
        alert = parseMsnAlert(weather.optJSONArray("alerts")),
        dailyForecast = dailyForecast.distinctBy { it.timeMillis },
        hourlyForecast = hourlyForecast.distinctBy { it.timeMillis }.take(48),
        astronomy = astronomy,
        pressureHpa = current.firstValueDouble("baro", "pressure", "pressureMeanSeaLevel"),
        dewPointC = current.firstValueDouble("dewPt", "dewPoint", "dewpoint"),
    )
}

internal fun parseMsnAlert(alerts: JSONArray?): WeatherAlert? {
    val alert = alerts?.optJSONObject(0) ?: return null
    val title = alert.firstValueString("title", "event", "cap")
    if (title.isBlank()) return null
    val detail = alert.firstValueString("description", "summary", "detail")
    val level = if (title.contains("暴") || title.contains("警") || title.contains("severe", true)) AlertLevel.Severe else AlertLevel.Rain
    return WeatherAlert(level, title, detail.ifBlank { "MSN 天气发布了新的天气提醒。" })
}

internal fun parseOpenWeather(body: String): WeatherReading? {
    val root = JSONObject(body)
    if (root.optInt("cod", 200) != 200) return null
    val main = root.optJSONObject("main") ?: return null
    val wind = root.optJSONObject("wind")
    val rain = root.optJSONObject("rain")
    val sys = root.optJSONObject("sys")
    val timezoneOffset = root.optInt("timezone", 0)
    val sunrise = sys?.optLong("sunrise", 0L)?.takeIf { it > 0 }?.let { formatUnixClock(it, timezoneOffset) }
    val sunset = sys?.optLong("sunset", 0L)?.takeIf { it > 0 }?.let { formatUnixClock(it, timezoneOffset) }
    val conditionText = root.optJSONArray("weather")?.optJSONObject(0)?.firstValueString("description", "main").orEmpty()
    return WeatherReading(
        source = WeatherSource(SourceId.OpenWeather, "OpenWeather", "全球 · 实时接口"),
        temperatureC = main.firstValueDouble("temp"),
        feelsLikeC = main.firstValueDouble("feels_like"),
        rainProbability = null,
        rainNextHourMm = rain?.firstValueDouble("1h"),
        windKph = wind?.firstValueDouble("speed")?.times(3.6),
        aqi = null,
        pm25 = null,
        uvIndex = null,
        humidityPercent = main.firstValueDouble("humidity")?.roundToInt(),
        pollenLevel = null,
        sporeLevel = null,
        condition = xiaomiWeatherCondition(conditionText, rain?.firstValueDouble("1h")),
        alert = null,
        astronomy = AstronomyInfo(sunrise, sunset, null, null),
        pressureHpa = main.firstValueDouble("pressure"),
    )
}

internal fun parseOpenMeteo(body: String): WeatherReading? {
    val root = JSONObject(body)
    val current = root.optJSONObject("current")
    val daily = root.optJSONObject("daily")
    val hourly = root.optJSONObject("hourly")
    if (current == null && daily == null && hourly == null) return null

    val timeZone = TimeZone.getTimeZone(root.optCleanString("timezone").ifBlank { "UTC" })
    val nowMillis = System.currentTimeMillis()
    val dayFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { this.timeZone = timeZone }
    val todayKey = dayFormatter.format(Date(nowMillis))

    val dailyTimes = daily?.optJSONArray("time")
    val dailyCodes = daily?.optJSONArray("weather_code")
    val dailyHigh = daily?.optJSONArray("temperature_2m_max")
    val dailyLow = daily?.optJSONArray("temperature_2m_min")
    val dailyRainProbability = daily?.optJSONArray("precipitation_probability_max")
    val dailyRainSum = daily?.optJSONArray("precipitation_sum")
    val dailyWind = daily?.optJSONArray("wind_speed_10m_max")
    val dailySunrise = daily?.optJSONArray("sunrise")
    val dailySunset = daily?.optJSONArray("sunset")
    val dailyForecast = mutableListOf<DailyForecast>()
    val dailyCount = listOf(
        dailyTimes?.length() ?: 0,
        dailyHigh?.length() ?: 0,
        dailyLow?.length() ?: 0,
    ).maxOrNull() ?: 0
    repeat(min(10, dailyCount)) { index ->
        val dayKey = dailyTimes?.optCleanString(index).orEmpty()
        val time = parseOpenMeteoTime(dayKey, timeZone) ?: return@repeat
        val high = dailyHigh?.optDoubleOrNull(index) ?: return@repeat
        val low = dailyLow?.optDoubleOrNull(index) ?: return@repeat
        val rainSum = dailyRainSum?.optDoubleOrNull(index)
        dailyForecast += DailyForecast(
            timeMillis = time,
            highC = high,
            lowC = low,
            condition = openMeteoWeatherCondition(dailyCodes?.optDoubleOrNull(index)?.roundToInt(), rainSum)
                ?: WeatherCondition.Cloudy,
            rainProbability = dailyRainProbability?.optDoubleOrNull(index),
            aqi = null,
            windKph = dailyWind?.optDoubleOrNull(index),
            sunrise = formatClock(dailySunrise?.optCleanString(index).orEmpty()),
            sunset = formatClock(dailySunset?.optCleanString(index).orEmpty()),
            isYesterday = dayKey.isNotBlank() && dayKey < todayKey,
        )
    }

    val hourlyTimes = hourly?.optJSONArray("time")
    val hourlyTemperature = hourly?.optJSONArray("temperature_2m")
    val hourlyCodes = hourly?.optJSONArray("weather_code")
    val hourlyRainProbability = hourly?.optJSONArray("precipitation_probability")
    val hourlyPrecipitation = hourly?.optJSONArray("precipitation")
    val hourlyRain = hourly?.optJSONArray("rain")
    val hourlyWind = hourly?.optJSONArray("wind_speed_10m")
    val hourlyWindDirection = hourly?.optJSONArray("wind_direction_10m")
    val hourlyHumidity = hourly?.optJSONArray("relative_humidity_2m")
    val hourlyUv = hourly?.optJSONArray("uv_index")
    val hourlyForecast = mutableListOf<HourlyForecast>()
    var nearestHourlyIndex: Int? = null
    var nearestHourlyDelta = Long.MAX_VALUE
    val hourlyCount = listOf(
        hourlyTimes?.length() ?: 0,
        hourlyTemperature?.length() ?: 0,
    ).maxOrNull() ?: 0
    repeat(min(72, hourlyCount)) { index ->
        val time = parseOpenMeteoTime(hourlyTimes?.optCleanString(index).orEmpty(), timeZone) ?: return@repeat
        val delta = abs(time - nowMillis)
        if (delta < nearestHourlyDelta) {
            nearestHourlyDelta = delta
            nearestHourlyIndex = index
        }
        if (time < nowMillis - TimeUnit.HOURS.toMillis(1)) return@repeat
        val temperature = hourlyTemperature?.optDoubleOrNull(index) ?: return@repeat
        val rainMm = hourlyRain?.optDoubleOrNull(index) ?: hourlyPrecipitation?.optDoubleOrNull(index)
        hourlyForecast += HourlyForecast(
            timeMillis = time,
            temperatureC = temperature,
            condition = openMeteoWeatherCondition(hourlyCodes?.optDoubleOrNull(index)?.roundToInt(), rainMm)
                ?: WeatherCondition.Cloudy,
            rainProbability = hourlyRainProbability?.optDoubleOrNull(index),
            aqi = null,
            windKph = hourlyWind?.optDoubleOrNull(index),
            windDirection = hourlyWindDirection?.optDoubleOrNull(index),
        )
    }

    val firstHour = hourlyForecast.firstOrNull()
    val today = dailyForecast.firstOrNull { !it.isYesterday }
    val currentRain = current?.firstValueDouble("rain") ?: current?.firstValueDouble("precipitation")
    val nearestIndex = nearestHourlyIndex
    return WeatherReading(
        source = WeatherSource(SourceId.OpenMeteo, "Open-Meteo", "全球 · 免费预报"),
        temperatureC = current?.firstValueDouble("temperature_2m") ?: firstHour?.temperatureC
            ?: today?.let { (it.highC + it.lowC) / 2.0 },
        feelsLikeC = current?.firstValueDouble("apparent_temperature"),
        rainProbability = firstHour?.rainProbability ?: today?.rainProbability,
        rainNextHourMm = currentRain,
        windKph = current?.firstValueDouble("wind_speed_10m") ?: firstHour?.windKph,
        aqi = null,
        pm25 = null,
        uvIndex = nearestIndex?.let { hourlyUv?.optDoubleOrNull(it) },
        humidityPercent = current?.firstValueDouble("relative_humidity_2m")?.roundToInt()
            ?: nearestIndex?.let { hourlyHumidity?.optDoubleOrNull(it)?.roundToInt() },
        pollenLevel = null,
        sporeLevel = null,
        condition = openMeteoWeatherCondition(current?.firstValueDouble("weather_code")?.roundToInt(), currentRain)
            ?: firstHour?.condition
            ?: today?.condition,
        alert = null,
        dailyForecast = dailyForecast.distinctBy { it.timeMillis },
        hourlyForecast = hourlyForecast.distinctBy { it.timeMillis }.take(48),
        astronomy = buildAstronomyInfo(today),
        pressureHpa = current?.firstValueDouble("pressure_msl"),
    )
}

internal fun parseOpenMeteoTime(value: String, timeZone: TimeZone): Long? {
    if (value.isBlank()) return null
    val patterns = if (value.contains('T')) {
        listOf("yyyy-MM-dd'T'HH:mm", "yyyy-MM-dd'T'HH:mm:ss")
    } else {
        listOf("yyyy-MM-dd")
    }
    return patterns.firstNotNullOfOrNull { pattern ->
        runCatching {
            SimpleDateFormat(pattern, Locale.US).apply { this.timeZone = timeZone }.parse(value)?.time
        }.getOrNull()
    }
}

internal fun openMeteoWeatherCondition(code: Int?, precipitationMm: Double? = null): WeatherCondition? = when (code) {
    null -> rainConditionForHourlyMm(precipitationMm)
    0 -> WeatherCondition.Sunny
    1, 2, 3 -> WeatherCondition.Cloudy
    45, 48 -> WeatherCondition.Fog
    51, 56, 61, 80 -> WeatherCondition.LightRain
    53, 55, 57, 63, 66, 81 -> WeatherCondition.ModerateRain
    65, 67, 82 -> WeatherCondition.HeavyRain
    71, 73, 75, 77, 85, 86 -> WeatherCondition.Snow
    95 -> WeatherCondition.ThunderShower
    96, 99 -> WeatherCondition.Hail
    else -> rainConditionForHourlyMm(precipitationMm) ?: WeatherCondition.Cloudy
}
internal fun parseVisualCrossing(body: String): WeatherReading? {
    val root = JSONObject(body)
    val current = root.optJSONObject("currentConditions")
    val days = root.optJSONArray("days")
    if (current == null && (days == null || days.length() == 0)) return null

    val nowMillis = System.currentTimeMillis()
    val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(nowMillis))
    val dailyForecast = mutableListOf<DailyForecast>()
    val hourlyForecast = mutableListOf<HourlyForecast>()

    if (days != null) {
        for (dayIndex in 0 until min(15, days.length())) {
            val day = days.optJSONObject(dayIndex) ?: continue
            val dayTime = day.optLong("datetimeEpoch", 0L)
                .takeIf { it > 0 }
                ?.times(1000L)
                ?: continue
            val high = day.firstValueDouble("tempmax") ?: continue
            val low = day.firstValueDouble("tempmin") ?: continue
            val dayKey = day.optCleanString("datetime")
            dailyForecast += DailyForecast(
                timeMillis = dayTime,
                highC = high,
                lowC = low,
                condition = xiaomiWeatherCondition(day.firstValueString("conditions", "icon"))
                    ?: WeatherCondition.Cloudy,
                rainProbability = day.firstValueDouble("precipprob"),
                aqi = null,
                windKph = day.firstValueDouble("windspeed"),
                sunrise = visualCrossingClock(day.optCleanString("sunrise")),
                sunset = visualCrossingClock(day.optCleanString("sunset")),
                isYesterday = dayKey.isNotBlank() && dayKey < todayKey,
            )

            val hours = day.optJSONArray("hours") ?: continue
            for (hourIndex in 0 until hours.length()) {
                val hour = hours.optJSONObject(hourIndex) ?: continue
                val hourTime = hour.optLong("datetimeEpoch", 0L)
                    .takeIf { it > 0 }
                    ?.times(1000L)
                    ?: continue
                if (hourTime < nowMillis - TimeUnit.HOURS.toMillis(1)) continue
                val temperature = hour.firstValueDouble("temp") ?: continue
                hourlyForecast += HourlyForecast(
                    timeMillis = hourTime,
                    temperatureC = temperature,
                    condition = xiaomiWeatherCondition(
                        hour.firstValueString("conditions", "icon"),
                        hour.firstValueDouble("precip"),
                    )
                        ?: WeatherCondition.Cloudy,
                    rainProbability = hour.firstValueDouble("precipprob"),
                    aqi = null,
                    windKph = hour.firstValueDouble("windspeed"),
                    windDirection = hour.firstValueDouble("winddir"),
                )
            }
        }
    }

    val today = dailyForecast.firstOrNull { !it.isYesterday }
    val astronomy = AstronomyInfo(
        sunrise = visualCrossingClock(current?.optCleanString("sunrise").orEmpty()) ?: today?.sunrise,
        sunset = visualCrossingClock(current?.optCleanString("sunset").orEmpty()) ?: today?.sunset,
        moonrise = null,
        moonset = null,
    )
    val currentCondition = current?.firstValueString("conditions", "icon").orEmpty()
    return WeatherReading(
        source = WeatherSource(SourceId.VisualCrossing, "Visual Crossing", "全球 · 实时与预报"),
        temperatureC = current?.firstValueDouble("temp") ?: today?.let { (it.highC + it.lowC) / 2.0 },
        feelsLikeC = current?.firstValueDouble("feelslike"),
        rainProbability = current?.firstValueDouble("precipprob"),
        rainNextHourMm = current?.firstValueDouble("precip"),
        windKph = current?.firstValueDouble("windspeed"),
        aqi = null,
        pm25 = null,
        uvIndex = current?.firstValueDouble("uvindex"),
        humidityPercent = current?.firstValueDouble("humidity")?.roundToInt(),
        pollenLevel = null,
        sporeLevel = null,
        condition = xiaomiWeatherCondition(currentCondition, current?.firstValueDouble("precip")) ?: today?.condition,
        alert = parseVisualCrossingAlert(root.optJSONArray("alerts")),
        dailyForecast = dailyForecast.distinctBy { it.timeMillis },
        hourlyForecast = hourlyForecast.distinctBy { it.timeMillis }.take(48),
        astronomy = astronomy,
        pressureHpa = current?.firstValueDouble("pressure", "sealevelpressure"),
        dewPointC = current?.firstValueDouble("dew", "dewpoint"),
    )
}

internal fun parseVisualCrossingAlert(alerts: JSONArray?): WeatherAlert? {
    val alert = alerts?.optJSONObject(0) ?: return null
    val title = alert.firstValueString("event", "headline")
    if (title.isBlank()) return null
    val severity = alert.firstValueString("severity")
    val level = if (
        severity.contains("severe", ignoreCase = true) ||
        severity.contains("extreme", ignoreCase = true)
    ) AlertLevel.Severe else AlertLevel.Rain
    val detail = alert.firstValueString("description", "headline")
    return WeatherAlert(level, title, detail.ifBlank { "Visual Crossing 发布了新的天气提醒。" })
}

internal fun visualCrossingClock(value: String): String? {
    val match = Regex("^([0-9]{1,2}):([0-9]{2})").find(value.trim()) ?: return null
    val hour = match.groupValues[1].toIntOrNull() ?: return null
    val minute = match.groupValues[2].toIntOrNull() ?: return null
    if (hour !in 0..23 || minute !in 0..59) return null
    return "%02d:%02d".format(Locale.CHINA, hour, minute)
}

internal fun parseMeteostat(body: String): WeatherReading? {
    val data = JSONObject(body).optJSONArray("data") ?: return null
    val latest = (data.length() - 1 downTo 0)
        .firstNotNullOfOrNull { index ->
            data.optJSONObject(index)?.takeIf { it.firstValueDouble("temp") != null }
        }
        ?: return null
    val conditionCode = latest.firstValueDouble("coco")?.roundToInt()
    return WeatherReading(
        source = WeatherSource(SourceId.Meteostat, "Meteostat API", "全球 · 历史观测"),
        temperatureC = latest.firstValueDouble("temp"),
        feelsLikeC = null,
        rainProbability = null,
        rainNextHourMm = latest.firstValueDouble("prcp"),
        windKph = latest.firstValueDouble("wspd"),
        aqi = null,
        pm25 = null,
        uvIndex = null,
        humidityPercent = latest.firstValueDouble("rhum")?.roundToInt(),
        pollenLevel = null,
        sporeLevel = null,
        condition = meteostatCondition(conditionCode, latest.firstValueDouble("prcp")),
        alert = null,
        pressureHpa = latest.firstValueDouble("pres"),
        dewPointC = latest.firstValueDouble("dwpt"),
    )
}

internal fun meteostatCondition(code: Int?, precipitationMm: Double? = null): WeatherCondition? = when (code) {
    null -> null
    1 -> WeatherCondition.Sunny
    2, 3, 4 -> WeatherCondition.Cloudy
    5, 6 -> WeatherCondition.Fog
    7 -> WeatherCondition.LightRain
    8 -> rainConditionForHourlyMm(precipitationMm) ?: WeatherCondition.Rain
    9 -> WeatherCondition.HeavyRain
    10, 11 -> WeatherCondition.FreezingRain
    12, 13, 19, 20 -> WeatherCondition.Sleet
    14, 15, 16, 21, 22 -> WeatherCondition.Snow
    17 -> WeatherCondition.Shower
    18 -> WeatherCondition.HeavyShower
    23, 25, 26 -> WeatherCondition.ThunderShower
    24 -> WeatherCondition.Hail
    27 -> WeatherCondition.Storm
    else -> null
}

internal fun parseQWeather(currentBody: String?, sunBody: String?, moonBody: String?): WeatherReading? {
    val currentRoot = currentBody?.let { runCatching { JSONObject(it) }.getOrNull() }
    val now = currentRoot?.takeIf { it.optString("code") == "200" }?.optJSONObject("now")
    val sunRoot = sunBody?.let { runCatching { JSONObject(it) }.getOrNull() }?.takeIf { it.optString("code") == "200" }
    val moonRoot = moonBody?.let { runCatching { JSONObject(it) }.getOrNull() }?.takeIf { it.optString("code") == "200" }
    val astronomy = AstronomyInfo(
        sunrise = formatClock(sunRoot?.optCleanString("sunrise").orEmpty()),
        sunset = formatClock(sunRoot?.optCleanString("sunset").orEmpty()),
        moonrise = formatClock(moonRoot?.optCleanString("moonrise").orEmpty()),
        moonset = formatClock(moonRoot?.optCleanString("moonset").orEmpty()),
        moonPhase = moonRoot?.optJSONArray("moonPhase")
            ?.optJSONObject(0)
            ?.firstValueString("name", "phase")
            ?.takeIf { it.isNotBlank() },
    )
    if (now == null && astronomy == AstronomyInfo.Empty) return null
    return WeatherReading(
        source = WeatherSource(SourceId.QWeather, "和风天气", "全球 · 实时与天文"),
        temperatureC = now?.firstValueDouble("temp"),
        feelsLikeC = now?.firstValueDouble("feelsLike"),
        rainProbability = null,
        rainNextHourMm = now?.firstValueDouble("precip"),
        windKph = now?.firstValueDouble("windSpeed"),
        aqi = null,
        pm25 = null,
        uvIndex = null,
        humidityPercent = now?.firstValueDouble("humidity")?.roundToInt(),
        pollenLevel = null,
        sporeLevel = null,
        condition = xiaomiWeatherCondition(
            now?.firstValueString("text", "icon").orEmpty(),
            now?.firstValueDouble("precip"),
        ),
        alert = null,
        astronomy = astronomy,
        pressureHpa = now?.firstValueDouble("pressure"),
        dewPointC = now?.firstValueDouble("dew"),
    )
}

internal fun parseSeniverseWeather(currentBody: String?, sunBody: String?): WeatherReading? {
    val currentResult = currentBody?.let { runCatching { JSONObject(it) }.getOrNull() }
        ?.optJSONArray("results")?.optJSONObject(0)
    val now = currentResult?.optJSONObject("now")
    val sunItem = sunBody?.let { runCatching { JSONObject(it) }.getOrNull() }
        ?.optJSONArray("results")?.optJSONObject(0)
        ?.optJSONArray("sun")?.optJSONObject(0)
    val astronomy = AstronomyInfo(
        sunrise = sunItem?.optCleanString("sunrise")?.takeIf { it.isNotBlank() },
        sunset = sunItem?.optCleanString("sunset")?.takeIf { it.isNotBlank() },
        moonrise = null,
        moonset = null,
    )
    if (now == null && astronomy.sunrise == null && astronomy.sunset == null) return null
    return WeatherReading(
        source = WeatherSource(SourceId.Seniverse, "心知天气", "全球 · 实时与日照"),
        temperatureC = now?.firstValueDouble("temperature"),
        feelsLikeC = now?.firstValueDouble("feels_like") ?: now?.firstValueDouble("temperature"),
        rainProbability = null,
        rainNextHourMm = null,
        windKph = null,
        aqi = null,
        pm25 = null,
        uvIndex = null,
        humidityPercent = null,
        pollenLevel = null,
        sporeLevel = null,
        condition = xiaomiWeatherCondition(now?.firstValueString("text", "code").orEmpty()),
        alert = null,
        astronomy = astronomy,
        pressureHpa = now?.firstValueDouble("pressure"),
    )
}

internal fun parseNwsWeather(body: String): WeatherReading? {
    val periods = JSONObject(body).optJSONObject("properties")?.optJSONArray("periods") ?: return null
    if (periods.length() == 0) return null
    val hourly = buildList {
        for (index in 0 until min(48, periods.length())) {
            val period = periods.optJSONObject(index) ?: continue
            val temperature = period.firstValueDouble("temperature") ?: continue
            val celsius = if (period.optString("temperatureUnit").equals("F", true)) (temperature - 32.0) / 1.8 else temperature
            add(
                HourlyForecast(
                    timeMillis = parseIsoTime(period.optCleanString("startTime")) ?: continue,
                    temperatureC = celsius,
                    condition = xiaomiWeatherCondition(period.firstValueString("shortForecast")) ?: WeatherCondition.Cloudy,
                    rainProbability = period.optJSONObject("probabilityOfPrecipitation")?.firstValueDouble("value"),
                    aqi = null,
                    windKph = parseNwsWindKph(period.optCleanString("windSpeed")),
                    windDirection = null,
                )
            )
        }
    }
    val first = periods.optJSONObject(0) ?: return null
    val rawTemperature = first.firstValueDouble("temperature")
    val temperature = rawTemperature?.let { if (first.optString("temperatureUnit").equals("F", true)) (it - 32.0) / 1.8 else it }
    return WeatherReading(
        source = WeatherSource(SourceId.Nws, "NWS / weather.gov", "美国官方 · 小时预报"),
        temperatureC = temperature,
        feelsLikeC = temperature,
        rainProbability = first.optJSONObject("probabilityOfPrecipitation")?.firstValueDouble("value"),
        rainNextHourMm = null,
        windKph = parseNwsWindKph(first.optCleanString("windSpeed")),
        aqi = null,
        pm25 = null,
        uvIndex = null,
        humidityPercent = first.optJSONObject("relativeHumidity")?.firstValueDouble("value")?.roundToInt(),
        pollenLevel = null,
        sporeLevel = null,
        condition = xiaomiWeatherCondition(first.firstValueString("shortForecast")),
        alert = null,
        hourlyForecast = hourly,
    )
}

internal fun parseNwsWindKph(value: String): Double? {
    val mph = Regex("[0-9.]+").find(value)?.value?.toDoubleOrNull() ?: return null
    return mph * 1.609344
}

internal fun formatUnixClock(epochSeconds: Long, timezoneOffsetSeconds: Int): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.CHINA)
    formatter.timeZone = TimeZone.getTimeZone(String.format(Locale.US, "GMT%+03d:%02d", timezoneOffsetSeconds / 3600, abs(timezoneOffsetSeconds / 60) % 60))
    return formatter.format(Date(epochSeconds * 1000L))
}

internal fun parseXiaomiCitySearch(body: String): List<District> {
    val array = JSONArray(body)
    return buildList {
        for (index in 0 until array.length()) {
            parseXiaomiLocation(array.optJSONObject(index))?.let(::add)
        }
    }.distinctBy { region ->
        region.locationKey.ifBlank { "${region.displayName}:${region.latitude}:${region.longitude}" }
    }
}

internal fun parseXiaomiWeather(body: String): WeatherReading? {
    val root = JSONObject(body)
    if (root.has("status") && root.optInt("status", 0) != 0) return null

    val current = root.optJSONObject("current")
        ?: root.optJSONObject("realtime")
        ?: root.optJSONObject("now")
        ?: root
    val daily = root.optJSONObject("forecastDaily")
        ?: root.optJSONObject("daily")
        ?: root.optJSONObject("forecast")
    val hourly = root.optJSONObject("forecastHourly")
        ?: root.optJSONObject("hourly")

    val temperature = current.firstValueDouble("temperature", "temp", "tempCurrent")
    val feelsLike = current.firstValueDouble("feelsLike", "feels_like", "apparentTemperature")
        ?: temperature
    val rainProbability = current.firstValueDouble("precipitationProbability", "rainProbability", "pop")
        ?: hourly?.firstForecastDouble("precipitationProbability", "value", "probability", "pop")
        ?: daily?.firstForecastDouble("precipitationProbability", "value", "probability", "pop")
    val rainNextHour = current.firstValueDouble("rain", "rainfall", "precipitation")
        ?: hourly?.firstForecastDouble("precipitation", "value", "rain", "rainfall")
    val windKph = current.optJSONObject("wind")?.firstValueDouble("speed", "windSpeed")
        ?: current.firstValueDouble("windSpeed", "wind_kph", "windKph")
    val airQuality = current.optJSONObject("airQuality")
        ?: current.optJSONObject("aqi")
        ?: root.optJSONObject("airQuality")
        ?: root.optJSONObject("aqi")
    val aqi = airQuality?.firstValueDouble("aqi", "value", "index")?.roundToInt()
        ?: daily?.firstForecastDouble("aqi", "value", "avg", "max", "min")?.roundToInt()
    val pm25 = airQuality?.firstValueDouble("pm25", "pm2p5", "pm2_5")
        ?: daily?.firstForecastDouble("pm25", "value", "avg", "max", "min")
    val uvIndex = current.firstValueDouble("uvIndex", "uvi", "uv")
        ?: daily?.firstForecastDouble("uvIndex", "value", "avg", "max", "min")
    val humidity = current.firstValueDouble("humidity", "relativeHumidity")?.roundToInt()
    val pressure = current.firstValueDouble("pressure", "pressureSeaLevel", "pressure_hpa")
    val dewPoint = current.firstValueDouble("dewPoint", "dewpoint", "dew_point")
    val condition = xiaomiWeatherCondition(
        current.firstValueString("weather", "weatherCode", "condition"),
        rainNextHour,
    )
    val alert = parseXiaomiAlert(root, condition, rainProbability, rainNextHour)
    val dailyForecast = parseXiaomiDailyForecast(root)
    val hourlyForecast = parseXiaomiHourlyForecast(root)
    val astronomy = buildAstronomyInfo(dailyForecast.firstOrNull { !it.isYesterday })

    if (
        temperature == null &&
        feelsLike == null &&
        rainProbability == null &&
        rainNextHour == null &&
        windKph == null &&
        aqi == null &&
        pm25 == null &&
        uvIndex == null &&
        humidity == null &&
        condition == null
    ) {
        return null
    }

    return WeatherReading(
        source = WeatherSource(SourceId.XiaomiWeather, "小米天气", "国内 · 实况与15日预报"),
        temperatureC = temperature,
        feelsLikeC = feelsLike,
        rainProbability = rainProbability,
        rainNextHourMm = rainNextHour,
        windKph = windKph,
        aqi = aqi,
        pm25 = pm25,
        uvIndex = uvIndex,
        humidityPercent = humidity,
        pollenLevel = null,
        sporeLevel = null,
        condition = condition,
        alert = alert,
        dailyForecast = dailyForecast,
        hourlyForecast = hourlyForecast,
        astronomy = astronomy,
        pressureHpa = pressure,
        dewPointC = dewPoint,
    )
}

internal fun parseXiaomiDailyForecast(root: JSONObject): List<DailyForecast> {
    val result = mutableListOf<DailyForecast>()
    root.optJSONObject("yesterday")?.let { yesterday ->
        val time = parseIsoTime(yesterday.optCleanString("date"))
        val high = yesterday.optDoubleOrNull("tempMax")
        val low = yesterday.optDoubleOrNull("tempMin")
        if (time != null && high != null && low != null) {
            result += DailyForecast(
                timeMillis = time,
                highC = high,
                lowC = low,
                condition = xiaomiWeatherCondition(yesterday.optCleanString("weatherStart")) ?: WeatherCondition.Cloudy,
                rainProbability = null,
                aqi = yesterday.optCleanString("aqi").toDoubleOrNull()?.roundToInt(),
                windKph = yesterday.optCleanString("windSpeedStart").toDoubleOrNull(),
                sunrise = formatClock(yesterday.optCleanString("sunRise")),
                sunset = formatClock(yesterday.optCleanString("sunSet")),
                isYesterday = true,
            )
        }
    }

    val daily = root.optJSONObject("forecastDaily") ?: return result
    val temperatures = daily.optJSONObject("temperature")?.optJSONArray("value")
    val conditions = daily.optJSONObject("weather")?.optJSONArray("value")
    val rain = daily.optJSONObject("precipitationProbability")?.optJSONArray("value")
    val aqi = daily.optJSONObject("aqi")?.optJSONArray("value")
    val sun = daily.optJSONObject("sunRiseSet")?.optJSONArray("value")
    val wind = daily.optJSONObject("wind")?.optJSONObject("speed")?.optJSONArray("value")
    val count = listOf(temperatures?.length() ?: 0, conditions?.length() ?: 0, sun?.length() ?: 0).maxOrNull() ?: 0
    val baseTime = parseIsoTime(daily.optCleanString("pubTime")) ?: System.currentTimeMillis()

    repeat(count) { index ->
        val temperature = temperatures?.optJSONObject(index)
        val high = temperature?.firstDoubleOrNull("from", "max")
        val low = temperature?.firstDoubleOrNull("to", "min")
        if (high == null || low == null) return@repeat
        val weather = conditions?.optJSONObject(index)
        val sunItem = sun?.optJSONObject(index)
        val windItem = wind?.optJSONObject(index)
        result += DailyForecast(
            timeMillis = parseIsoTime(sunItem?.optCleanString("from").orEmpty())
                ?: baseTime + index * 86_400_000L,
            highC = high,
            lowC = low,
            condition = xiaomiWeatherCondition(weather?.firstCleanString("from", "to").orEmpty())
                ?: WeatherCondition.Cloudy,
            rainProbability = rain?.optString(index)?.toDoubleOrNull(),
            aqi = aqi?.optString(index)?.toDoubleOrNull()?.roundToInt(),
            windKph = windItem?.firstDoubleOrNull("from", "to"),
            sunrise = formatClock(sunItem?.optCleanString("from").orEmpty()),
            sunset = formatClock(sunItem?.optCleanString("to").orEmpty()),
        )
    }
    return result.distinctBy { it.timeMillis }
}

internal fun parseXiaomiHourlyForecast(root: JSONObject): List<HourlyForecast> {
    val hourly = root.optJSONObject("forecastHourly") ?: return emptyList()
    val temperatureObject = hourly.optJSONObject("temperature")
    val temperatures = temperatureObject?.optJSONArray("value")
    val conditions = hourly.optJSONObject("weather")?.optJSONArray("value")
    val rain = hourly.optJSONObject("precipitationProbability")?.optJSONArray("value")
    val aqi = hourly.optJSONObject("aqi")?.optJSONArray("value")
    val wind = hourly.optJSONObject("wind")?.optJSONArray("value")
    val count = listOf(temperatures?.length() ?: 0, conditions?.length() ?: 0, wind?.length() ?: 0).maxOrNull() ?: 0
    val baseTime = parseIsoTime(temperatureObject?.optCleanString("pubTime").orEmpty()) ?: System.currentTimeMillis()

    return buildList {
        repeat(count.coerceAtMost(48)) { index ->
            val temp = temperatures?.optString(index)?.toDoubleOrNull() ?: return@repeat
            val windItem = wind?.optJSONObject(index)
            add(
                HourlyForecast(
                    timeMillis = parseIsoTime(windItem?.optCleanString("datetime").orEmpty())
                        ?: baseTime + index * 3_600_000L,
                    temperatureC = temp,
                    condition = xiaomiWeatherCondition(conditions?.optString(index).orEmpty())
                        ?: WeatherCondition.Cloudy,
                    rainProbability = rain?.optString(index)?.toDoubleOrNull(),
                    aqi = aqi?.optString(index)?.toDoubleOrNull()?.roundToInt(),
                    windKph = windItem?.optDoubleOrNull("speed"),
                    windDirection = windItem?.optDoubleOrNull("direction"),
                )
            )
        }
    }
}

internal fun buildAstronomyInfo(today: DailyForecast?): AstronomyInfo {
    if (today == null) return AstronomyInfo.Empty
    return AstronomyInfo(
        sunrise = today.sunrise,
        sunset = today.sunset,
        moonrise = null,
        moonset = null,
    )
}

internal fun parseIsoTime(value: String): Long? {
    if (value.isBlank()) return null
    val patterns = listOf("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ssXXX", "yyyy-MM-dd'T'HH:mmXXX")
    return patterns.firstNotNullOfOrNull { pattern ->
        runCatching { SimpleDateFormat(pattern, Locale.US).parse(value)?.time }.getOrNull()
    }
}

internal fun formatClock(value: String): String? {
    Regex("(?:T|\\s)(\\d{2}:\\d{2})").find(value)?.groupValues?.getOrNull(1)?.let { return it }
    Regex("^(\\d{1,2}:\\d{2})").find(value)?.groupValues?.getOrNull(1)?.let { clock ->
        val parts = clock.split(":")
        return "%02d:%02d".format(Locale.US, parts[0].toInt(), parts[1].toInt())
    }
    val time = parseIsoTime(value) ?: return null
    return SimpleDateFormat("HH:mm", Locale.CHINA).format(Date(time))
}

internal fun clockToMinutes(value: String?): Int? {
    val parts = value?.split(":") ?: return null
    return parts.getOrNull(0)?.toIntOrNull()?.times(60)?.plus(parts.getOrNull(1)?.toIntOrNull() ?: return null)
}

internal fun isNightTime(timeMillis: Long, astronomy: AstronomyInfo, longitude: Double): Boolean {
    val utcClock = SimpleDateFormat("HH:mm", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date(timeMillis))
    val utcMinute = clockToMinutes(utcClock) ?: return false
    val approximateLocalOffsetMinutes = (longitude / 15.0 * 60.0).roundToInt()
    val minute = Math.floorMod(utcMinute + approximateLocalOffsetMinutes, 24 * 60)
    val sunrise = clockToMinutes(astronomy.sunrise) ?: 6 * 60
    val sunset = clockToMinutes(astronomy.sunset) ?: 18 * 60
    return minute < sunrise || minute >= sunset
}

internal fun JSONObject.firstForecastDouble(sectionName: String, vararg valueNames: String): Double? {
    val section = optJSONObject(sectionName) ?: return null
    section.firstValueDouble(*valueNames)?.let { return it }
    val first = section.optJSONArray("value")?.optJSONObject(0) ?: return null
    val min = first.firstValueDouble("min", "minimum")
    val max = first.firstValueDouble("max", "maximum")
    if (min != null && max != null) return (min + max) / 2.0
    return first.firstValueDouble(*valueNames)
}

internal fun JSONObject.firstValueDouble(vararg names: String): Double? {
    names.forEach { name ->
        optDoubleOrNull(name)?.let { return it }
        optJSONObject(name)?.let { child ->
            child.firstDoubleOrNull("value", "avg", "current", "now")?.let { return it }
        }
    }
    return null
}

internal fun JSONObject.firstValueString(vararg names: String): String {
    names.forEach { name ->
        optCleanString(name).trim().takeIf { it.isNotBlank() }?.let { return it }
        optJSONObject(name)?.firstCleanString("value", "code", "text", "name")?.let { value ->
            if (value.isNotBlank()) return value
        }
    }
    return ""
}

internal fun xiaomiWeatherCondition(value: String, precipitationMm: Double? = null): WeatherCondition? {
    val normalized = value.trim()
    if (normalized.isBlank()) return null
    val code = normalized.toIntOrNull()
    return when {
        code in setOf(0, 100, 150) -> WeatherCondition.Sunny
        code in setOf(1, 2, 101, 102, 103, 104, 151, 152, 153, 154) -> WeatherCondition.Cloudy
        code in setOf(3, 300, 350) -> WeatherCondition.Shower
        code in setOf(301, 351) -> WeatherCondition.HeavyShower
        code in setOf(4, 302, 303) -> WeatherCondition.ThunderShower
        code in setOf(5, 304) -> WeatherCondition.Hail
        code in setOf(6, 404, 405, 406, 456) -> WeatherCondition.Sleet
        code in setOf(7, 305, 309) -> WeatherCondition.LightRain
        code in setOf(8, 306, 314) -> WeatherCondition.ModerateRain
        code in setOf(9, 307, 315) -> WeatherCondition.HeavyRain
        code in setOf(10, 11, 12, 23, 24, 25, 308, 310, 311, 312, 316, 317, 318) -> WeatherCondition.Rainstorm
        code in setOf(13, 14, 15, 16, 17, 26, 27, 28, 400, 401, 402, 403, 407, 408, 409, 410, 457, 499) -> WeatherCondition.Snow
        code in setOf(19, 313) -> WeatherCondition.FreezingRain
        code in setOf(18, 32, 49, 57, 58, 500, 501, 509, 510, 514, 515) -> WeatherCondition.Fog
        code in setOf(53, 54, 55, 56, 502, 511, 512, 513) -> WeatherCondition.Haze
        code in setOf(29, 30, 503, 504) -> WeatherCondition.Dust
        code in setOf(20, 31, 507, 508) -> WeatherCondition.Sandstorm
        code == 21 -> WeatherCondition.ModerateRain
        code == 22 -> WeatherCondition.HeavyRain
        code == 399 -> rainConditionForHourlyMm(precipitationMm) ?: WeatherCondition.Rain
        normalized.contains("雨夹雪") || normalized.contains("雨雪") || normalized.contains("sleet", true) ||
            normalized.contains("rain and snow", true) || normalized.contains("wintry mix", true) -> WeatherCondition.Sleet
        normalized.contains("冻雨") || normalized.contains("freezing rain", true) -> WeatherCondition.FreezingRain
        normalized.contains("冰雹") || normalized.contains("hail", true) -> WeatherCondition.Hail
        normalized.contains("雷阵雨") || normalized.contains("雷雨") || normalized.contains("thunder", true) -> WeatherCondition.ThunderShower
        normalized.contains("暴雪") || normalized.contains("雪", true) || normalized.contains("snow", true) -> WeatherCondition.Snow
        normalized.contains("沙尘暴") || normalized.contains("尘暴") || normalized.contains("sandstorm", true) ||
            normalized.contains("duststorm", true) || normalized.contains("dust storm", true) -> WeatherCondition.Sandstorm
        normalized.contains("扬沙") || normalized.contains("浮尘") || normalized.contains("沙尘") ||
            normalized.contains("dust", true) || normalized.contains("sand", true) || normalized.contains("ash", true) -> WeatherCondition.Dust
        normalized.contains("霾") || normalized.contains("haze", true) || normalized.contains("smoke", true) -> WeatherCondition.Haze
        normalized.contains("雾") || normalized.contains("fog", true) || normalized.contains("mist", true) -> WeatherCondition.Fog
        normalized.contains("暴雨") || normalized.contains("大暴雨") || normalized.contains("特大暴雨") ||
            normalized.contains("torrential", true) || normalized.contains("downpour", true) ||
            normalized.contains("rainstorm", true) -> WeatherCondition.Rainstorm
        normalized.contains("大雨") || normalized.contains("heavy rain", true) -> WeatherCondition.HeavyRain
        normalized.contains("中雨") || normalized.contains("moderate rain", true) -> WeatherCondition.ModerateRain
        normalized.contains("小雨") || normalized.contains("light rain", true) || normalized.contains("drizzle", true) -> WeatherCondition.LightRain
        normalized.contains("强阵雨") || normalized.contains("heavy shower", true) -> WeatherCondition.HeavyShower
        normalized.contains("阵雨") || normalized.contains("shower", true) -> WeatherCondition.Shower
        normalized.contains("雨") || normalized.contains("rain", true) ->
            rainConditionForHourlyMm(precipitationMm) ?: WeatherCondition.Rain
        normalized.contains("强对流") || normalized.contains("storm", true) || normalized.contains("squall", true) -> WeatherCondition.Storm
        normalized.contains("云") || normalized.contains("阴") || normalized.contains("cloud", true) ||
            normalized.contains("overcast", true) -> WeatherCondition.Cloudy
        else -> WeatherCondition.Sunny
    }
}

internal fun parseXiaomiAlert(
    root: JSONObject,
    condition: WeatherCondition?,
    rainProbability: Double?,
    rainNextHour: Double?,
): WeatherAlert? {
    val alertJson = root.optJSONArray("alerts")?.optJSONObject(0)
        ?: root.optJSONArray("alert")?.optJSONObject(0)
        ?: root.optJSONObject("alert")
        ?: root.optJSONObject("warning")
    val title = alertJson?.firstValueString("title", "type", "name", "eventType").orEmpty()
    val detail = alertJson?.firstValueString("detail", "description", "content", "desc").orEmpty()
    val levelText = alertJson?.firstValueString("level", "severity", "signal", "color").orEmpty()
    val level = when {
        levelText.contains("红") || levelText.contains("橙") || levelText.contains("severe", ignoreCase = true) -> AlertLevel.Severe
        title.contains("暴") || title.contains("雷") -> AlertLevel.Severe
        title.contains("雨") -> AlertLevel.Rain
        else -> AlertLevel.None
    }
    if (title.isNotBlank()) {
        return WeatherAlert(
            level = level,
            title = title,
            detail = detail.ifBlank { "小米天气返回预警信息，请关注当地官方发布。" },
        )
    }
    return when {
        condition?.isSevereWeather == true -> WeatherAlert(AlertLevel.Severe, condition.label, "小米天气实况显示存在高影响天气。")
        rainNextHour != null && rainNextHour >= 6.0 -> WeatherAlert(AlertLevel.Rain, "短时降雨", "小米天气显示未来一小时降雨偏强。")
        rainProbability != null && rainProbability >= 65.0 -> WeatherAlert(AlertLevel.Rain, "降雨概率较高", "小米天气显示降雨概率较高。")
        else -> null
    }
}

internal fun parseXiaomiCityGeo(body: String): District? {
    val text = body.trim()
    return when {
        text.startsWith("[") -> parseXiaomiCitySearch(text).firstOrNull()
        text.startsWith("{") -> {
            val json = JSONObject(text)
            parseXiaomiLocation(json)
                ?: parseXiaomiLocation(json.optJSONObject("result"))
                ?: parseXiaomiLocation(json.optJSONObject("data"))
                ?: parseXiaomiLocation(json.optJSONObject("location"))
                ?: parseXiaomiLocation(json.optJSONObject("city"))
                ?: parseFirstXiaomiLocation(json.optJSONArray("result"))
                ?: parseFirstXiaomiLocation(json.optJSONArray("data"))
                ?: parseFirstXiaomiLocation(json.optJSONArray("locations"))
        }
        else -> null
    }
}

internal fun parseFirstXiaomiLocation(array: JSONArray?): District? {
    if (array == null) return null
    for (index in 0 until array.length()) {
        parseXiaomiLocation(array.optJSONObject(index))?.let { return it }
    }
    return null
}

internal fun parseXiaomiLocation(json: JSONObject?): District? {
    if (json == null) return null
    if (json.optInt("status", 0) != 0) return null

    val name = json.firstCleanString("name", "displayName", "district", "city")
    val latitude = json.firstDoubleOrNull("latitude", "lat") ?: return null
    val longitude = json.firstDoubleOrNull("longitude", "lon", "lng") ?: return null
    val rawLocationKey = json.firstCleanString("locationKey", "location_key", "key")
    val locationKey = rawLocationKey.removePrefix("weathercn:")

    val affiliation = json.optCleanString("affiliation")
    val affiliationParts = affiliation
        .split(Regex("[,，/]+"))
        .map { it.trim() }
        .filter { it.isNotBlank() }

    val countryCode = json.firstCleanString("countryCode", "country_code").uppercase(Locale.US).ifBlank {
        inferCountryCode(affiliation)
    }
    if (countryCode != "CN") {
        val province = json.firstCleanString("province", "adminArea", "adm1")
            .ifBlank { affiliationParts.firstOrNull().orEmpty() }
        return District(
            countryCode = countryCode,
            province = province,
            city = name,
            district = "",
            latitude = latitude,
            longitude = longitude,
            locationKey = locationKey,
        )
    }

    val domesticAffiliationParts = affiliationParts.filter { it != "中国" && !it.equals("China", true) }

    val province = json.firstCleanString("province", "adminArea", "adm1")
        .ifBlank { domesticAffiliationParts.getOrNull(1) ?: domesticAffiliationParts.firstOrNull().orEmpty() }
    val cityFromAffiliation = domesticAffiliationParts.firstOrNull().orEmpty()
    val cityFromJson = json.firstCleanString("city", "locality", "adm2")
    val city = when {
        cityFromJson.isNotBlank() -> cityFromJson
        cityFromAffiliation.isNotBlank() -> cityFromAffiliation
        province.endsWith("市") -> province
        name.endsWith("市") || name.endsWith("自治州") || name.endsWith("地区") -> name
        province.isNotBlank() -> province
        else -> name
    }
    val districtFromJson = json.firstCleanString("district", "county", "adm3")
    val district = when {
        districtFromJson.isNotBlank() && districtFromJson != city -> districtFromJson
        name.isNotBlank() && name != city -> name
        else -> ""
    }

    if (city.isBlank() && name.isBlank()) return null

    return District(
        countryCode = countryCode,
        province = province,
        city = city,
        district = district,
        latitude = latitude,
        longitude = longitude,
        locationKey = locationKey,
    )
}

internal fun inferCountryCode(affiliation: String): String = when {
    affiliation.contains("United States", true) || affiliation.contains("美国") -> "US"
    affiliation.contains("China", true) || affiliation.contains("中国") -> "CN"
    affiliation.contains("United Kingdom", true) || affiliation.contains("英国") -> "GB"
    affiliation.contains("Japan", true) || affiliation.contains("日本") -> "JP"
    affiliation.contains("Chile", true) || affiliation.contains("智利") -> "CL"
    affiliation.contains("Philippines", true) || affiliation.contains("菲律宾") -> "PH"
    affiliation.contains("Nicaragua", true) || affiliation.contains("尼加拉瓜") -> "NI"
    affiliation.contains("Spain", true) || affiliation.contains("西班牙") -> "ES"
    affiliation.contains("Cuba", true) || affiliation.contains("古巴") -> "CU"
    affiliation.contains("Brazil", true) || affiliation.contains("巴西") -> "BR"
    else -> "ZZ"
}

internal fun JSONArray.optCleanString(index: Int): String =
    if (index < 0 || index >= length() || isNull(index)) "" else optString(index).takeUnless { it.equals("null", ignoreCase = true) }.orEmpty()

internal fun JSONArray.optDoubleOrNull(index: Int): Double? {
    optCleanString(index).toDoubleOrNull()?.let { return it }
    if (index < 0 || index >= length() || isNull(index)) return null
    val value = optDouble(index, Double.NaN)
    return if (value.isNaN()) null else value
}
internal fun JSONObject.optCleanString(name: String): String =
    optString(name).takeUnless { it.equals("null", ignoreCase = true) }.orEmpty()

internal fun JSONObject.firstCleanString(vararg names: String): String {
    names.forEach { name ->
        optCleanString(name).trim().takeIf { it.isNotBlank() }?.let { return it }
    }
    return ""
}

internal fun JSONObject.optDoubleOrNull(name: String): Double? {
    optCleanString(name).toDoubleOrNull()?.let { return it }
    if (!has(name) || isNull(name)) return null
    val value = optDouble(name, Double.NaN)
    return if (value.isNaN()) null else value
}

internal fun JSONObject.firstDoubleOrNull(vararg names: String): Double? {
    names.forEach { name ->
        optDoubleOrNull(name)?.let { return it }
    }
    return null
}

