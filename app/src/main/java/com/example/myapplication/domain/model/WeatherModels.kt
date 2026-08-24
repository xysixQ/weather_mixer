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

internal data class WeatherMetric(val label: String, val value: String, val icon: ImageVector)

internal data class UserProfile(
    val occupation: Occupation,
    val commuteMode: CommuteMode,
    val allergens: Set<Allergen>,
    val vehicleRestrictionEnabled: Boolean,
)

internal enum class Occupation(val label: String, val icon: ImageVector) {
    Outdoor("户外工作者", Icons.Filled.Work),
    Office("办公室人群", Icons.Filled.Person),
    Student("学生", Icons.Filled.School),
    Homebody("宅在家", Icons.Filled.Home),
    Other("其他", Icons.Filled.Person),
}

internal enum class CommuteMode(val label: String, val icon: ImageVector) {
    Bike("骑车", Icons.AutoMirrored.Filled.DirectionsBike),
    Car("开车", Icons.Filled.DirectionsCar),
    PublicTransit("公共交通", Icons.Filled.DirectionsBus),
    Walk("步行", Icons.AutoMirrored.Filled.DirectionsWalk),
    Taxi("打车", Icons.Filled.LocalTaxi),
    Other("其他", Icons.Filled.Person),
}

internal enum class Allergen(val label: String, val icon: ImageVector) {
    Pollen("花粉", Icons.Filled.Air),
    Uv("紫外线敏感", Icons.Filled.WbSunny),
    Spore("孢子", Icons.Filled.Cloud),
}

internal val DefaultUserProfile = UserProfile(
    occupation = Occupation.Other,
    commuteMode = CommuteMode.Other,
    allergens = emptySet(),
    vehicleRestrictionEnabled = false,
)

internal enum class LocationMethod(val label: String, val icon: ImageVector) {
    BaiduIp("百度 IP 定位", Icons.Filled.LocationOn),
    Device("系统定位", Icons.Filled.MyLocation),
}

internal data class District(
    val countryCode: String,
    val province: String,
    val city: String,
    val district: String,
    val latitude: Double,
    val longitude: Double,
    val locationKey: String = "",
) {
    val isDomestic: Boolean = countryCode == "CN"
    private fun String.cleanDomesticSuffix(): String = removeSuffix("省").removeSuffix("市").removeSuffix("区").removeSuffix("县")
    val storageKey: String = locationKey.ifBlank { "$countryCode|$province|$city|$district|$latitude|$longitude" }
    val locationButtonName: String = (district.ifBlank { city }).let { if (isDomestic) it.cleanDomesticSuffix() else it }
    val shortName: String = locationButtonName
    val compactName: String = locationButtonName
    val displayName: String = if (isDomestic) {
        listOf(province, city, district)
            .map { it.cleanDomesticSuffix() }
            .filter(String::isNotBlank)
            .distinct()
            .joinToString(" · ")
    } else {
        listOf(district, city, province)
            .filter(String::isNotBlank)
            .distinct()
            .joinToString(", ")
    }
    val recommendedSources: List<String> = when {
        isDomestic -> listOf("小米天气", "高德天气", "和风天气", "MSN 天气")
        countryCode == "US" -> listOf("NWS", "MSN 天气", "OpenWeather", "Open-Meteo")
        else -> listOf("MSN 天气", "Open-Meteo", "OpenWeather", "met.no")
    }
}

internal enum class SourceId {
    XiaomiCitySearch,
    XiaomiCityGeo,
    XiaomiWeather,
    Cnemc,
    Amap,
    QWeather,
    Seniverse,
    MsnWeather,
    OpenWeather,
    VisualCrossing,
    MetNo,
    Meteostat,
    OpenMeteo,
    Nws,
    BaiduIpLocation,
}

internal data class WeatherApiConfig(
    val sourceId: SourceId,
    val displayName: String,
    val endpoint: String,
    val apiKey: String,
    val userAgent: String,
    val enabled: Boolean,
    val requiresKey: Boolean,
    val hasBuiltInDefault: Boolean,
    val needsUserAgent: Boolean,
    val note: String,
    val apiHost: String = "",
) {
    val isReady: Boolean
        get() = enabled &&
            endpoint.isNotBlank() &&
            (sourceId != SourceId.QWeather || apiHost.isNotBlank()) &&
            (!requiresKey || apiKey.isNotBlank()) &&
            (!needsUserAgent || userAgent.isNotBlank())

    val statusLabel: String
        get() = when {
            !enabled -> "已停用"
            endpoint.isBlank() -> "需要填写 endpoint"
            sourceId == SourceId.QWeather && apiHost.isBlank() -> "需要填写 API Host"
            requiresKey && apiKey.isBlank() -> "需要填写 Key"
            needsUserAgent && userAgent.isBlank() -> "需要填写 User-Agent"
            hasBuiltInDefault -> "默认免费配置可用"
            else -> "已配置"
    }
}

