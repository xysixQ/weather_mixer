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

internal class WeatherRepository {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .build()
    private val sourceFailures = ConcurrentHashMap<SourceId, ApiSourceFailure>()

    val regions = listOf(
        District("CN", "北京市", "北京市", "海淀区", 39.9599, 116.2981),
        District("CN", "北京市", "北京市", "朝阳区", 39.9219, 116.4431),
        District("CN", "北京市", "北京市", "西城区", 39.9123, 116.3659),
        District("CN", "北京市", "北京市", "丰台区", 39.8584, 116.2867),
        District("CN", "北京市", "北京市", "通州区", 39.9025, 116.6564),
        District("CN", "上海市", "上海市", "浦东新区", 31.2304, 121.5444),
        District("CN", "广东省", "深圳市", "南山区", 22.5333, 113.9304),
        District("CN", "广东省", "广州市", "天河区", 23.1246, 113.3612),
        District("US", "New York", "New York City", "Manhattan", 40.7831, -73.9712),
        District("US", "California", "San Francisco", "Mission District", 37.7599, -122.4148),
        District("GB", "England", "London", "Westminster", 51.4975, -0.1357),
        District("JP", "Tokyo", "Tokyo", "Shinjuku", 35.6938, 139.7034),
    )

    val defaultRegion = District(
        countryCode = "",
        province = "",
        city = "",
        district = "选择地区",
        latitude = 0.0,
        longitude = 0.0,
    )

    fun buildSnapshot(region: District, profile: UserProfile?): WeatherSnapshot {
        val readings = if (region.isDomestic) domesticReadings(region) else internationalReadings(region)
        return buildSnapshotFromReadings(region, profile, readings)
    }

    fun rebuildSnapshotForProfile(
        snapshot: WeatherSnapshot,
        profile: UserProfile?,
    ): WeatherSnapshot = snapshot.copy(
        advice = buildAdvice(snapshot.region, profile, snapshot.fused),
    )

    suspend fun buildSnapshot(
        region: District,
        profile: UserProfile?,
        apiConfigs: List<WeatherApiConfig>,
        onXiaomiSnapshot: (WeatherSnapshot) -> Unit = {},
        onSourceFailures: (List<ApiSourceFailure>) -> Unit = {},
    ): WeatherSnapshot = coroutineScope {
        sourceFailures.clear()
        val vehicleRestriction = if (
            region.isDomestic &&
            profile?.commuteMode == CommuteMode.Car &&
            profile.vehicleRestrictionEnabled
        ) {
            LocalVehicleRestrictions.find(region.city)?.let(::VehicleRestriction)
        } else {
            null
        }
        val liveReadings = if (region.isDomestic) {
            val xiaomiReading = fetchXiaomiWeatherReading(region, apiConfigs)
            if (xiaomiReading != null) {
                onXiaomiSnapshot(
                    buildSnapshotFromReadings(
                        region = region,
                        profile = profile,
                        readings = listOf(xiaomiReading),
                        vehicleRestriction = vehicleRestriction,
                    )
                )
            }
            val amap = async { fetchAmapWeatherReading(region, apiConfigs) }
            val qWeather = async { fetchQWeatherReading(region, apiConfigs) }
            val seniverse = async { fetchSeniverseReading(region, apiConfigs) }
            val openWeather = async { fetchOpenWeatherReading(region, apiConfigs) }
            val openMeteo = async { fetchOpenMeteoReading(region, apiConfigs) }
            val msn = async { fetchMsnWeatherReading(region, apiConfigs) }
            listOfNotNull(
                xiaomiReading,
                amap.await(),
                qWeather.await(),
                seniverse.await(),
                openWeather.await(),
                openMeteo.await(),
                msn.await(),
            )
        } else {
            val qWeather = async { fetchQWeatherReading(region, apiConfigs) }
            val openWeather = async { fetchOpenWeatherReading(region, apiConfigs) }
            val openMeteo = async { fetchOpenMeteoReading(region, apiConfigs) }
            val msn = async { fetchMsnWeatherReading(region, apiConfigs) }
            val visualCrossing = async { fetchVisualCrossingReading(region, apiConfigs) }
            val meteostat = async { fetchMeteostatReading(region, apiConfigs) }
            val nws = if (region.countryCode == "US") async { fetchNwsWeatherReading(region, apiConfigs) } else null
            listOfNotNull(
                qWeather.await(),
                openWeather.await(),
                openMeteo.await(),
                msn.await(),
                visualCrossing.await(),
                meteostat.await(),
                nws?.await(),
            )
        }
        val readings = if (region.isDomestic) {
            domesticReadings(region, liveReadings)
        } else {
            internationalReadings(region, liveReadings)
        }
        buildSnapshotFromReadings(region, profile, readings, vehicleRestriction).also {
            onSourceFailures(sourceFailures.values.sortedBy(ApiSourceFailure::displayName))
        }
    }

