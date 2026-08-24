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


@Composable
internal fun SourceFusionCard(snapshot: WeatherSnapshot, onClick: () -> Unit) {
    Card(
        modifier = Modifier.floatingCardMotion().clip(RoundedCornerShape(28.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Psychology, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("多源融合分析", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text(
                text = snapshot.fusionSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AnimatedVisibility(visible = snapshot.anomalyNotes.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    snapshot.anomalyNotes.forEach { note ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Filled.Info,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.tertiary,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(note, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            HorizontalDivider()
            snapshot.sourceWeights.forEach { item ->
                SourceWeightRow(item)
            }
        }
    }
}

@Composable
internal fun SourceWeightRow(item: SourceWeightView) {
    val progress by animateFloatAsState(
        targetValue = item.normalizedWeight,
        animationSpec = tween(600),
        label = item.name,
    )
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(item.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = item.reason,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
internal fun AdviceCard(title: String, subtitle: String, advice: List<PersonalizedAdvice>, onClick: () -> Unit) {
    val orderedAdvice = remember(advice) {
        advice.sortedByDescending { item -> item.level.ordinal }
    }
    val shape = RoundedCornerShape(28.dp)
    Card(
        modifier = Modifier
            .floatingCardMotion()
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Icon(Icons.Filled.HealthAndSafety, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            }
            orderedAdvice.forEach { item -> AdviceItem(item) }
        }
    }
}

@Composable
internal fun AdviceItem(item: PersonalizedAdvice) {
    val isVehicleRestriction = item.title == "机动车限行"
    val darkTheme = LocalAppDarkTheme.current
    var expanded by remember(item) { mutableStateOf(!isVehicleRestriction) }
    val shape = RoundedCornerShape(18.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressedDepth by animateFloatAsState(
        targetValue = if (isPressed) 0.12f else 0f,
        animationSpec = tween(150),
        label = "advicePressedDepth",
    )
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(360, easing = FastOutSlowInEasing),
        label = "restrictionArrowRotation",
    )
    val baseContainerColor = when {
        isVehicleRestriction && darkTheme -> Color(0xFF6A602F)
        isVehicleRestriction -> Color(0xFFFFF8D8)
        else -> when (item.level) {
        AdviceLevel.Info -> MaterialTheme.colorScheme.secondaryContainer
        AdviceLevel.Caution -> MaterialTheme.colorScheme.tertiaryContainer
        AdviceLevel.Warning -> MaterialTheme.colorScheme.errorContainer
        }
    }
    val containerColor = lerp(baseContainerColor, Color.Black, pressedDepth)
    val iconTint = when {
        isVehicleRestriction && darkTheme -> Color(0xFFFFE082)
        isVehicleRestriction -> Color(0xFF6D4C00)
        else -> when (item.level) {
        AdviceLevel.Info -> MaterialTheme.colorScheme.secondary
        AdviceLevel.Caution -> MaterialTheme.colorScheme.tertiary
        AdviceLevel.Warning -> MaterialTheme.colorScheme.error
        }
    }
    val rowModifier = Modifier
        .fillMaxWidth()
        .clip(shape)
        .background(containerColor.copy(alpha = if (isVehicleRestriction) 0.54f else 0.72f))
        .let { base ->
            if (isVehicleRestriction) {
                base.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { expanded = !expanded }
            } else {
                base
            }
        }
        .animateContentSize(animationSpec = tween(460, easing = FastOutSlowInEasing))
        .padding(12.dp)
    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.Top,
    ) {
        Icon(item.icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                if (isVehicleRestriction) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "收起限行详情" else "展开限行详情",
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer { rotationZ = arrowRotation },
                        tint = iconTint,
                    )
                }
            }
            Text(
                text = item.detail,
                style = MaterialTheme.typography.bodySmall,
                color = if (isVehicleRestriction) {
                    if (darkTheme) Color(0xFFFFF4C2) else Color(0xFF3D3100)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun SourceStatusCard(snapshot: WeatherSnapshot, onClick: () -> Unit) {
    val temperatureUnit = LocalTemperatureUnit.current
    Card(
        modifier = Modifier.floatingCardMotion().clip(RoundedCornerShape(28.dp)).clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("天气源接入状态", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            snapshot.readings.forEach { reading ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(reading.source.displayName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = reading.source.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = "T ${reading.temperatureC?.let { temperatureUnit.format(it, decimal = snapshot.region.isDomestic) } ?: "-"} · AQI ${reading.aqi ?: "-"}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Composable
internal fun ApiSettingsCard(
    configs: List<WeatherApiConfig>,
    onSave: (List<WeatherApiConfig>) -> Unit,
    onReset: () -> Unit,
) {
    val context = LocalContext.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    var expanded by remember { mutableStateOf(false) }
    var draftConfigs by remember(configs) { mutableStateOf(configs) }
    val readyCount = draftConfigs.count { it.isReady }
    val freeReadyCount = draftConfigs.count { it.hasBuiltInDefault && it.isReady }

    Card(
        modifier = Modifier.floatingCardMotion(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("天气源配置", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$readyCount/${draftConfigs.size} 个源可用 · $freeReadyCount 个免费默认源已内置",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "收起" else "编辑")
                }
            }
            Text(
                text = "小米城市搜索、小米天气、MSN、Open-Meteo、met.no、NWS 等源已有默认 endpoint；和风需同时填写专属 API Host 和 Key。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    draftConfigs.forEach { config ->
                        ApiConfigEditor(
                            config = config,
                            onChange = { updated ->
                                draftConfigs = draftConfigs.map {
                                    if (it.sourceId == updated.sourceId) updated else it
                                }
                            },
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (hapticFeedbackEnabled) {
                                    context.performAppVibration(AppVibration.StrongImpact)
                                }
                                draftConfigs = ApiConfigDefaults.defaultConfigs()
                                onReset()
                            },
                        ) {
                            Text("恢复默认")
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (hapticFeedbackEnabled) {
                                    context.performAppVibration(AppVibration.StrongImpact)
                                }
                                onSave(draftConfigs)
                            },
                        ) {
                            Text("保存配置")
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ApiConfigEditor(
    config: WeatherApiConfig,
    onChange: (WeatherApiConfig) -> Unit,
) {
    val defaultConfig = remember(config.sourceId) {
        ApiConfigDefaults.defaultConfigs().firstOrNull { it.sourceId == config.sourceId }
    }
    val defaultApiKey = defaultConfig?.apiKey.orEmpty()
    val displayedApiKey = if (defaultApiKey.isNotBlank() && config.apiKey == defaultApiKey) {
        ""
    } else {
        config.apiKey
    }
    val defaultApiHost = defaultConfig?.apiHost.orEmpty()
    val displayedApiHost = if (defaultApiHost.isNotBlank() && config.apiHost == defaultApiHost) {
        ""
    } else {
        config.apiHost
    }
    val defaultEndpoint = defaultConfig?.endpoint.orEmpty()
    val displayedEndpoint = if (defaultEndpoint.isNotBlank() && config.endpoint == defaultEndpoint) {
        ""
    } else {
        config.endpoint
    }
    val endpointHelp = endpointVariableHelp(config, defaultEndpoint)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.48f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(config.displayName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    text = config.statusLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (config.isReady) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
                )
            }
            Switch(
                checked = config.enabled,
                onCheckedChange = { onChange(config.copy(enabled = it)) },
            )
        }
        val apiKeyField: @Composable (Modifier) -> Unit = { fieldModifier ->
            OutlinedTextField(
                modifier = fieldModifier,
                value = displayedApiKey,
                onValueChange = {
                    onChange(
                        config.copy(
                            apiKey = if (it.isBlank() && defaultApiKey.isNotBlank()) defaultApiKey else it,
                        ),
                    )
                },
                label = {
                    Text(if (config.requiresKey) "API Key / Token" else "API Key / Token（可选）")
                },
                supportingText = if (defaultApiKey.isNotBlank()) {
                    { Text("留空时使用默认 Key") }
                } else {
                    null
                },
                singleLine = true,
            )
        }
        if (config.sourceId == SourceId.QWeather) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = displayedApiHost,
                    onValueChange = {
                        onChange(
                            config.copy(
                                apiHost = if (it.isBlank() && defaultApiHost.isNotBlank()) defaultApiHost else it,
                            ),
                        )
                    },
                    label = { Text("API Host") },
                    supportingText = if (defaultApiHost.isNotBlank()) {
                        { Text("留空时使用默认 Host") }
                    } else {
                        null
                    },
                    singleLine = true,
                )
                apiKeyField(Modifier.weight(1f))
            }
        } else {
            apiKeyField(Modifier.fillMaxWidth())
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = displayedEndpoint,
            onValueChange = {
                onChange(
                    config.copy(
                        endpoint = if (it.isBlank() && defaultEndpoint.isNotBlank()) defaultEndpoint else it,
                    ),
                )
            },
            label = { Text("Endpoint") },
            supportingText = if (defaultEndpoint.isNotBlank()) {
                { Text("留空时使用默认 Endpoint；填写后使用自定义地址") }
            } else {
                null
            },
            singleLine = false,
            minLines = 1,
            maxLines = 3,
        )
        if (endpointHelp.isNotBlank()) {
            Text(
                text = endpointHelp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = config.note,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
internal fun DataPlanCard() {
    Card(
        modifier = Modifier.floatingCardMotion(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("接口预留", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text(
                text = "AI 建议 API 未配置时使用离线算法；极端天气下预留在线查询停工停学通知入口。可在 local.properties 中加入 aiAdviceApiKey 后替换为真实 AI 服务。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
internal fun MetricGrid(metrics: List<WeatherMetric>, onMetricClick: (WeatherMetric) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        metrics.chunked(2).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                rowItems.forEachIndexed { columnIndex, metric ->
                    MetricTile(
                        modifier = Modifier.weight(1f),
                        metric = metric,
                        delayMillis = (rowIndex * 2 + columnIndex) * 55,
                        onClick = { onMetricClick(metric) },
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
internal fun MetricTile(
    modifier: Modifier = Modifier,
    metric: WeatherMetric,
    delayMillis: Int = 0,
    onClick: () -> Unit,
) {
    val blockColor = when (metric.label) {
        "风速" -> MaterialTheme.colorScheme.primaryContainer
        "AQI" -> MaterialTheme.colorScheme.secondaryContainer
        "UV" -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    Card(
        modifier = modifier
            .heightIn(min = 112.dp)
            .floatingCardMotion(delayMillis)
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = blockColor.copy(alpha = 0.86f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.54f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        metric.icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Text(
                    metric.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                metric.value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
internal fun StatusBadge(level: AlertLevel) {
    val (color, text) = when (level) {
        AlertLevel.None -> MaterialTheme.colorScheme.secondary to "平稳"
        AlertLevel.Rain -> MaterialTheme.colorScheme.primary to "降雨"
        AlertLevel.Heat -> MaterialTheme.colorScheme.tertiary to "高温"
        AlertLevel.Severe -> MaterialTheme.colorScheme.error to "极端"
    }
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (level == AlertLevel.Severe) Icons.Filled.Warning else Icons.Filled.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = color,
        )
        Spacer(Modifier.width(6.dp))
        Text(text, color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
internal fun WeatherMiniIcon(
    condition: WeatherCondition,
    large: Boolean = false,
    isNight: Boolean = false,
    compact: Boolean = false,
) {
    Canvas(modifier = Modifier.size(if (large) 108.dp else if (compact) 34.dp else 44.dp)) {
        val scale = size.minDimension / 108f
        val center = Offset(size.width / 2f, size.height / 2f)
        if (isNight) {
            val clearNight = condition == WeatherCondition.Sunny
            val moonBounds = Offset(center.x - 30f * scale, center.y - 36f * scale)
            val moonSize = Size(58f * scale, 58f * scale)
            if (clearNight) {
                drawArc(
                    color = Color(0xFF173553),
                    startAngle = 48f,
                    sweepAngle = 258f,
                    useCenter = false,
                    topLeft = moonBounds,
                    size = moonSize,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(20f * scale, cap = StrokeCap.Round),
                )
                drawArc(
                    color = Color(0xFFF4FAFF),
                    startAngle = 48f,
                    sweepAngle = 258f,
                    useCenter = false,
                    topLeft = moonBounds,
                    size = moonSize,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(11f * scale, cap = StrokeCap.Round),
                )
                listOf(
                    Offset(center.x + 34f * scale, center.y - 28f * scale) to 3.2f,
                    Offset(center.x + 42f * scale, center.y - 7f * scale) to 2.5f,
                ).forEach { (starCenter, radius) ->
                    drawCircle(Color(0xFF173553), radius = (radius + 2.2f) * scale, center = starCenter)
                    drawCircle(Color(0xFFFFF4B8), radius = radius * scale, center = starCenter)
                }
            } else {
                drawArc(
                    color = Color(0xFFD9E8FF),
                    startAngle = 48f,
                    sweepAngle = 258f,
                    useCenter = false,
                    topLeft = moonBounds,
                    size = moonSize,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(12f * scale, cap = StrokeCap.Round),
                )
                drawCircle(Color(0xFFAEC9F4), 3f * scale, Offset(center.x + 34f * scale, center.y - 28f * scale))
                drawCircle(Color(0xFFEAF3FF), 2f * scale, Offset(center.x + 42f * scale, center.y - 7f * scale))
            }
        }
        fun drawCloud() {
            val cloudLight = if (isNight) Color(0xFF9FB2CE) else Color.White
            val cloudDark = if (isNight) Color(0xFF7186A5) else Color(0xFFE2E8F0)
            val cloudBase = if (isNight) Color(0xFF8297B5) else Color(0xFFD7E0EA)
            drawCircle(cloudLight, radius = 26f * scale, center = Offset(center.x - 18f * scale, center.y))
            drawCircle(cloudDark, radius = 32f * scale, center = Offset(center.x + 12f * scale, center.y - 6f * scale))
            drawRoundRect(
                color = cloudBase,
                topLeft = Offset(center.x - 42f * scale, center.y),
                size = Size(84f * scale, 26f * scale),
                cornerRadius = CornerRadius(20f * scale),
            )
        }

        fun drawRainDrops(count: Int, color: Color = Color(0xFF1769E0)) {
            repeat(count) { index ->
                val spacing = 56f / max(1, count - 1)
                val x = center.x - 28f * scale + index * spacing * scale
                drawLine(
                    color = color,
                    start = Offset(x, center.y + 31f * scale),
                    end = Offset(x - 7f * scale, center.y + 48f * scale),
                    strokeWidth = 4f * scale,
                    cap = StrokeCap.Round,
                )
            }
        }

        when (condition.visualFamily) {
            WeatherVisualFamily.Clear -> if (!isNight) {
                drawCircle(Color(0xFFFFC857), radius = 30f * scale, center = center)
                repeat(10) { index ->
                    val angle = index / 10f * Math.PI.toFloat() * 2f
                    drawLine(
                        color = Color(0xFFFFB020).copy(alpha = 0.75f),
                        start = Offset(center.x + cos(angle) * 38f * scale, center.y + sin(angle) * 38f * scale),
                        end = Offset(center.x + cos(angle) * 48f * scale, center.y + sin(angle) * 48f * scale),
                        strokeWidth = 4f * scale,
                        cap = StrokeCap.Round,
                    )
                }
            }
            WeatherVisualFamily.Cloud -> drawCloud()
            WeatherVisualFamily.Rain -> {
                drawCloud()
                val count = when (condition) {
                    WeatherCondition.LightRain -> 2
                    WeatherCondition.HeavyShower, WeatherCondition.HeavyRain, WeatherCondition.Rainstorm -> 5
                    else -> 3
                }
                drawRainDrops(count)
            }
            WeatherVisualFamily.Thunder -> {
                drawCloud()
                if (condition == WeatherCondition.Hail) {
                    repeat(4) { index ->
                        drawCircle(
                            color = Color(0xFFB9E6FF),
                            radius = 5f * scale,
                            center = Offset(center.x - 27f * scale + index * 18f * scale, center.y + 40f * scale),
                        )
                    }
                } else {
                    drawRainDrops(if (condition == WeatherCondition.Storm) 5 else 3)
                    val bolt = Path().apply {
                        moveTo(center.x + 5f * scale, center.y + 22f * scale)
                        lineTo(center.x - 8f * scale, center.y + 43f * scale)
                        lineTo(center.x + 3f * scale, center.y + 41f * scale)
                        lineTo(center.x - 4f * scale, center.y + 58f * scale)
                        lineTo(center.x + 19f * scale, center.y + 34f * scale)
                        lineTo(center.x + 7f * scale, center.y + 35f * scale)
                        close()
                    }
                    drawPath(bolt, Color(0xFFFFC928))
                }
            }
            WeatherVisualFamily.Snow -> {
                drawCloud()
                repeat(4) { index ->
                    val snowCenter = Offset(center.x - 28f * scale + index * 19f * scale, center.y + 42f * scale)
                    drawLine(Color(0xFF65B7E8), snowCenter - Offset(5f * scale, 0f), snowCenter + Offset(5f * scale, 0f), 2.5f * scale, cap = StrokeCap.Round)
                    drawLine(Color(0xFF65B7E8), snowCenter - Offset(0f, 5f * scale), snowCenter + Offset(0f, 5f * scale), 2.5f * scale, cap = StrokeCap.Round)
                    if (condition == WeatherCondition.Sleet && index % 2 == 1) {
                        drawLine(Color(0xFF1769E0), snowCenter, snowCenter + Offset(-6f * scale, 14f * scale), 3.5f * scale, cap = StrokeCap.Round)
                    }
                }
            }
            WeatherVisualFamily.Atmosphere -> {
                if (condition == WeatherCondition.Haze) {
                    drawCircle(Color(0xFFE0B85C), radius = 23f * scale, center = Offset(center.x - 14f * scale, center.y - 10f * scale))
                } else {
                    drawCloud()
                }
                repeat(3) { index ->
                    val y = center.y + (20f + index * 13f) * scale
                    drawLine(
                        color = if (isNight) Color(0xFFD6E2E8) else Color(0xFF78909C),
                        start = Offset(center.x - (38f - index * 7f) * scale, y),
                        end = Offset(center.x + (38f - index * 4f) * scale, y),
                        strokeWidth = 5f * scale,
                        cap = StrokeCap.Round,
                    )
                }
            }
            WeatherVisualFamily.Dust -> {
                val dustColor = if (isNight) Color(0xFFD6B47C) else Color(0xFFB6782B)
                repeat(3) { index ->
                    val y = center.y - 16f * scale + index * 20f * scale
                    drawArc(
                        color = dustColor.copy(alpha = 0.95f - index * 0.16f),
                        startAngle = 190f,
                        sweepAngle = 250f,
                        useCenter = false,
                        topLeft = Offset(center.x - (38f - index * 4f) * scale, y),
                        size = Size((76f - index * 8f) * scale, 28f * scale),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(5f * scale, cap = StrokeCap.Round),
                    )
                }
                repeat(if (condition == WeatherCondition.Sandstorm) 7 else 4) { index ->
                    drawCircle(
                        dustColor.copy(alpha = 0.72f),
                        radius = (2.5f + index % 3) * scale,
                        center = Offset(center.x - 35f * scale + index * 12f * scale, center.y + (index % 2) * 14f * scale),
                    )
                }
            }
        }
    }
}