internal const val BuiltInCredentialPlaceholder = "{内置凭据}"

internal val endpointCredentialPattern = Regex(
    pattern = """(?i)([?&])(apiKey|appKey|sign)=([^&]*)""",
)

internal fun hideBuiltInEndpointCredentials(endpoint: String, defaultEndpoint: String): String {
    if (endpoint.isBlank() || defaultEndpoint.isBlank()) return endpoint
    val defaults = endpointCredentialPattern.findAll(defaultEndpoint).associate {
        it.groupValues[2].lowercase() to it.groupValues[3]
    }
    return endpointCredentialPattern.replace(endpoint) { match ->
        val name = match.groupValues[2]
        val value = match.groupValues[3]
        val displayedValue = if (defaults[name.lowercase()] == value && value.isNotBlank()) {
            BuiltInCredentialPlaceholder
        } else {
            value
        }
        "${match.groupValues[1]}$name=$displayedValue"
    }
}

internal fun restoreBuiltInEndpointCredentials(endpoint: String, defaultEndpoint: String): String {
    if (endpoint.isBlank() || defaultEndpoint.isBlank()) return endpoint
    val defaults = endpointCredentialPattern.findAll(defaultEndpoint).associate {
        it.groupValues[2].lowercase() to it.groupValues[3]
    }
    return endpointCredentialPattern.replace(endpoint) { match ->
        val name = match.groupValues[2]
        val value = match.groupValues[3]
        val restoredValue = if (value == BuiltInCredentialPlaceholder) {
            defaults[name.lowercase()] ?: value
        } else {
            value
        }
        "${match.groupValues[1]}$name=$restoredValue"
    }
}

internal fun endpointVariableHelp(config: WeatherApiConfig, defaultEndpoint: String): String {
    val template = config.endpoint.ifBlank { defaultEndpoint }
    val descriptions = mapOf(
        "key" to "API Key 或 Token",
        "host" to "API Host（不含 https://）",
        "sign" to "API 签名",
        "lat" to "纬度",
        "latitude" to "纬度",
        "lon" to "经度",
        "longitude" to "经度",
        "query" to "搜索关键词（自动编码）",
        "name" to "城市搜索关键词（自动编码）",
        "city" to "城市名称",
        "adcode" to "行政区划代码",
        "locationKey" to "小米 weathercn 城市 Key",
        "locale" to "语言和地区代码",
        "start" to "开始日期，格式 yyyy-MM-dd",
        "end" to "结束日期，格式 yyyy-MM-dd",
    )
    val variables = Regex("""\{([A-Za-z][A-Za-z0-9_]*)\}""")
        .findAll(template)
        .map { it.groupValues[1] }
        .distinct()
        .toList()
    if (variables.isEmpty()) return "此 Endpoint 不需要模板参数。"
    return "模板参数：" + variables.joinToString("；") { variable ->
        "{$variable} = ${descriptions[variable] ?: "运行时变量"}"
    }
}