    private fun buildSnapshotFromReadings(
        region: District,
        profile: UserProfile?,
        readings: List<WeatherReading>,
        vehicleRestriction: VehicleRestriction? = null,
    ): WeatherSnapshot {
        val fusion = WeatherFusionEngine.fuse(region, readings)
        val nowMillis = System.currentTimeMillis()
        val fusedDaily = WeatherFusionEngine.fuseDaily(region, readings)
        val fusedHourly = if (region.countryCode == "US") {
            readings.firstOrNull { it.source.id == SourceId.Nws && it.hourlyForecast.isNotEmpty() }?.hourlyForecast
                ?: readings.firstOrNull { it.hourlyForecast.isNotEmpty() }?.hourlyForecast.orEmpty()
        } else {
            readings.firstOrNull { it.hourlyForecast.isNotEmpty() }?.hourlyForecast.orEmpty()
        }
        val advice = buildAdvice(region, profile, fusion.weather, vehicleRestriction)
        return WeatherSnapshot(
            region = region,
            readings = readings,
            fused = fusion.weather,
            advice = advice,
            sourceWeights = fusion.sourceWeights,
            fusionSummary = fusion.summary,
            anomalyNotes = fusion.anomalyNotes,
            dailyForecast = fusedDaily.ifEmpty { fallbackDailyForecast(fusion.weather, nowMillis) },
            hourlyForecast = fusedHourly.ifEmpty { fallbackHourlyForecast(fusion.weather, nowMillis) },
            astronomy = mergeAstronomy(readings),
            updatedAtMillis = nowMillis,
        )
    }

    private fun fallbackDailyForecast(weather: FusedWeather, nowMillis: Long): List<DailyForecast> {
        val baseTemperature = weather.temperatureC
        return List(7) { index ->
            val trend = sin(index / 6.0 * Math.PI) * 1.4
            val high = max(baseTemperature, baseTemperature + 2.0 + trend)
            val low = min(baseTemperature, baseTemperature - 2.4 + trend * 0.55)
            DailyForecast(
                timeMillis = nowMillis + TimeUnit.DAYS.toMillis(index.toLong()),
                highC = high,
                lowC = low,
                condition = fallbackForecastCondition(weather, index),
                rainProbability = weather.rainProbability.coerceIn(0.0, 100.0),
                aqi = weather.aqi,
                windKph = weather.windKph.coerceAtLeast(0.0),
                sunrise = null,
                sunset = null,
            )
        }
    }

    private fun fallbackHourlyForecast(weather: FusedWeather, nowMillis: Long): List<HourlyForecast> {
        val hourMillis = TimeUnit.HOURS.toMillis(1)
        val firstHour = nowMillis - (nowMillis % hourMillis) + hourMillis
        return List(36) { index ->
            val hourAngle = index / 24.0 * Math.PI * 2.0 - Math.PI / 2.0
            HourlyForecast(
                timeMillis = firstHour + hourMillis * index,
                temperatureC = weather.temperatureC + sin(hourAngle) * 1.8,
                condition = fallbackForecastCondition(weather, index),
                rainProbability = weather.rainProbability.coerceIn(0.0, 100.0),
                aqi = weather.aqi,
                windKph = weather.windKph.coerceAtLeast(0.0),
                windDirection = null,
            )
        }
    }

    private fun fallbackForecastCondition(weather: FusedWeather, index: Int): WeatherCondition {
        val shortTermRain = index <= 3 && (weather.rainNextHourMm > 0.05 || weather.rainProbability >= 55.0)
        return if (shortTermRain) {
            rainConditionForHourlyMm(weather.rainNextHourMm) ?: weather.condition.takeIf { it.isRainLike } ?: WeatherCondition.Rain
        } else {
            weather.condition
        }
    }

    private fun buildAdvice(
        region: District,
        profile: UserProfile?,
        weather: FusedWeather,
        suppliedVehicleRestriction: VehicleRestriction? = null,
    ): List<PersonalizedAdvice> {
        val vehicleRestriction = suppliedVehicleRestriction ?: if (
            region.isDomestic &&
            profile?.commuteMode == CommuteMode.Car &&
            profile.vehicleRestrictionEnabled
        ) {
            LocalVehicleRestrictions.find(region.city)?.let(::VehicleRestriction)
        } else {
            null
        }
        val adviceProvider = AdviceProvider(
            aiApiKey = BuildConfig.AI_ADVICE_API_KEY,
            offlineEngine = OfflineAdviceEngine,
        )
        val weatherAdvice = adviceProvider.buildAdvice(
            profile = profile ?: DefaultUserProfile,
            region = region,
            weather = weather,
        )
        return vehicleRestriction?.let {
            listOf(
                PersonalizedAdvice(
                    title = "机动车限行",
                    detail = it.detail,
                    icon = Icons.Filled.DirectionsCar,
                    level = AdviceLevel.Warning,
                )
            ) + weatherAdvice
        } ?: weatherAdvice
    }

    private fun mergeAstronomy(readings: List<WeatherReading>): AstronomyInfo {
        val ordered = readings
            .filter { it.astronomy != AstronomyInfo.Empty }
            .sortedBy { astronomyPriority(it.source.id) }
        if (ordered.isEmpty()) return AstronomyInfo.Empty
        val moonOrdered = ordered.sortedBy {
            when (it.source.id) {
                SourceId.MsnWeather -> 0
                SourceId.QWeather -> 1
                else -> 10
            }
        }
        return AstronomyInfo(
            sunrise = ordered.firstNotNullOfOrNull { it.astronomy.sunrise },
            sunset = ordered.firstNotNullOfOrNull { it.astronomy.sunset },
            moonrise = moonOrdered.firstNotNullOfOrNull { it.astronomy.moonrise },
            moonset = moonOrdered.firstNotNullOfOrNull { it.astronomy.moonset },
            moonPhase = moonOrdered.firstNotNullOfOrNull { it.astronomy.moonPhase },
        )
    }