internal object ApiConfigDefaults {
    fun defaultConfigs(): List<WeatherApiConfig> = listOf(
        WeatherApiConfig(
            sourceId = SourceId.BaiduIpLocation,
            displayName = "百度 IP 定位",
            endpoint = "https://api.map.baidu.com/location/ip?coor=gcj02&ak={key}",
            apiKey = BuildConfig.BAIDU_IP_LOCATION_API_KEY,
            userAgent = "",
            enabled = true,
            requiresKey = true,
            hasBuiltInDefault = false,
            needsUserAgent = false,
            note = "默认定位方式。百度 IP 定位接口需要百度地图开放平台 AK；填写后可不依赖系统 GPS 权限获取大致城市/坐标。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.XiaomiCitySearch,
            displayName = "小米城市搜索",
            endpoint = "https://weatherapi.market.xiaomi.com/wtr-v3/location/city/search?name={query}&locale=zh_CN",
            apiKey = "",
            userAgent = "",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = false,
            note = "无 Key 城市搜索源；返回 weathercn 城市 key，供小米天气接口继续使用。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.XiaomiCityGeo,
            displayName = "小米坐标反查",
            endpoint = "https://weatherapi.market.xiaomi.com/wtr-v3/location/city/geo?latitude={lat}&longitude={lon}&locale=zh_CN",
            apiKey = "",
            userAgent = "",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = false,
            note = "无 Key 坐标反查源；百度 IP 或系统定位拿到坐标后，用它识别城市并取得 weathercn 城市 key。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.XiaomiWeather,
            displayName = "小米天气",
            endpoint = "https://weatherapi.market.xiaomi.com/wtr-v3/weather/all?latitude={lat}&longitude={lon}&isLocated=false&locationKey=weathercn:{locationKey}&days=15&isGlobal=false&locale=zh_CN",
            apiKey = "",
            userAgent = "",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = false,
            note = "小米天气国内源，无 Key；用城市 key 请求 weather/all。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.Cnemc,
            displayName = "中国环境监测总站",
            endpoint = "https://air.cnemc.cn:18007/CityData/GetAQIDataPublishLive?cityName={city}",
            apiKey = "",
            userAgent = "",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = false,
            note = "内置空气质量公共入口模板；若官方入口调整，可手动改 endpoint。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.Amap,
            displayName = "高德天气",
            endpoint = "https://restapi.amap.com/v3/weather/weatherInfo?key={key}&city={adcode}&extensions=all",
            apiKey = BuildConfig.AMAP_API_KEY,
            userAgent = "",
            enabled = true,
            requiresKey = true,
            hasBuiltInDefault = false,
            needsUserAgent = false,
            note = "需要高德 Web 服务 Key；可同时用于行政区划和逆地理补充。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.QWeather,
            displayName = "和风天气",
            endpoint = "https://{host}/v7/weather/now?location={lon},{lat}",
            apiKey = BuildConfig.QWEATHER_API_KEY,
            userAgent = "",
            enabled = true,
            requiresKey = true,
            hasBuiltInDefault = false,
            needsUserAgent = false,
            note = "使用新版专属 API Host 和 X-QW-Api-Key 请求头鉴权；API Host 可在和风控制台的设置页面查看。",
            apiHost = BuildConfig.QWEATHER_API_HOST,
        ),
        WeatherApiConfig(
            sourceId = SourceId.Seniverse,
            displayName = "心知天气",
            endpoint = "https://api.seniverse.com/v3/weather/now.json?key={key}&location={city}&language=zh-Hans&unit=c",
            apiKey = BuildConfig.SENIVERSE_API_KEY,
            userAgent = "",
            enabled = true,
            requiresKey = true,
            hasBuiltInDefault = false,
            needsUserAgent = false,
            note = "需要心知 Key；当前保留实况模板，后续可加逐小时接口。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.MsnWeather,
            displayName = "MSN 天气",
            endpoint = "https://api.msn.com/weather/overview?locale=zh-cn&lat={lat}&lon={lon}&appId=9e21380c-ff19-4c78-b4ea-19558e93a5d3&ocid=msftweather&wrapOData=false&units=C&pastPeriods=1&days=10&hours=24",
            apiKey = "",
            userAgent = "WeatherFusionAssistant/1.0",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = false,
            note = "MSN 天气综合源，无 Key；提供实况、逐日、逐小时、日出日落和月相。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.OpenWeather,
            displayName = "OpenWeather",
            endpoint = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={key}&units=metric&lang=zh_cn",
            apiKey = BuildConfig.OPENWEATHER_API_KEY,
            userAgent = "",
            enabled = true,
            requiresKey = true,
            hasBuiltInDefault = false,
            needsUserAgent = false,
            note = "需要 API Key；国外融合中权重增加。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.VisualCrossing,
            displayName = "Visual Crossing",
            endpoint = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/{lat},{lon}?unitGroup=metric&key={key}&contentType=json",
            apiKey = BuildConfig.VISUAL_CROSSING_API_KEY,
            userAgent = "",
            enabled = true,
            requiresKey = true,
            hasBuiltInDefault = false,
            needsUserAgent = false,
            note = "需要 Key；适合补充历史和逐日/逐小时数据。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.MetNo,
            displayName = "met.no",
            endpoint = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat={lat}&lon={lon}",
            apiKey = "",
            userAgent = "WeatherFusionAssistant/1.0",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = true,
            note = "官方免费源，无 Key，但请求必须带明确 User-Agent。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.Meteostat,
            displayName = "Meteostat API",
            endpoint = "https://meteostat.p.rapidapi.com/point/hourly?lat={lat}&lon={lon}&start={start}&end={end}",
            apiKey = BuildConfig.METEOSTAT_RAPIDAPI_KEY,
            userAgent = "",
            enabled = true,
            requiresKey = true,
            hasBuiltInDefault = false,
            needsUserAgent = false,
            note = "通过 RapidAPI 请求头接入；优先请求 point/hourly，若无有效观测则自动查询附近 station 并用 station/hourly 回退测试。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.OpenMeteo,
            displayName = "Open-Meteo",
            endpoint = "https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,rain,weather_code,wind_speed_10m,wind_direction_10m,pressure_msl&hourly=temperature_2m,apparent_temperature,precipitation_probability,precipitation,rain,weather_code,wind_speed_10m,wind_direction_10m,relative_humidity_2m,uv_index&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max,precipitation_sum,wind_speed_10m_max,sunrise,sunset&past_days=1&forecast_days=10&timezone=auto",
            apiKey = "",
            userAgent = "",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = false,
            note = "官方免费源，无 Key；作为国外免费基线源内置启用。",
        ),
        WeatherApiConfig(
            sourceId = SourceId.Nws,
            displayName = "NWS / weather.gov",
            endpoint = "https://api.weather.gov/points/{lat},{lon}",
            apiKey = "",
            userAgent = "WeatherFusionAssistant/1.0",
            enabled = true,
            requiresKey = false,
            hasBuiltInDefault = true,
            needsUserAgent = true,
            note = "美国地区官方免费源，无 Key；只在美国地区参与融合。",
        ),
    )
}

internal data class WeatherSource(val id: SourceId, val displayName: String, val category: String)

internal data class DailyForecast(
    val timeMillis: Long,
    val highC: Double,
    val lowC: Double,
    val condition: WeatherCondition,
    val rainProbability: Double?,
    val aqi: Int?,
    val windKph: Double?,
    val sunrise: String?,
    val sunset: String?,
    val isYesterday: Boolean = false,
)

internal data class HourlyForecast(
    val timeMillis: Long,
    val temperatureC: Double,
    val condition: WeatherCondition,
    val rainProbability: Double?,
    val aqi: Int?,
    val windKph: Double?,
    val windDirection: Double?,
)

internal data class AstronomyInfo(
    val sunrise: String?,
    val sunset: String?,
    val moonrise: String?,
    val moonset: String?,
    val moonPhase: String? = null,
) {
    companion object {
        val Empty = AstronomyInfo(null, null, null, null)
    }
}

internal data class WeatherReading(
    val source: WeatherSource,
    val temperatureC: Double?,
    val feelsLikeC: Double?,
    val rainProbability: Double?,
    val rainNextHourMm: Double?,
    val windKph: Double?,
    val aqi: Int?,
    val pm25: Double?,
    val uvIndex: Double?,
    val humidityPercent: Int?,
    val pollenLevel: Int?,
    val sporeLevel: Int?,
    val condition: WeatherCondition?,
    val alert: WeatherAlert?,
    val dailyForecast: List<DailyForecast> = emptyList(),
    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val astronomy: AstronomyInfo = AstronomyInfo.Empty,
    val pressureHpa: Double? = null,
    val dewPointC: Double? = null,
)

internal enum class WeatherVisualFamily {
    Clear,
    Cloud,
    Rain,
    Thunder,
    Snow,
    Atmosphere,
    Dust,
}

internal enum class WeatherCondition(
    val label: String,
    val visualFamily: WeatherVisualFamily,
    val intensityRank: Int = 0,
) {
    Sunny("晴朗", WeatherVisualFamily.Clear),
    Cloudy("多云", WeatherVisualFamily.Cloud),
    Fog("雾", WeatherVisualFamily.Atmosphere),
    Haze("霾", WeatherVisualFamily.Atmosphere),
    Dust("浮尘", WeatherVisualFamily.Dust),
    Sandstorm("沙尘暴", WeatherVisualFamily.Dust, 2),
    Shower("阵雨", WeatherVisualFamily.Rain, 1),
    HeavyShower("强阵雨", WeatherVisualFamily.Rain, 3),
    LightRain("小雨", WeatherVisualFamily.Rain, 1),
    ModerateRain("中雨", WeatherVisualFamily.Rain, 2),
    Rain("降雨", WeatherVisualFamily.Rain, 2),
    HeavyRain("大雨", WeatherVisualFamily.Rain, 3),
    Rainstorm("暴雨", WeatherVisualFamily.Rain, 4),
    ThunderShower("雷阵雨", WeatherVisualFamily.Thunder, 2),
    Hail("冰雹", WeatherVisualFamily.Thunder, 3),
    Storm("强对流", WeatherVisualFamily.Thunder, 4),
    Snow("雪", WeatherVisualFamily.Snow, 1),
    Sleet("雨夹雪", WeatherVisualFamily.Snow, 2),
    FreezingRain("冻雨", WeatherVisualFamily.Snow, 3),
}