    private fun astronomyPriority(sourceId: SourceId): Int = when (sourceId) {
        SourceId.QWeather -> 0
        SourceId.Seniverse -> 1
        SourceId.OpenMeteo -> 2
        SourceId.OpenWeather -> 3
        SourceId.MsnWeather -> 4
        SourceId.XiaomiWeather -> 5
        else -> 10
    }

    suspend fun locateCurrentPosition(
        context: Context,
        apiConfigs: List<WeatherApiConfig>,
        method: LocationMethod,
    ): LocationLookupResult {
        return when (method) {
            LocationMethod.BaiduIp -> locateByBaiduIp(apiConfigs)
            LocationMethod.Device -> locateByDeviceLocation(context, apiConfigs)
        }
    }

    suspend fun searchCities(
        query: String,
        apiConfigs: List<WeatherApiConfig>,
    ): CitySearchResult {
        val keyword = query.trim()
        if (keyword.isBlank()) {
            return CitySearchResult(emptyList(), "请输入城市或区县名称。")
        }

        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.XiaomiCitySearch }
            ?: return CitySearchResult(emptyList(), "小米城市搜索配置不存在，请恢复默认数据源配置。")
        if (!config.enabled) {
            return CitySearchResult(emptyList(), "小米城市搜索已停用，请在天气源配置中启用。")
        }
        if (config.endpoint.isBlank()) {
            return CitySearchResult(emptyList(), "小米城市搜索 endpoint 为空，请恢复默认或手动填写。")
        }

        val encodedKeyword = encodeUrlParameter(keyword)
        val requestUrl = config.endpoint
            .replace("{query}", encodedKeyword)
            .replace("{name}", encodedKeyword)
            .replace("{locale}", "zh_CN")

        val body = withContext(Dispatchers.IO) {
            runCatching {
                val requestBuilder = Request.Builder().url(requestUrl)
                if (config.userAgent.isNotBlank()) {
                    requestBuilder.header("User-Agent", config.userAgent)
                }
                httpClient.newCall(requestBuilder.build()).execute().use { response ->
                    if (!response.isSuccessful) return@use null
                    response.body.string()
                }
            }.getOrNull()
        } ?: return CitySearchResult(emptyList(), "小米城市搜索请求失败，请检查网络或 endpoint。")

        return runCatching {
            val results = parseXiaomiCitySearch(body)
            CitySearchResult(
                regions = results,
                message = if (results.isEmpty()) "没有找到匹配城市。" else "找到 ${results.size} 个城市结果。",
            )
        }.getOrElse {
            CitySearchResult(emptyList(), "小米城市搜索返回数据无法解析。")
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun locateByDeviceLocation(
        context: Context,
        apiConfigs: List<WeatherApiConfig>,
    ): LocationLookupResult {
        if (!hasLocationPermission(context)) {
            return LocationLookupResult(null, "系统定位需要先授予 Android 定位权限。")
        }
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return LocationLookupResult(null, "系统定位服务不可用。")
        val currentLocation = locationManager.awaitFreshLocation()
            ?: locationManager.latestKnownLocation()
            ?: return LocationLookupResult(null, "系统定位失败：请检查定位开关、模拟器位置，或等待设备产生一次定位。")

        return resolveLocatedPosition(
            latitude = currentLocation.latitude,
            longitude = currentLocation.longitude,
            providerLabel = "系统定位",
            apiConfigs = apiConfigs,
        )
    }

    private suspend fun locateByBaiduIp(apiConfigs: List<WeatherApiConfig>): LocationLookupResult {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.BaiduIpLocation }
            ?: return LocationLookupResult(null, "百度 IP 定位配置不存在，请恢复默认数据源配置。")
        if (!config.enabled) {
            return LocationLookupResult(null, "百度 IP 定位已停用，请在天气源配置中启用。")
        }
        if (config.apiKey.isBlank()) {
            return LocationLookupResult(null, "百度 IP 定位需要百度地图开放平台 AK，请在天气源配置中填写。")
        }
        if (config.endpoint.isBlank()) {
            return LocationLookupResult(null, "百度 IP 定位 endpoint 为空，请恢复默认或手动填写。")
        }

        val requestUrl = config.endpoint
            .replace("{key}", config.apiKey.trim())
            .replace("{ak}", config.apiKey.trim())

        val body = withContext(Dispatchers.IO) {
            runCatching {
                val request = Request.Builder().url(requestUrl).build()
                httpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) return@use null
                    response.body.string()
                }
            }.getOrNull()
        } ?: return LocationLookupResult(null, "百度 IP 定位请求失败，请检查 AK、网络或 endpoint。")

        return try {
            val json = JSONObject(body)
            val status = json.optInt("status", -1)
            if (status != 0) {
                val message = json.optString("message").ifBlank { "status=$status" }
                return LocationLookupResult(null, "百度 IP 定位失败：$message")
            }
            val content = json.optJSONObject("content")
                ?: return LocationLookupResult(null, "百度 IP 定位返回缺少 content。")
            val point = content.optJSONObject("point")
                ?: return LocationLookupResult(null, "百度 IP 定位返回缺少坐标。")
            val longitude = point.optString("x").toDoubleOrNull()
            val latitude = point.optString("y").toDoubleOrNull()
            if (latitude == null || longitude == null) {
                return LocationLookupResult(null, "百度 IP 定位坐标解析失败。")
            }

            resolveLocatedPosition(
                latitude = latitude,
                longitude = longitude,
                providerLabel = "百度 IP 定位",
                apiConfigs = apiConfigs,
            )
        } catch (error: Exception) {
            LocationLookupResult(null, "百度 IP 定位返回数据无法解析。")
        }
    }

    private suspend fun resolveLocatedPosition(
        latitude: Double,
        longitude: Double,
        providerLabel: String,
        apiConfigs: List<WeatherApiConfig>,
    ): LocationLookupResult {
        val geoResult = resolveXiaomiCityByCoordinates(
            latitude = latitude,
            longitude = longitude,
            apiConfigs = apiConfigs,
        )
        val position = LocatedPosition(
            latitude = latitude,
            longitude = longitude,
            providerLabel = providerLabel,
            region = geoResult.region,
        )
        return LocationLookupResult(
            position = position,
            failureMessage = geoResult.message,
        )
    }

    private suspend fun resolveXiaomiCityByCoordinates(
        latitude: Double,
        longitude: Double,
        apiConfigs: List<WeatherApiConfig>,
    ): CityGeoResult {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.XiaomiCityGeo }
            ?: return CityGeoResult(null, "小米坐标反查配置不存在，请恢复默认数据源配置。")
        if (!config.enabled) {
            return CityGeoResult(null, "小米坐标反查已停用，请在天气源配置中启用。")
        }
        if (config.endpoint.isBlank()) {
            return CityGeoResult(null, "小米坐标反查 endpoint 为空，请恢复默认或手动填写。")
        }

        val latitudeText = formatCoordinate(latitude)
        val longitudeText = formatCoordinate(longitude)
        val requestUrl = config.endpoint
            .replace("{lat}", latitudeText)
            .replace("{latitude}", latitudeText)
            .replace("{lon}", longitudeText)
            .replace("{lng}", longitudeText)
            .replace("{longitude}", longitudeText)
            .replace("{locale}", "zh_CN")

        val body = withContext(Dispatchers.IO) {
            runCatching {
                val requestBuilder = Request.Builder().url(requestUrl)
                if (config.userAgent.isNotBlank()) {
                    requestBuilder.header("User-Agent", config.userAgent)
                }
                httpClient.newCall(requestBuilder.build()).execute().use { response ->
                    if (!response.isSuccessful) return@use null
                    response.body.string()
                }
            }.getOrNull()
        } ?: return CityGeoResult(null, "小米坐标反查请求失败，请检查网络或 endpoint。")

        return runCatching {
            val region = parseXiaomiCityGeo(body)
            CityGeoResult(
                region = region,
                message = when {
                    region == null -> "小米坐标反查没有返回可用城市。"
                    region.locationKey.isBlank() -> "小米坐标反查识别到 ${region.displayName}，但没有返回城市 key。"
                    else -> "小米坐标反查识别到 ${region.displayName}。"
                },
            )
        }.getOrElse {
            CityGeoResult(null, "小米坐标反查返回数据无法解析。")
        }
    }

    private suspend fun fetchXiaomiWeatherReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        if (!region.isDomestic) return null
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.XiaomiWeather } ?: return null
        if (!config.isReady) return null

        val resolvedRegion = if (region.locationKey.isBlank()) {
            resolveXiaomiCityByCoordinates(
                latitude = region.latitude,
                longitude = region.longitude,
                apiConfigs = apiConfigs,
            ).region ?: region
        } else {
            region
        }
        val locationKey = resolvedRegion.locationKey.removePrefix("weathercn:")
        if (locationKey.isBlank()) return null

        val latitudeText = formatCoordinate(resolvedRegion.latitude)
        val longitudeText = formatCoordinate(resolvedRegion.longitude)
        val requestUrl = config.endpoint
            .replace("{lat}", latitudeText)
            .replace("{latitude}", latitudeText)
            .replace("{lon}", longitudeText)
            .replace("{lng}", longitudeText)
            .replace("{longitude}", longitudeText)
            .replace("{locationKey}", encodeUrlParameter(locationKey))
            .replace("{locale}", "zh_CN")

        val body = requestBody(config, requestUrl) ?: return null

        return runCatching {
            parseXiaomiWeather(body)
        }.getOrNull()
    }

    private suspend fun fetchMsnWeatherReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.MsnWeather } ?: return null
        if (!config.isReady) return null
        val requestUrl = fillCoordinateTemplate(config.endpoint, region)
        val body = requestBody(config, requestUrl) ?: return null
        return runCatching { parseMsnWeather(body) }.getOrNull()
    }

    private suspend fun fetchAmapWeatherReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        if (!region.isDomestic) return null
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.Amap } ?: return null
        if (!config.isReady) return null
        val key = encodeUrlParameter(config.apiKey.trim())
        val coordinate = "${formatCoordinate(region.longitude)},${formatCoordinate(region.latitude)}"
        val geoUrl = "https://restapi.amap.com/v3/geocode/regeo?location=$coordinate&key=$key&extensions=base"
        val geoBody = requestBody(config, geoUrl) ?: return null
        val adcode = runCatching {
            JSONObject(geoBody).optJSONObject("regeocode")
                ?.optJSONObject("addressComponent")
                ?.optString("adcode")
        }.getOrNull().orEmpty()
        if (adcode.isBlank()) return null
        val weatherUrl = config.endpoint
            .replace("{key}", key)
            .replace("{adcode}", encodeUrlParameter(adcode))
            .replace("extensions=all", "extensions=base")
        val body = requestBody(config, weatherUrl) ?: return null
        return runCatching { parseAmapWeather(body) }.getOrNull()
    }

    private suspend fun fetchOpenWeatherReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.OpenWeather } ?: return null
        if (!config.isReady) return null
        val requestUrl = fillCoordinateTemplate(config.endpoint, region)
            .replace("{key}", encodeUrlParameter(config.apiKey.trim()))
        val body = requestBody(config, requestUrl) ?: return null
        return runCatching { parseOpenWeather(body) }.getOrNull()
    }

    private suspend fun fetchOpenMeteoReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.OpenMeteo } ?: return null
        if (!config.isReady) return null
        val requestUrl = fillCoordinateTemplate(config.endpoint, region)
        val body = requestBody(config, requestUrl) ?: return null
        return runCatching { parseOpenMeteo(body) }.getOrNull()
    }
    private suspend fun fetchQWeatherReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? = coroutineScope {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.QWeather } ?: return@coroutineScope null
        if (!config.isReady) return@coroutineScope null
        val apiHost = normalizeApiHost(config.apiHost) ?: return@coroutineScope null
        val coordinate = "${String.format(Locale.US, "%.2f", region.longitude)}%2C${String.format(Locale.US, "%.2f", region.latitude)}"
        val currentUrl = fillCoordinateTemplate(
            config.endpoint.replace("{lon},{lat}", coordinate),
            region,
        )
            .replace("{host}", apiHost)
            .replace("{key}", encodeUrlParameter(config.apiKey.trim()))
        val apiBase = "https://$apiHost"
        val authHeaders = mapOf("X-QW-Api-Key" to config.apiKey.trim())
        val date = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val sunUrl = "$apiBase/v7/astronomy/sun?location=$coordinate&date=$date"
        val moonUrl = "$apiBase/v7/astronomy/moon?location=$coordinate&date=$date"
        val current = async { requestBody(config, currentUrl, authHeaders) }
        val sun = async { requestBody(config, sunUrl, authHeaders) }
        val moon = async { requestBody(config, moonUrl, authHeaders) }
        runCatching { parseQWeather(current.await(), sun.await(), moon.await()) }.getOrNull()
    }

    private suspend fun fetchVisualCrossingReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.VisualCrossing } ?: return null
        if (!config.isReady) return null
        val requestUrl = fillCoordinateTemplate(config.endpoint, region)
            .replace("{key}", encodeUrlParameter(config.apiKey.trim()))
        val body = requestBody(config, requestUrl) ?: return null
        return runCatching { parseVisualCrossing(body) }.getOrNull()
    }

    private suspend fun fetchMeteostatReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.Meteostat } ?: return null
        if (!config.isReady) return null
        val endMillis = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        val startMillis = endMillis - TimeUnit.DAYS.toMillis(2)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val startDate = dateFormat.format(Date(startMillis))
        val endDate = dateFormat.format(Date(endMillis))
        val requestUrl = fillCoordinateTemplate(config.endpoint, region)
            .replace("{start}", startDate)
            .replace("{end}", endDate)
        val headers = mapOf(
            "X-RapidAPI-Key" to config.apiKey.trim(),
            "X-RapidAPI-Host" to "meteostat.p.rapidapi.com",
        )
        val pointResult = requestBodyResult(config, requestUrl, headers, recordFailure = false)
        pointResult.body?.let { body ->
            runCatching { parseMeteostat(body) }.getOrNull()?.let { return it }
        }
        val stationResult = fetchMeteostatStationHourly(
            config = config,
            region = region,
            headers = headers,
            startDate = startDate,
            endDate = endDate,
        )
        stationResult.reading?.let { return it }
        val reason = listOfNotNull(
            pointResult.failureReason?.let { "point/hourly：$it" },
            stationResult.failureReason?.let { "station/hourly：$it" },
        ).joinToString("；").ifBlank { "point/hourly 与 station/hourly 都没有有效观测数据" }
        recordSourceFailure(config, reason)
        return null
    }

    private suspend fun fetchMeteostatStationHourly(
        config: WeatherApiConfig,
        region: District,
        headers: Map<String, String>,
        startDate: String,
        endDate: String,
    ): MeteostatAttempt {
        val baseUrl = meteostatApiBase(config.endpoint)
        val nearbyUrl = "$baseUrl/stations/nearby?lat=${formatCoordinate(region.latitude)}&lon=${formatCoordinate(region.longitude)}&limit=1"
        val nearbyResult = requestBodyResult(config, nearbyUrl, headers, recordFailure = false)
        val stationId = nearbyResult.body?.let(::parseMeteostatStationId).orEmpty()
        if (stationId.isBlank()) {
            return MeteostatAttempt(
                reading = null,
                failureReason = nearbyResult.failureReason ?: "未找到附近观测站",
            )
        }
        val hourlyUrl = "$baseUrl/stations/hourly?station=${encodeUrlParameter(stationId)}&start=$startDate&end=$endDate"
        val hourlyResult = requestBodyResult(config, hourlyUrl, headers, recordFailure = false)
        val reading = hourlyResult.body?.let { body -> runCatching { parseMeteostat(body) }.getOrNull() }
        return if (reading != null) {
            MeteostatAttempt(reading = reading, failureReason = null)
        } else {
            MeteostatAttempt(
                reading = null,
                failureReason = hourlyResult.failureReason ?: "观测站 $stationId 未返回小时数据",
            )
        }
    }

    private suspend fun fetchSeniverseReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        if (!region.isDomestic) return null
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.Seniverse } ?: return null
        if (!config.isReady) return null
        val key = encodeUrlParameter(config.apiKey.trim())
        val location = encodeUrlParameter(region.city.ifBlank { "${region.latitude}:${region.longitude}" })
        val currentUrl = config.endpoint
            .replace("{key}", key)
            .replace("{city}", location)
            .replace("{lat}", formatCoordinate(region.latitude))
            .replace("{lon}", formatCoordinate(region.longitude))
        val current = requestBody(config, currentUrl)
        return runCatching { parseSeniverseWeather(current, null) }.getOrNull()
    }

    private suspend fun fetchNwsWeatherReading(
        region: District,
        apiConfigs: List<WeatherApiConfig>,
    ): WeatherReading? {
        val config = apiConfigs.firstOrNull { it.sourceId == SourceId.Nws } ?: return null
        if (config.endpoint.isBlank()) return null
        val pointsUrl = fillCoordinateTemplate(config.endpoint, region)
        val pointsBody = requestBody(config.copy(enabled = true), pointsUrl) ?: return null
        val hourlyUrl = runCatching {
            JSONObject(pointsBody).optJSONObject("properties")?.optString("forecastHourly")
        }.getOrNull().orEmpty()
        if (hourlyUrl.isBlank()) return null
        val hourlyBody = requestBody(config.copy(enabled = true), hourlyUrl) ?: return null
        return runCatching { parseNwsWeather(hourlyBody) }.getOrNull()
    }

    private data class MeteostatAttempt(
        val reading: WeatherReading?,
        val failureReason: String?,
    )

    private data class ApiBodyResult(
        val body: String?,
        val failureReason: String?,
    )

    private fun meteostatApiBase(endpoint: String): String {
        val normalized = endpoint.substringBefore('?').trimEnd('/')
        return when {
            "/point/hourly" in normalized -> normalized.substringBefore("/point/hourly")
            "/stations/hourly" in normalized -> normalized.substringBefore("/stations/hourly")
            else -> "https://meteostat.p.rapidapi.com"
        }.trimEnd('/')
    }

    private fun parseMeteostatStationId(body: String): String? {
        val data = runCatching { JSONObject(body).optJSONArray("data") }.getOrNull() ?: return null
        for (index in 0 until data.length()) {
            val station = data.optJSONObject(index) ?: continue
            station.firstValueString("id", "station").takeIf(String::isNotBlank)?.let { return it }
        }
        return null
    }

    private fun fillCoordinateTemplate(endpoint: String, region: District): String = endpoint
        .replace("{lat}", formatCoordinate(region.latitude))
        .replace("{latitude}", formatCoordinate(region.latitude))
        .replace("{lon}", formatCoordinate(region.longitude))
        .replace("{lng}", formatCoordinate(region.longitude))
        .replace("{longitude}", formatCoordinate(region.longitude))
        .replace("{city}", encodeUrlParameter(region.city))

    private fun normalizeApiHost(value: String): String? = value
        .trim()
        .removePrefix("https://")
        .removePrefix("http://")
        .trimEnd('/')
        .takeIf { it.isNotBlank() && '/' !in it }

    private suspend fun requestBody(
        config: WeatherApiConfig,
        requestUrl: String,
        headers: Map<String, String> = emptyMap(),
    ): String? = requestBodyResult(config, requestUrl, headers, recordFailure = true).body

    private suspend fun requestBodyResult(
        config: WeatherApiConfig,
        requestUrl: String,
        headers: Map<String, String> = emptyMap(),
        recordFailure: Boolean,
    ): ApiBodyResult = withContext(Dispatchers.IO) {
        runCatching {
            val requestBuilder = Request.Builder().url(requestUrl)
            if (config.userAgent.isNotBlank()) requestBuilder.header("User-Agent", config.userAgent)
            headers.forEach { (name, value) -> requestBuilder.header(name, value) }
            httpClient.newCall(requestBuilder.build()).execute().use { response ->
                val body = response.body.string()
                val apiError = detectApiError(config, body)
                if (!response.isSuccessful) {
                    val reason = apiError ?: "HTTP ${response.code}"
                    if (recordFailure) recordSourceFailure(config, reason)
                    return@use ApiBodyResult(body = null, failureReason = reason)
                }
                apiError?.let { reason ->
                    if (recordFailure) recordSourceFailure(config, reason)
                    return@use ApiBodyResult(body = null, failureReason = reason)
                }
                ApiBodyResult(body = body, failureReason = null)
            }
        }.getOrElse { error ->
            ApiBodyResult(body = null, failureReason = error.message?.takeIf(String::isNotBlank))
        }
    }

    private fun recordSourceFailure(config: WeatherApiConfig, reason: String) {
        sourceFailures.putIfAbsent(
            config.sourceId,
            ApiSourceFailure(config.sourceId, config.displayName, reason),
        )
    }

    private fun detectApiError(config: WeatherApiConfig, body: String): String? {
        val payload = runCatching { JSONObject(body) }.getOrNull() ?: return null
        fun cleanValue(name: String): String? = payload.opt(name)
            ?.takeUnless { it == JSONObject.NULL }
            ?.toString()
            ?.trim()
            ?.takeIf(String::isNotBlank)
        fun errorReason(code: String): String = "错误码 $code"

        if (payload.optBoolean("error", false)) {
            return cleanValue("reason") ?: cleanValue("message") ?: "接口返回错误"
        }
        cleanValue("error_code")?.let { code ->
            if (code !in setOf("0", "200")) return errorReason(code)
        }
        cleanValue("errorCode")?.let { code ->
            if (code !in setOf("0", "200")) return errorReason(code)
        }
        return when (config.sourceId) {
            SourceId.Amap -> cleanValue("status")
                ?.takeUnless { it == "1" }
                ?.let { errorReason(cleanValue("infocode") ?: it) }
            SourceId.QWeather -> cleanValue("code")
                ?.takeUnless { it == "200" }
                ?.let(::errorReason)
            SourceId.Seniverse -> cleanValue("status_code")
                ?.takeUnless { it in setOf("0", "200") }
                ?.let(::errorReason)
            SourceId.OpenWeather -> cleanValue("cod")
                ?.takeUnless { it.toIntOrNull() in 200..299 }
                ?.let(::errorReason)
            SourceId.XiaomiWeather -> cleanValue("status")
                ?.takeUnless { it in setOf("0", "1", "200") }
                ?.let(::errorReason)
            SourceId.Meteostat -> cleanValue("message")
                ?.takeIf { payload.optJSONArray("data") == null }
            else -> null
        }
    }

    private fun domesticReadings(
        region: District,
        liveReadings: List<WeatherReading> = emptyList(),
    ): List<WeatherReading> {
        val liveXiaomi = liveReadings.firstOrNull { it.source.id == SourceId.XiaomiWeather }
        if (liveReadings.any { it.temperatureC != null }) {
            return liveReadings.distinctBy { it.source.id }
        }
        val coastalSevere = region.city == "深圳市"
        val beijing = region.city == "北京市"
        val baseTemp = liveXiaomi?.temperatureC ?: when {
            coastalSevere -> 29.0
            beijing -> 27.5
            else -> 30.0
        }
        val feelsLike = liveXiaomi?.feelsLikeC ?: baseTemp + 1.5
        val rain = liveXiaomi?.rainProbability ?: if (coastalSevere) 92.0 else if (beijing) 64.0 else 48.0
        val rainNextHour = liveXiaomi?.rainNextHourMm ?: if (coastalSevere) 14.0 else 4.2
        val wind = liveXiaomi?.windKph ?: if (coastalSevere) 54.0 else 20.0
        val aqi = liveXiaomi?.aqi ?: 72
        val pm25 = liveXiaomi?.pm25 ?: 31.0
        val uvIndex = liveXiaomi?.uvIndex ?: if (coastalSevere) 5.2 else 7.4
        val humidity = liveXiaomi?.humidityPercent ?: if (coastalSevere) 86 else 68
        val condition = liveXiaomi?.condition ?: if (coastalSevere) WeatherCondition.Storm else WeatherCondition.Rain
        val alert = liveXiaomi?.alert ?: when {
            coastalSevere -> WeatherAlert(AlertLevel.Severe, "台风和暴雨风险", "已触发停工停学在线查询预留流程。")
            rain >= 60 -> WeatherAlert(AlertLevel.Rain, "短时降雨", "未来一小时有明显降水概率。")
            else -> WeatherAlert(AlertLevel.None, "无显著预警", "未发现高影响天气。")
        }
        val xiaomi = liveXiaomi ?: WeatherReading(WeatherSource(SourceId.XiaomiWeather, "小米天气", "国内 · 优先预报源"), baseTemp - 0.3, feelsLike, rain, rainNextHour, wind, aqi, pm25, uvIndex, humidity, 3, if (coastalSevere) 4 else 2, condition, alert)

        return (listOf(
            xiaomi,
            WeatherReading(WeatherSource(SourceId.Cnemc, "中国环境监测总站", "国内 · 优先空气质量源"), null, null, null, null, null, (aqi + 11).coerceAtLeast(0), pm25 + 7.0, null, null, null, null, null, null),
            WeatherReading(WeatherSource(SourceId.Amap, "高德天气", "国内 · 地址和实况补充"), baseTemp + 0.7, feelsLike + 0.6, (rain - 8).coerceIn(0.0, 100.0), (rainNextHour * 0.74).coerceAtLeast(0.0), (wind * 0.86).coerceAtLeast(0.0), (aqi - 21).coerceAtLeast(0), (pm25 - 10.0).coerceAtLeast(0.0), (uvIndex + 0.6).coerceAtLeast(0.0), (humidity + 4).coerceIn(0, 100), 2, if (coastalSevere) 4 else 2, condition, alert),
            WeatherReading(WeatherSource(SourceId.QWeather, "和风天气", "国内 · 综合预报"), baseTemp + 0.1, feelsLike - 0.1, (rain - 3).coerceIn(0.0, 100.0), (rainNextHour * 0.79).coerceAtLeast(0.0), (wind * 0.94).coerceAtLeast(0.0), (aqi - 17).coerceAtLeast(0), (pm25 - 11.0).coerceAtLeast(0.0), (uvIndex + 0.3).coerceAtLeast(0.0), (humidity + 2).coerceIn(0, 100), 3, if (coastalSevere) 4 else 2, condition, alert),
            WeatherReading(WeatherSource(SourceId.Seniverse, "心知天气", "国内 · 综合预报"), baseTemp - 0.8, feelsLike - 0.7, (rain - 14).coerceIn(0.0, 100.0), (rainNextHour * 0.55).coerceAtLeast(0.0), (wind * 0.78).coerceAtLeast(0.0), (aqi + 16).coerceAtLeast(0), pm25 + 10.0, (uvIndex - 0.6).coerceAtLeast(0.0), (humidity + 1).coerceIn(0, 100), 2, if (coastalSevere) 3 else 2, if (rain >= 60) WeatherCondition.Rain else WeatherCondition.Cloudy, alert),
        ) + liveReadings.filter { it.astronomy != AstronomyInfo.Empty }).distinctBy { it.source.id }
    }

    private fun internationalReadings(
        region: District,
        liveReadings: List<WeatherReading> = emptyList(),
    ): List<WeatherReading> {
        if (liveReadings.any { it.temperatureC != null }) {
            return liveReadings.distinctBy { it.source.id }
        }
        val us = region.countryCode == "US"
        val cool = region.city == "London"
        val baseTemp = if (cool) 17.5 else if (us) 23.0 else 26.0
        val rain = if (cool) 58.0 else 32.0
        val alert = if (rain >= 55) WeatherAlert(AlertLevel.Rain, "Showers likely", "Short-term rain signal is consistent across sources.") else WeatherAlert(AlertLevel.None, "No active severe alert", "No high-impact public alert detected.")
        return listOf(
            WeatherReading(WeatherSource(SourceId.OpenWeather, "OpenWeather", "国外 · 加权增强"), baseTemp + 0.2, baseTemp + 0.7, rain, if (cool) 2.8 else 0.6, if (cool) 24.0 else 14.0, if (cool) 42 else 61, if (cool) 12.0 else 19.0, if (cool) 3.6 else 7.2, if (cool) 76 else 54, if (cool) 3 else 2, if (cool) 3 else 1, if (cool) WeatherCondition.Rain else WeatherCondition.Sunny, alert),
            WeatherReading(WeatherSource(SourceId.MsnWeather, "MSN 天气", "国外 · 综合预报"), baseTemp - 0.1, baseTemp + 0.5, rain + 4, if (cool) 3.1 else 0.5, if (cool) 25.0 else 15.0, if (cool) 45 else 58, if (cool) 13.0 else 18.0, if (cool) 3.4 else 7.0, if (cool) 74 else 55, if (cool) 3 else 2, if (cool) 3 else 1, if (cool) WeatherCondition.Rain else WeatherCondition.Sunny, alert),
            WeatherReading(WeatherSource(SourceId.VisualCrossing, "Visual Crossing", "国外 · 历史和预报"), baseTemp + 0.6, baseTemp + 1.0, rain - 8, if (cool) 1.9 else 0.2, if (cool) 21.0 else 13.0, null, null, if (cool) 3.7 else 7.6, if (cool) 78 else 52, if (cool) 2 else 2, if (cool) 2 else 1, if (cool) WeatherCondition.Cloudy else WeatherCondition.Sunny, null),
            WeatherReading(WeatherSource(SourceId.MetNo, "met.no", "国外 · 免费预报"), baseTemp - 0.4, baseTemp + 0.1, rain + 1, if (cool) 2.6 else 0.4, if (cool) 23.0 else 12.0, null, null, if (cool) 3.5 else 7.1, if (cool) 77 else 56, null, null, if (cool) WeatherCondition.Rain else WeatherCondition.Sunny, alert),
            WeatherReading(WeatherSource(SourceId.Meteostat, "Meteostat API", "国外 · 历史观测"), baseTemp + 1.8, baseTemp + 2.4, rain - 22, if (cool) 0.3 else 0.0, if (cool) 15.0 else 9.0, null, null, null, if (cool) 68 else 49, null, null, WeatherCondition.Cloudy, null),
            WeatherReading(WeatherSource(SourceId.OpenMeteo, "Open-Meteo", "国外 · 免费基线"), baseTemp, baseTemp + 0.4, rain + 2, if (cool) 2.7 else 0.3, if (cool) 22.0 else 13.0, if (cool) 44 else 62, if (cool) 11.0 else 20.0, if (cool) 3.5 else 7.3, if (cool) 75 else 54, if (cool) 3 else 2, if (cool) 3 else 1, if (cool) WeatherCondition.Rain else WeatherCondition.Sunny, alert),
            WeatherReading(WeatherSource(SourceId.Nws, "NWS / weather.gov", "美国地区 · 官方补充"), if (us) baseTemp - 0.2 else null, if (us) baseTemp + 0.3 else null, if (us) rain - 2 else null, if (us) 0.4 else null, if (us) 16.0 else null, null, null, if (us) 7.4 else null, if (us) 56 else null, null, null, if (us) WeatherCondition.Sunny else null, if (us) alert else null),
        )
    }
}