internal val WeatherCondition.isRainLike: Boolean
    get() = visualFamily == WeatherVisualFamily.Rain ||
        visualFamily == WeatherVisualFamily.Thunder ||
        this == WeatherCondition.Sleet ||
        this == WeatherCondition.FreezingRain

internal val WeatherCondition.isSevereWeather: Boolean
    get() = this in setOf(
        WeatherCondition.Rainstorm,
        WeatherCondition.Sandstorm,
        WeatherCondition.Hail,
        WeatherCondition.Storm,
    )

internal fun rainConditionForHourlyMm(value: Double?): WeatherCondition? = when {
    value == null || !value.isFinite() || value <= 0.0 -> null
    value <= 2.5 -> WeatherCondition.LightRain
    value <= 8.0 -> WeatherCondition.ModerateRain
    value < 16.0 -> WeatherCondition.HeavyRain
    else -> WeatherCondition.Rainstorm
}

internal fun WeatherCondition.refinedByHourlyPrecipitation(value: Double?): WeatherCondition {
    if (visualFamily != WeatherVisualFamily.Rain) return this
    return rainConditionForHourlyMm(value) ?: this
}

internal enum class AlertLevel(val severity: Int) {
    None(0),
    Rain(1),
    Heat(2),
    Severe(3),
}

internal data class WeatherAlert(val level: AlertLevel, val title: String, val detail: String)

internal data class FusedWeather(
    val temperatureC: Double,
    val feelsLikeC: Double,
    val rainProbability: Double,
    val rainNextHourMm: Double,
    val windKph: Double,
    val aqi: Int,
    val pm25: Double,
    val uvIndex: Double,
    val humidityPercent: Int,
    val pollenLevel: Int,
    val sporeLevel: Int,
    val condition: WeatherCondition,
    val alert: WeatherAlert,
    val confidencePercent: Int,
    val pressureHpa: Double = 1013.25,
    val dewPointC: Double? = null,
)

internal data class PersonalizedAdvice(
    val title: String,
    val detail: String,
    val icon: ImageVector,
    val level: AdviceLevel,
)

internal data class VehicleRestriction(val detail: String)

internal enum class AdviceLevel {
    Info,
    Caution,
    Warning,
}

internal data class SourceWeightView(val name: String, val normalizedWeight: Float, val reason: String)

internal data class WeatherSnapshot(
    val region: District,
    val readings: List<WeatherReading>,
    val fused: FusedWeather,
    val advice: List<PersonalizedAdvice>,
    val sourceWeights: List<SourceWeightView>,
    val fusionSummary: String,
    val anomalyNotes: List<String>,
    val dailyForecast: List<DailyForecast>,
    val hourlyForecast: List<HourlyForecast>,
    val astronomy: AstronomyInfo,
    val updatedAtMillis: Long,
)

internal data class WeatherSnapshotCacheEntry(
    val snapshot: WeatherSnapshot,
    val apiConfigSignature: Int,
    val cachedAtMillis: Long,
)

internal const val MinimumWeatherRefreshIntervalMillis = 10_000L
internal const val AutomaticWeatherRefreshIntervalMillis = 8 * 60_000L

internal data class LocatedPosition(
    val latitude: Double,
    val longitude: Double,
    val providerLabel: String,
    val region: District?,
) {
    val coordinateText: String = "${formatCoordinate(latitude)}, ${formatCoordinate(longitude)}"
}

internal data class LocationLookupResult(
    val position: LocatedPosition?,
    val failureMessage: String,
)

internal data class CityGeoResult(
    val region: District?,
    val message: String,
)

internal data class CitySearchResult(
    val regions: List<District>,
    val message: String,
)

internal data class ApiSourceFailure(
    val sourceId: SourceId,
    val displayName: String,
    val reason: String,
)

