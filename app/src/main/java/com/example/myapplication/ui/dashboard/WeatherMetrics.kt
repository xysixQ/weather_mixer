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
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.foundation.lazy.LazyListState
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

internal data class MetricPlacement(
    val detail: DashboardDetail,
    val x: Dp,
    val y: Dp,
)

@Composable
internal fun WeatherMetricsGrid(
    snapshot: WeatherSnapshot,
    isNight: Boolean,
    listState: LazyListState,
    metricOrder: List<DashboardDetail>,
    onMetricOrderChanged: (List<DashboardDetail>) -> Unit,
    editingMetric: DashboardDetail?,
    onEditingMetricChange: (DashboardDetail?) -> Unit,
    onEditingBoundsChange: (Rect) -> Unit,
    onOpenDetail: (DashboardDetail, Rect) -> Unit,
) {
    val weather = snapshot.fused
    val windDirection = snapshot.hourlyForecast.firstOrNull()?.windDirection ?: 0.0
    val resizeOutlineColor = when (weather.condition.visualFamily) {
        WeatherVisualFamily.Clear -> Color(0xFF287FC5)
        WeatherVisualFamily.Cloud, WeatherVisualFamily.Atmosphere -> Color(0xFF55759B)
        WeatherVisualFamily.Rain, WeatherVisualFamily.Snow -> Color(0xFF187FC4)
        WeatherVisualFamily.Thunder -> Color(0xFF7558A8)
        WeatherVisualFamily.Dust -> Color(0xFF9A6A2D)
    }
    var expandedMetric by remember { mutableStateOf<DashboardDetail?>(null) }
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val fullWidth = maxWidth
        val gap = 10.dp
        val cardHeight = 162.dp
        val compactWidth = (fullWidth - 10.dp) / 2f
        val placements = remember(metricOrder, expandedMetric, fullWidth) {
            buildList {
                var y = 0.dp
                var halfRowOccupied = false
                metricOrder.distinct().forEach { detail ->
                    if (detail == expandedMetric) {
                        if (halfRowOccupied) {
                            y += cardHeight + gap
                            halfRowOccupied = false
                        }
                        add(MetricPlacement(detail, 0.dp, y))
                        y += cardHeight + gap
                    } else if (!halfRowOccupied) {
                        add(MetricPlacement(detail, 0.dp, y))
                        halfRowOccupied = true
                    } else {
                        add(MetricPlacement(detail, compactWidth + gap, y))
                        y += cardHeight + gap
                        halfRowOccupied = false
                    }
                }
            }
        }
        val targetGridHeight = (placements.maxOfOrNull { it.y + cardHeight } ?: cardHeight)
        val gridHeight by animateDpAsState(
            targetValue = targetGridHeight,
            animationSpec = tween(430, easing = FastOutSlowInEasing),
            label = "metricGridHeight",
        )
        Box(modifier = Modifier.fillMaxWidth().height(gridHeight)) {
            placements.forEach { placement ->
                key(placement.detail) {
                    val detail = placement.detail
                    val placementSpec = if (editingMetric == detail) tween<Dp>(0) else tween(360, easing = FastOutSlowInEasing)
                    val animatedX by animateDpAsState(placement.x, placementSpec, label = "metricX-${detail.name}")
                    val animatedY by animateDpAsState(placement.y, placementSpec, label = "metricY-${detail.name}")
                    ResizableMetricSlot(
                    modifier = Modifier.offset(
                        x = if (editingMetric == detail) placement.x else animatedX,
                        y = if (editingMetric == detail) placement.y else animatedY,
                    ),
                    detail = detail,
                    listState = listState,
                    itemIndex = metricOrder.indexOf(detail),
                    itemCount = metricOrder.size,
                    compactWidth = compactWidth,
                    expandedWidth = fullWidth,
                    expanded = expandedMetric == detail,
                    editing = editingMetric == detail,
                    outlineColor = resizeOutlineColor,
                    onEditingChange = { onEditingMetricChange(if (it) detail else null) },
                    onEditingBoundsChange = onEditingBoundsChange,
                    onResize = { expandedMetric = if (it) detail else null },
                    onReorderTarget = { targetIndex ->
                        val from = metricOrder.indexOf(detail)
                        val to = targetIndex.coerceIn(0, metricOrder.lastIndex)
                        if (expandedMetric == null && from >= 0 && from != to) {
                            onMetricOrderChanged(metricOrder.toMutableList().apply { add(to, removeAt(from)) })
                            true
                        } else false
                    },
                ) { slotModifier, editing, resizeProgress, onLongPress ->
                    DetailCardAnchor(detail, onOpenDetail, slotModifier) { onClick ->
                        when (detail) {
                            DashboardDetail.Precipitation -> PrecipitationMetricCard(
                                Modifier.fillMaxWidth(), weather,
                                expanded = expandedMetric == detail,
                                expansionProgress = resizeProgress,
                                onClick = { if (!editing) onClick() }, onLongClick = onLongPress,
                            )
                            DashboardDetail.Wind -> WindMetricCard(
                                Modifier.fillMaxWidth(), weather.windKph, windDirection,
                                onClick = { if (!editing) onClick() }, onLongClick = onLongPress,
                            )
                            DashboardDetail.AirQuality -> AirQualityMetricCard(
                                Modifier.fillMaxWidth(), weather.aqi,
                                onClick = { if (!editing) onClick() }, onLongClick = onLongPress,
                            )
                            DashboardDetail.Ultraviolet -> UvMetricCard(
                                Modifier.fillMaxWidth(), weather.uvIndex,
                                onClick = { if (!editing) onClick() }, onLongClick = onLongPress,
                            )
                            DashboardDetail.Humidity -> HumidityMetricCard(
                                Modifier.fillMaxWidth(), weather.humidityPercent, weather.dewPointC,
                                onClick = { if (!editing) onClick() }, onLongClick = onLongPress,
                            )
                            DashboardDetail.Pressure -> PressureMetricCard(
                                Modifier.fillMaxWidth(), weather.pressureHpa,
                                nightCloudTheme = isNight && weather.condition == WeatherCondition.Cloudy,
                                onClick = { if (!editing) onClick() }, onLongClick = onLongPress,
                            )
                            else -> Unit
                        }
                    }
                    }
                }
            }
        }
    }
}

@Composable
internal fun ResizableMetricSlot(
    modifier: Modifier = Modifier,
    detail: DashboardDetail,
    listState: LazyListState,
    itemIndex: Int,
    itemCount: Int,
    compactWidth: Dp,
    expandedWidth: Dp,
    expanded: Boolean,
    editing: Boolean,
    outlineColor: Color,
    onEditingChange: (Boolean) -> Unit,
    onEditingBoundsChange: (Rect) -> Unit,
    onResize: (Boolean) -> Unit,
    onReorderTarget: (Int) -> Boolean,
    reorderEnabled: Boolean = true,
    content: @Composable (
        Modifier,
        editing: Boolean,
        resizeProgress: Float,
        onLongPress: () -> Unit,
    ) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val density = LocalDensity.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    val scope = rememberCoroutineScope()
    var draggingHandle by remember(detail) { mutableStateOf(false) }
    var resizeDistance by remember(detail) { mutableStateOf(Offset.Zero) }
    var reorderDistance by remember(detail) { mutableStateOf(Offset.Zero) }
    var totalReorderDistance by remember(detail) { mutableStateOf(Offset.Zero) }
    var dragStartIndex by remember(detail) { mutableStateOf(itemIndex) }
    var compensatedItemIndex by remember(detail) { mutableIntStateOf(itemIndex) }
    var reordering by remember(detail) { mutableStateOf(false) }
    var metricPointerRootY by remember(detail) { mutableFloatStateOf(0f) }
    var metricAutoScrollSpeed by remember(detail) { mutableFloatStateOf(0f) }
    var slotBoundsInRoot by remember(detail) { mutableStateOf(Rect.Zero) }
    var sizeFraction by remember(detail) { mutableFloatStateOf(if (expanded) 1f else 0f) }
    var frameOffset by remember(detail) { mutableStateOf(Offset.Zero) }
    var thresholdHapticSent by remember(detail) { mutableStateOf(false) }
    val availableWidth = expandedWidth - compactWidth
    val availableWidthPx = with(density) { availableWidth.toPx() }.coerceAtLeast(1f)
    val resizeTravelPx = availableWidthPx
    val interactiveWidth = compactWidth + availableWidth * sizeFraction.coerceIn(0f, 1f)
    val editShape = RoundedCornerShape(30.dp)
    val editOutlineColor = outlineColor.copy(alpha = 0.82f)
    val resistanceLimitPx = with(density) { 13.dp.toPx() }
    val latestItemIndex by rememberUpdatedState(itemIndex)
    val latestOnReorderTarget by rememberUpdatedState(onReorderTarget)
    val reorderCellWidthPx = with(density) { (compactWidth + 10.dp).toPx() }
    val reorderRowHeightPx = with(density) { 172.dp.toPx() }

    SideEffect {
        if (reordering && itemIndex != compensatedItemIndex) {
            val layoutShift = Offset(
                x = (itemIndex % 2 - compensatedItemIndex % 2) * reorderCellWidthPx,
                y = (itemIndex / 2 - compensatedItemIndex / 2) * reorderRowHeightPx,
            )
            reorderDistance -= layoutShift
            compensatedItemIndex = itemIndex
        } else if (!reordering) {
            compensatedItemIndex = itemIndex
        }
    }

    fun requestMetricReorder() {
        val startCenterX = (dragStartIndex % 2) * reorderCellWidthPx +
            with(density) { compactWidth.toPx() } / 2f
        val startCenterY = (dragStartIndex / 2) * reorderRowHeightPx + reorderRowHeightPx / 2f
        val targetColumn = floor(
            (startCenterX + totalReorderDistance.x) / reorderCellWidthPx
        ).toInt().coerceIn(0, 1)
        val targetRow = floor(
            (startCenterY + totalReorderDistance.y) / reorderRowHeightPx
        ).toInt().coerceAtLeast(0)
        val targetIndex = (targetRow * 2 + targetColumn).coerceIn(0, itemCount - 1)
        if (latestOnReorderTarget(targetIndex) && hapticFeedbackEnabled) {
            context.performScaleTick(view, 0.42f)
        }
    }

    LaunchedEffect(reordering, metricAutoScrollSpeed) {
        while (reordering && abs(metricAutoScrollSpeed) > 0.5f) {
            val consumed = listState.scrollBy(metricAutoScrollSpeed)
            if (abs(consumed) < 0.1f) {
                metricAutoScrollSpeed = 0f
                break
            }
            val scrollCompensation = Offset(0f, consumed)
            reorderDistance += scrollCompensation
            totalReorderDistance += scrollCompensation
            requestMetricReorder()
            delay(8L)
        }
    }

    fun resistedOffset(value: Float): Float {
        if (abs(value) < 0.5f) return 0f
        return resistanceLimitPx * value / (abs(value) + resistanceLimitPx * 1.8f)
    }

    fun settleFrameOffset() {
        val start = frameOffset
        scope.launch {
            val xAnimation = ComposeAnimatable(start.x)
            val yAnimation = ComposeAnimatable(start.y)
            launch {
                xAnimation.animateTo(
                    0f,
                    spring(dampingRatio = 0.54f, stiffness = Spring.StiffnessMediumLow),
                ) { frameOffset = frameOffset.copy(x = value) }
            }
            launch {
                yAnimation.animateTo(
                    0f,
                    spring(dampingRatio = 0.54f, stiffness = Spring.StiffnessMediumLow),
                ) { frameOffset = frameOffset.copy(y = value) }
            }
        }
    }

    fun settleReorderOffset() {
        val start = reorderDistance
        scope.launch {
            val xAnimation = ComposeAnimatable(start.x)
            val yAnimation = ComposeAnimatable(start.y)
            launch {
                xAnimation.animateTo(0f, tween(240, easing = FastOutSlowInEasing)) {
                    reorderDistance = reorderDistance.copy(x = value)
                }
            }
            launch {
                yAnimation.animateTo(0f, tween(240, easing = FastOutSlowInEasing)) {
                    reorderDistance = reorderDistance.copy(y = value)
                }
            }
        }
    }

    LaunchedEffect(expanded, draggingHandle) {
        if (!draggingHandle) {
            val animation = ComposeAnimatable(sizeFraction)
            animation.animateTo(
                targetValue = if (expanded) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 430,
                    easing = CubicBezierEasing(0.12f, 0.82f, 0.12f, 1f),
                ),
            ) { sizeFraction = value }
        }
    }

    Box(
        modifier = modifier
            .requiredWidth(interactiveWidth)
            .onGloballyPositioned { coordinates ->
                slotBoundsInRoot = coordinates.boundsInRoot()
            }
            .zIndex(if (editing || abs(reorderDistance.x) + abs(reorderDistance.y) > 0.5f) 8f else 0f)
            .graphicsLayer {
                translationX = reorderDistance.x
                translationY = reorderDistance.y
            }
            .then(if (reorderEnabled) Modifier.pointerInput(detail) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { touchOffset ->
                        onEditingChange(true)
                        reorderDistance = Offset.Zero
                        totalReorderDistance = Offset.Zero
                        dragStartIndex = latestItemIndex
                        compensatedItemIndex = latestItemIndex
                        reordering = true
                        metricPointerRootY = slotBoundsInRoot.top + touchOffset.y
                        metricAutoScrollSpeed = 0f
                        if (hapticFeedbackEnabled) {
                            context.performAppVibration(AppVibration.StrongImpact)
                        }
                    },
                    onDrag = { change, dragAmount ->
                        if (!draggingHandle) {
                            change.consume()
                            reorderDistance += dragAmount
                            totalReorderDistance += dragAmount
                            metricPointerRootY += dragAmount.y
                            val edgeRange = with(density) { 156.dp.toPx() }
                            val viewportEnd = view.height.toFloat()
                            metricAutoScrollSpeed = when {
                                metricPointerRootY < edgeRange -> {
                                    val penetration = (
                                        (edgeRange - metricPointerRootY) / edgeRange
                                        ).coerceIn(0f, 1f)
                                    -(penetration * (8f + 6f * penetration))
                                }
                                metricPointerRootY > viewportEnd - edgeRange -> {
                                    val penetration = (
                                        (metricPointerRootY - (viewportEnd - edgeRange)) / edgeRange
                                        ).coerceIn(0f, 1f)
                                    penetration * (8f + 6f * penetration)
                                }
                                else -> 0f
                            }
                            requestMetricReorder()
                        }
                    },
                    onDragEnd = {
                        totalReorderDistance = Offset.Zero
                        metricAutoScrollSpeed = 0f
                        scope.launch {
                            delay(24L)
                            reordering = false
                            settleReorderOffset()
                        }
                    },
                    onDragCancel = {
                        totalReorderDistance = Offset.Zero
                        metricAutoScrollSpeed = 0f
                        scope.launch {
                            delay(24L)
                            reordering = false
                            settleReorderOffset()
                        }
                    },
                )
            } else Modifier),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    if (editing) onEditingBoundsChange(coordinates.boundsInRoot())
                }
                .graphicsLayer {
                    val widthPx = size.width.coerceAtLeast(1f)
                    val heightPx = size.height.coerceAtLeast(1f)
                    scaleX = (1f + frameOffset.x / widthPx).coerceAtLeast(0.86f)
                    scaleY = (1f + frameOffset.y / heightPx).coerceAtLeast(0.86f)
                    transformOrigin = TransformOrigin(0f, 0f)
                },
        ) {
            content(
                Modifier.fillMaxWidth(),
                editing,
                sizeFraction,
                {
                    onEditingChange(true)
                    if (hapticFeedbackEnabled) {
                        context.performAppVibration(AppVibration.StrongImpact)
                    }
                },
            )
            if (editing) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(1.2.dp, editOutlineColor, editShape),
                )
            }
            if (editing) {
                Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .graphicsLayer {
                        translationX = frameOffset.x
                        translationY = frameOffset.y
                    }
                    .size(30.dp)
                    .zIndex(4f)
                    .pointerInput(detail, expanded) {
                        detectDragGestures(
                            onDragStart = {
                                draggingHandle = true
                                resizeDistance = Offset.Zero
                                frameOffset = Offset.Zero
                                thresholdHapticSent = false
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                resizeDistance += dragAmount
                                val outwardDirection = if (expanded) -1f else 1f
                                val outwardDistance = resizeDistance.x * outwardDirection
                                val progress = (outwardDistance / resizeTravelPx).coerceIn(0f, 1f)
                                sizeFraction = if (expanded) 1f - progress else progress

                                val forbiddenX = when {
                                    outwardDistance < 0f -> resizeDistance.x
                                    outwardDistance > resizeTravelPx ->
                                        (outwardDistance - resizeTravelPx) * outwardDirection
                                    else -> 0f
                                }
                                frameOffset = Offset(
                                    x = resistedOffset(forbiddenX),
                                    y = resistedOffset(resizeDistance.y),
                                )
                                if (progress >= 0.56f && !thresholdHapticSent && hapticFeedbackEnabled) {
                                    context.performScaleTick(view, 0.56f)
                                    thresholdHapticSent = true
                                } else if (progress < 0.48f) {
                                    thresholdHapticSent = false
                                }
                            },
                            onDragEnd = {
                                onResize(sizeFraction >= 0.5f)
                                draggingHandle = false
                                resizeDistance = Offset.Zero
                                settleFrameOffset()
                            },
                            onDragCancel = {
                                draggingHandle = false
                                resizeDistance = Offset.Zero
                                settleFrameOffset()
                            },
                        )
                    },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                shadowElevation = if (draggingHandle) 10.dp else 5.dp,
                border = BorderStroke(1.dp, editOutlineColor),
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.DragHandle,
                            contentDescription = if (expanded) "拖动缩小${detail.label}" else "拖动放大${detail.label}",
                            modifier = Modifier.size(18.dp).rotate(-45f),
                            tint = editOutlineColor,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal fun Modifier.metricCardGestures(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
): Modifier = clickable(onClick = onClick)

internal enum class RainResizePhase {
    Idle,
    Pouring,
    Draining,
}

@Composable
internal fun PrecipitationMetricCard(
    modifier: Modifier = Modifier,
    weather: FusedWeather,
    expanded: Boolean,
    expansionProgress: Float,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val context = LocalContext.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    val targetLevel = (weather.rainProbability / 100.0).toFloat().coerceIn(0f, 1f)
    var dropRevealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { dropRevealed = true }
    val baseWaterLevel by animateFloatAsState(
        targetValue = targetLevel,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "rainWaterLevel",
    )
    var previousExpanded by remember { mutableStateOf(expanded) }
    var resizePhase by remember { mutableStateOf(RainResizePhase.Idle) }
    val transferProgress = remember { ComposeAnimatable(1f) }
    val umbrellaTilt = remember { ComposeAnimatable(0f) }

    LaunchedEffect(expanded) {
        if (expanded == previousExpanded) return@LaunchedEffect
        previousExpanded = expanded
        transferProgress.stop()
        umbrellaTilt.stop()
        transferProgress.snapTo(0f)
        umbrellaTilt.snapTo(0f)
        if (expanded) {
            resizePhase = RainResizePhase.Pouring
            delay(680L)
            umbrellaTilt.animateTo(
                56f,
                tween(360, easing = FastOutSlowInEasing),
            )
            transferProgress.animateTo(
                1f,
                tween(2_400, easing = LinearEasing),
            )
            umbrellaTilt.animateTo(
                0f,
                tween(380, easing = FastOutSlowInEasing),
            )
        } else {
            resizePhase = RainResizePhase.Draining
            delay(460L)
            transferProgress.animateTo(
                0.82f,
                tween(2_050, easing = LinearEasing),
            )
            transferProgress.animateTo(
                1f,
                tween(850, easing = LinearOutSlowInEasing),
            )
        }
        resizePhase = RainResizePhase.Idle
    }
    val widthProgress = expansionProgress.coerceIn(0f, 1f)
    val spreadProgress by animateFloatAsState(
        targetValue = widthProgress,
        animationSpec = tween(170, easing = FastOutSlowInEasing),
        label = "rainHorizontalSpread",
    )
    val expandedVolumeLevel = (baseWaterLevel / (1f + spreadProgress)).coerceIn(0f, 1f)
    val compactVolumeLevel = (baseWaterLevel * 2f / (1f + widthProgress)).coerceIn(0f, 1f)
    val waterLevel = when (resizePhase) {
        RainResizePhase.Pouring -> if (widthProgress < 0.995f) {
            expandedVolumeLevel
        } else {
            androidx.compose.ui.util.lerp(baseWaterLevel / 2f, baseWaterLevel, transferProgress.value)
        }
        RainResizePhase.Draining -> if (widthProgress > 0.005f) {
            compactVolumeLevel
        } else {
            androidx.compose.ui.util.lerp(
                (baseWaterLevel * 2f).coerceAtMost(1f),
                baseWaterLevel,
                transferProgress.value,
            )
        }
        RainResizePhase.Idle -> when {
            !expanded && widthProgress > 0.005f -> expandedVolumeLevel
            expanded && widthProgress < 0.995f -> compactVolumeLevel
            else -> baseWaterLevel
        }
    }

    val dropProgress by animateFloatAsState(
        targetValue = if (dropRevealed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = 0.54f,
            stiffness = Spring.StiffnessLow,
        ),
        label = "rainWaterDrop",
    )
    val rainMotion = rememberRainMotion()
    val umbrellaPouringVisible = resizePhase == RainResizePhase.Pouring && abs(umbrellaTilt.value) > 4f
    LaunchedEffect(umbrellaPouringVisible, hapticFeedbackEnabled) {
        if (!umbrellaPouringVisible || !hapticFeedbackEnabled) return@LaunchedEffect
        while (true) {
            context.performAppVibration(AppVibration.RainPourPulse)
            delay(58L)
        }
    }
    val waveTransition = rememberInfiniteTransition(label = "rainSurfaceWave")
    val wavePhase by waveTransition.animateFloat(
        initialValue = 0f,
        targetValue = (Math.PI * 2.0).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rainSurfacePhase",
    )
    val shape = RoundedCornerShape(30.dp)
    Card(
        modifier = modifier
            .height(162.dp)
            .floatingCardMotion()
            .clip(shape)
            .metricCardGestures(onClick, onLongClick),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val fillHeight = size.height * waterLevel
                val waterTop = -fillHeight + size.height * dropProgress
                val waterBottom = (waterTop + fillHeight).coerceIn(0f, size.height)
                val gravityOffset = rainMotion.tilt * size.height * 0.11f
                val gravityLeftY = waterTop + gravityOffset
                val gravityRightY = waterTop - gravityOffset
                val gravitySlope = (gravityRightY - gravityLeftY) / size.width.coerceAtLeast(1f)
                val primaryWaveHeight = (2.2f + rainMotion.agitation * 13.5f).dp.toPx()
                val secondaryWaveHeight = (0.7f + rainMotion.agitation * 5.5f).dp.toPx()
                val compactEdge = size.width / (1f + widthProgress)
                val rawFrontFraction = if (widthProgress > 0.001f) {
                    (spreadProgress / widthProgress).coerceIn(0f, 1f)
                } else {
                    1f
                }
                fun smoothStep(value: Float): Float {
                    val clamped = value.coerceIn(0f, 1f)
                    return clamped * clamped * (3f - 2f * clamped)
                }
                val flowingIntoNewSpace = widthProgress > 0.005f &&
                    spreadProgress < widthProgress - 0.002f
                val frontFraction = smoothStep(rawFrontFraction / 0.52f)
                val waterRight = if (flowingIntoNewSpace) {
                    compactEdge + (size.width - compactEdge) * frontFraction
                } else {
                    size.width
                }
                val leveling = smoothStep((rawFrontFraction - 0.22f) / 0.78f)
                val flowImpulse = smoothStep(rawFrontFraction / 0.2f) * (1f - leveling)
                val flowTiltHeight = min(
                    25.dp.toPx(),
                    (waterBottom - waterTop).coerceAtLeast(0f) * 0.34f,
                ) * flowImpulse

                fun waveYAt(x: Float): Float {
                    val fraction = (x / size.width).coerceIn(0f, 1f)
                    return sin(wavePhase + fraction * Math.PI.toFloat() * 2f) * primaryWaveHeight +
                        sin(wavePhase * 2f + fraction * Math.PI.toFloat() * 4f) * secondaryWaveHeight
                }

                fun settledSurfaceYAt(x: Float): Float =
                    waterTop + gravitySlope * (x - size.width / 2f) + waveYAt(x)

                fun movingSurfaceYAt(x: Float): Float {
                    val across = ((x / size.width) - 0.5f) * 2f
                    return settledSurfaceYAt(x) + flowTiltHeight * across
                }

                fun Path.appendSmoothSurface(
                    startX: Float,
                    endX: Float,
                    segmentCount: Int,
                    yAt: (Float) -> Float,
                ) {
                    if (endX <= startX + 0.5f) return
                    val segmentWidth = (endX - startX) / segmentCount
                    repeat(segmentCount) { index ->
                        val x0 = startX + segmentWidth * index
                        val x1 = startX + segmentWidth * (index + 1)
                        val sample = min(2.dp.toPx(), segmentWidth * 0.22f).coerceAtLeast(0.5f)
                        val y0 = yAt(x0)
                        val y1 = yAt(x1)
                        val sampleLeft0 = (x0 - sample).coerceAtLeast(startX)
                        val sampleRight0 = (x0 + sample).coerceAtMost(endX)
                        val sampleLeft1 = (x1 - sample).coerceAtLeast(startX)
                        val sampleRight1 = (x1 + sample).coerceAtMost(endX)
                        val slope0 = (yAt(sampleRight0) - yAt(sampleLeft0)) /
                            (sampleRight0 - sampleLeft0).coerceAtLeast(0.5f)
                        val slope1 = (yAt(sampleRight1) - yAt(sampleLeft1)) /
                            (sampleRight1 - sampleLeft1).coerceAtLeast(0.5f)
                        cubicTo(
                            x0 + segmentWidth / 3f,
                            y0 + slope0 * segmentWidth / 3f,
                            x1 - segmentWidth / 3f,
                            y1 - slope1 * segmentWidth / 3f,
                            x1,
                            y1,
                        )
                    }
                }

                val waterPath = Path().apply {
                    moveTo(0f, movingSurfaceYAt(0f))
                    if (flowingIntoNewSpace) {
                        appendSmoothSurface(0f, compactEdge, 8, ::movingSurfaceYAt)
                        val startY = movingSurfaceYAt(compactEdge)
                        val naturalFrontY = movingSurfaceYAt(waterRight)
                        val shallowFrontY = (waterBottom - 1.5.dp.toPx()).coerceAtLeast(naturalFrontY)
                        val frontY = androidx.compose.ui.util.lerp(shallowFrontY, naturalFrontY, leveling)
                        val span = (waterRight - compactEdge).coerceAtLeast(0f)
                        if (span > 0.5f) {
                            val startSample = min(2.dp.toPx(), compactEdge.coerceAtLeast(1f))
                            val startSlope = (movingSurfaceYAt(compactEdge) -
                                movingSurfaceYAt((compactEdge - startSample).coerceAtLeast(0f))) /
                                startSample.coerceAtLeast(0.5f)
                            cubicTo(
                                compactEdge + span * 0.26f,
                                startY + startSlope * span * 0.26f,
                                waterRight - span * 0.2f,
                                frontY,
                                waterRight,
                                frontY,
                            )
                        }
                        val frontDepth = (waterBottom - frontY).coerceAtLeast(0f)
                        val capWidth = min(
                            min(10.dp.toPx(), frontDepth * 0.36f),
                            (size.width - waterRight).coerceAtLeast(0f),
                        )
                        cubicTo(
                            waterRight + capWidth,
                            frontY + frontDepth * 0.2f,
                            waterRight + capWidth,
                            waterBottom - frontDepth * 0.16f,
                            waterRight,
                            waterBottom,
                        )
                    } else {
                        appendSmoothSurface(0f, size.width, 12, ::settledSurfaceYAt)
                        lineTo(size.width, waterBottom)
                    }
                    lineTo(0f, waterBottom)
                    close()
                }
                drawPath(waterPath, Color(0xFF8DD5FF).copy(alpha = 0.42f))
            }
            if (umbrellaPouringVisible) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val visibility = (abs(umbrellaTilt.value) / 56f).coerceIn(0f, 1f)
                    val pivot = Offset(26.dp.toPx(), 24.8.dp.toPx())
                    val rightCanopyLip = Offset(32.6.dp.toPx(), 25.4.dp.toPx())
                    val angle = Math.toRadians(umbrellaTilt.value.toDouble())
                    val relativeX = rightCanopyLip.x - pivot.x
                    val relativeY = rightCanopyLip.y - pivot.y
                    val streamStart = Offset(
                        pivot.x + relativeX * cos(angle).toFloat() - relativeY * sin(angle).toFloat(),
                        pivot.y + relativeX * sin(angle).toFloat() + relativeY * cos(angle).toFloat(),
                    )
                    val surfaceCenterY = size.height * (1f - waterLevel)
                    val surfaceGravityOffset = rainMotion.tilt * size.height * 0.11f
                    fun streamSurfaceYAt(x: Float): Float = surfaceCenterY -
                        (surfaceGravityOffset * 2f / size.width.coerceAtLeast(1f)) *
                        (x - size.width / 2f)
                    val fallHeight = (streamSurfaceYAt(streamStart.x) - streamStart.y)
                        .coerceAtLeast(14.dp.toPx())
                    val horizontalTravel = 8.dp.toPx() + rainMotion.tilt * fallHeight * 0.12f
                    val streamEndX = (streamStart.x + horizontalTravel)
                        .coerceIn(4.dp.toPx(), size.width - 4.dp.toPx())
                    val streamEnd = Offset(streamEndX, streamSurfaceYAt(streamEndX))
                    val actualTravel = streamEnd.x - streamStart.x
                    val controlOne = Offset(
                        streamStart.x + actualTravel * 0.28f,
                        streamStart.y + fallHeight * 0.12f,
                    )
                    val controlTwo = Offset(
                        streamStart.x + actualTravel * 0.72f,
                        streamStart.y + fallHeight * 0.48f,
                    )
                    val streamPath = Path().apply {
                        moveTo(streamStart.x, streamStart.y)
                        cubicTo(
                            controlOne.x,
                            controlOne.y,
                            controlTwo.x,
                            controlTwo.y,
                            streamEnd.x,
                            streamEnd.y,
                        )
                    }
                    drawPath(
                        path = streamPath,
                        color = Color(0xFF52BDF2).copy(alpha = 0.72f * visibility),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round,
                        ),
                    )
                    repeat(3) { index ->
                        val fraction = ((wavePhase / (Math.PI.toFloat() * 2f)) + index / 3f) % 1f
                        val inverse = 1f - fraction
                        val droplet = Offset(
                            inverse * inverse * inverse * streamStart.x +
                                3f * inverse * inverse * fraction * controlOne.x +
                                3f * inverse * fraction * fraction * controlTwo.x +
                                fraction * fraction * fraction * streamEnd.x,
                            inverse * inverse * inverse * streamStart.y +
                                3f * inverse * inverse * fraction * controlOne.y +
                                3f * inverse * fraction * fraction * controlTwo.y +
                                fraction * fraction * fraction * streamEnd.y,
                        )
                        drawCircle(
                            color = Color(0xFFA7E2FF).copy(alpha = 0.8f * visibility),
                            radius = 2.dp.toPx(),
                            center = droplet,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Umbrella,
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = umbrellaTilt.value
                            transformOrigin = TransformOrigin(0.5f, 0.45f)
                        },
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("降雨", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
                Column {
                    AnimatedNumberText(
                        value = weather.rainProbability,
                        formatter = { "${it.roundToInt()}%" },
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "未来 1 小时 ${oneDecimal(weather.rainNextHourMm)} mm",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

internal data class RainMotion(
    val tilt: Float,
    val agitation: Float,
)

@Composable
internal fun rememberRainMotion(): RainMotion {
    val context = LocalContext.current
    var tilt by remember { mutableFloatStateOf(0f) }
    var agitation by remember { mutableFloatStateOf(0f) }
    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val motionSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_GRAVITY -> {
                        val targetTilt = (-event.values[0] / SensorManager.GRAVITY_EARTH)
                            .coerceIn(-1f, 1f)
                        tilt = tilt * 0.8f + targetTilt * 0.2f
                    }
                    Sensor.TYPE_LINEAR_ACCELERATION, Sensor.TYPE_ACCELEROMETER -> {
                        val magnitude = sqrt(
                            event.values[0] * event.values[0] +
                                event.values[1] * event.values[1] +
                                event.values[2] * event.values[2]
                        )
                        val movement = if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                            abs(magnitude - SensorManager.GRAVITY_EARTH)
                        } else {
                            magnitude
                        }
                        val normalized = (movement / 5.2f).coerceIn(0f, 1f)
                        agitation = agitation * 0.78f + normalized * 0.22f
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        gravitySensor?.let { sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        motionSensor?.let { sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }
        onDispose { sensorManager?.unregisterListener(listener) }
    }
    return RainMotion(tilt, agitation)
}

@Composable
internal fun WeatherMetricModule(
    modifier: Modifier = Modifier,
    snapshot: WeatherSnapshot,
    isNight: Boolean,
    listState: LazyListState,
    detail: DashboardDetail,
    compactWidth: Dp,
    expandedWidth: Dp,
    expanded: Boolean,
    editing: Boolean,
    onEditingChange: (Boolean) -> Unit,
    onEditingBoundsChange: (Rect) -> Unit,
    onResize: (Boolean) -> Unit,
    onOpenDetail: (DashboardDetail, Rect) -> Unit,
) {
    val weather = snapshot.fused
    val windDirection = snapshot.hourlyForecast.firstOrNull()?.windDirection ?: 0.0
    val resizeOutlineColor = when (weather.condition.visualFamily) {
        WeatherVisualFamily.Clear -> Color(0xFF287FC5)
        WeatherVisualFamily.Cloud, WeatherVisualFamily.Atmosphere -> Color(0xFF55759B)
        WeatherVisualFamily.Rain, WeatherVisualFamily.Snow -> Color(0xFF187FC4)
        WeatherVisualFamily.Thunder -> Color(0xFF7558A8)
        WeatherVisualFamily.Dust -> Color(0xFF9A6A2D)
    }
    ResizableMetricSlot(
        modifier = modifier,
        detail = detail,
        listState = listState,
        itemIndex = 0,
        itemCount = 1,
        compactWidth = compactWidth,
        expandedWidth = expandedWidth,
        expanded = expanded,
        editing = editing,
        outlineColor = resizeOutlineColor,
        onEditingChange = onEditingChange,
        onEditingBoundsChange = onEditingBoundsChange,
        onResize = onResize,
        onReorderTarget = { false },
        reorderEnabled = false,
    ) { slotModifier, isEditing, resizeProgress, onLongPress ->
        DetailCardAnchor(detail, onOpenDetail, slotModifier) { onClick ->
            when (detail) {
                DashboardDetail.Precipitation -> PrecipitationMetricCard(
                    Modifier.fillMaxWidth(),
                    weather,
                    expanded = expanded,
                    expansionProgress = resizeProgress,
                    onClick = { if (!isEditing) onClick() },
                    onLongClick = onLongPress,
                )
                DashboardDetail.Wind -> WindMetricCard(
                    Modifier.fillMaxWidth(), weather.windKph, windDirection,
                    onClick = { if (!isEditing) onClick() }, onLongClick = onLongPress,
                )
                DashboardDetail.AirQuality -> AirQualityMetricCard(
                    Modifier.fillMaxWidth(), weather.aqi,
                    onClick = { if (!isEditing) onClick() }, onLongClick = onLongPress,
                )
                DashboardDetail.Ultraviolet -> UvMetricCard(
                    Modifier.fillMaxWidth(), weather.uvIndex,
                    onClick = { if (!isEditing) onClick() }, onLongClick = onLongPress,
                )
                DashboardDetail.Humidity -> HumidityMetricCard(
                    Modifier.fillMaxWidth(), weather.humidityPercent, weather.dewPointC,
                    onClick = { if (!isEditing) onClick() }, onLongClick = onLongPress,
                )
                DashboardDetail.Pressure -> PressureMetricCard(
                    Modifier.fillMaxWidth(), weather.pressureHpa,
                    nightCloudTheme = isNight && weather.condition == WeatherCondition.Cloudy,
                    onClick = { if (!isEditing) onClick() }, onLongClick = onLongPress,
                )
                else -> Unit
            }
        }
    }
}

@Composable
internal fun rememberDeviceHeading(): Float {
    val context = LocalContext.current
    var heading by remember { mutableFloatStateOf(0f) }
    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                SensorManager.getOrientation(rotationMatrix, orientation)
                val rawHeading = ((Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0).toFloat()
                val shortestDelta = ((rawHeading - heading + 540f) % 360f) - 180f
                heading = (heading + shortestDelta * 0.28f + 360f) % 360f
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        rotationSensor?.let { sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME) }
        onDispose { sensorManager?.unregisterListener(listener) }
    }
    return heading
}

@Composable
internal fun WindMetricCard(
    modifier: Modifier,
    speedKph: Double,
    direction: Double,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val deviceHeading = rememberDeviceHeading()
    val screenDirection = ((direction.toFloat() - deviceHeading) + 360f) % 360f
    Box(modifier = modifier.height(162.dp), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.size(150.dp).floatingCardMotion(55).clip(CircleShape)
                .metricCardGestures(onClick, onLongClick),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.84f),
            ),
        ) {
            Box(Modifier.fillMaxSize().padding(12.dp)) {
                Text(
                    text = "风况",
                    modifier = Modifier.align(Alignment.TopCenter),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
                val pointerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.46f)
                Canvas(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(78.dp)
                        .rotate(screenDirection),
                ) {
                    val pointer = Path().apply {
                        val centerX = size.width / 2f
                        val top = 3.dp.toPx()
                        val side = 7.dp.toPx()
                        val bottom = size.height - 7.dp.toPx()
                        moveTo(centerX - 3.dp.toPx(), top + 5.dp.toPx())
                        quadraticBezierTo(centerX, top, centerX + 3.dp.toPx(), top + 5.dp.toPx())
                        lineTo(size.width - side - 3.dp.toPx(), bottom - 5.dp.toPx())
                        quadraticBezierTo(size.width - side, bottom, size.width - side - 6.dp.toPx(), bottom)
                        val notchY = size.height * 0.845f
                        cubicTo(
                            size.width - side - 13.dp.toPx(), bottom,
                            centerX + 10.dp.toPx(), notchY,
                            centerX, notchY,
                        )
                        cubicTo(
                            centerX - 10.dp.toPx(), notchY,
                            side + 13.dp.toPx(), bottom,
                            side + 6.dp.toPx(), bottom,
                        )
                        quadraticBezierTo(side, bottom, side + 3.dp.toPx(), bottom - 5.dp.toPx())
                        close()
                    }
                    drawPath(pointer, pointerColor)
                }
                Column(
                    modifier = Modifier.align(Alignment.Center).offset(y = 17.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedNumberText(
                        value = speedKph,
                        formatter = { "${it.roundToInt()} km/h" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        windDirectionLabel(direction),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
internal fun AirQualityMetricCard(
    modifier: Modifier,
    aqi: Int,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val progress = (aqi / 300f).coerceIn(0f, 1f)
    val accent = when {
        aqi <= 50 -> Color(0xFF198754)
        aqi <= 100 -> Color(0xFFD69E00)
        aqi <= 150 -> Color(0xFFE67E22)
        else -> MaterialTheme.colorScheme.error
    }
    val trackColor = accent.copy(alpha = 0.18f)
    Box(modifier = modifier.height(162.dp), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier
                .size(150.dp)
                .floatingCardMotion(110)
                .clip(CircleShape)
                .metricCardGestures(onClick, onLongClick),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
            ),
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Canvas(Modifier.fillMaxSize().padding(7.dp)) {
                    val stroke = 8.dp.toPx()
                    drawArc(
                        color = trackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = accent,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(stroke, cap = StrokeCap.Round),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("空气质量", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    AnimatedNumberText(
                        value = aqi.toDouble(),
                        formatter = { "AQI ${it.roundToInt()}" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        aqiDescription(aqi).substringBefore('，'),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
internal fun UvMetricCard(
    modifier: Modifier,
    uvIndex: Double,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val activeLevel = when {
        uvIndex < 3.0 -> 0
        uvIndex < 6.0 -> 1
        uvIndex < 8.0 -> 2
        uvIndex < 11.0 -> 3
        else -> 4
    }
    val levelColors = listOf(
        Color(0xFF43A96B),
        Color(0xFFD8C84B),
        Color(0xFFE6A236),
        Color(0xFFD9573F),
        Color(0xFF8F1D2C),
    )
    val activeHaloColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f)
    Box(modifier = modifier.height(164.dp), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.size(154.dp).floatingCardMotion(165).clip(BreezyUvShape)
                .metricCardGestures(onClick, onLongClick),
            shape = BreezyUvShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.84f),
            ),
        ) {
            Box(Modifier.fillMaxSize()) {
                Canvas(Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = size.minDimension * 0.425f
                    val indicatorCorners = listOf(8, 7, 6, 5, 4)
                    repeat(5) { index ->
                        val cornerIndex = indicatorCorners[index]
                        val angle = -Math.PI / 2.0 + cornerIndex * Math.PI * 2.0 / 12.0
                        val isActive = index == activeLevel
                        if (isActive) {
                            drawCircle(
                                color = activeHaloColor,
                                radius = 7.dp.toPx(),
                                center = Offset(
                                    center.x + cos(angle).toFloat() * radius,
                                    center.y + sin(angle).toFloat() * radius,
                                ),
                            )
                        }
                        drawCircle(
                            color = levelColors[index].copy(alpha = if (isActive) 1f else 0.32f),
                            radius = if (isActive) 5.4.dp.toPx() else 4.4.dp.toPx(),
                            center = Offset(
                                center.x + cos(angle).toFloat() * radius,
                                center.y + sin(angle).toFloat() * radius,
                            ),
                        )
                    }
                }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("紫外线", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    AnimatedNumberText(
                        value = uvIndex,
                        formatter = { "UV ${oneDecimal(it)}" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
internal fun HumidityMetricCard(
    modifier: Modifier,
    humidityPercent: Int,
    dewPointC: Double?,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val temperatureUnit = LocalTemperatureUnit.current
    val humidityLevel by animateFloatAsState(
        targetValue = (humidityPercent / 100f).coerceIn(0f, 1f),
        animationSpec = tween(1_050, easing = FastOutSlowInEasing),
        label = "humidityLineFill",
    )
    val dashColor = Color(0xFF49AEDD).copy(alpha = 0.52f)
    Card(
        modifier = modifier.height(162.dp).floatingCardMotion(220).clip(RoundedCornerShape(30.dp))
            .metricCardGestures(onClick, onLongClick),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f)),
    ) {
        Box(Modifier.fillMaxSize()) {
            Canvas(Modifier.fillMaxSize()) {
                val fillTop = size.height * (1f - humidityLevel)
                val dashLength = 17.dp.toPx()
                val dashGap = 8.dp.toPx()
                val rowGap = 9.dp.toPx()
                val strokeWidth = 2.4.dp.toPx()
                clipRect(top = fillTop, bottom = size.height) {
                    var row = 0
                    var y = fillTop + rowGap * 0.55f
                    while (y < size.height) {
                        var x = if (row % 2 == 0) 10.dp.toPx() else 10.dp.toPx() - (dashLength + dashGap) / 2f
                        while (x < size.width + dashLength) {
                            drawLine(
                                color = dashColor,
                                start = Offset(x, y),
                                end = Offset(x + dashLength, y),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round,
                            )
                            x += dashLength + dashGap
                        }
                        row += 1
                        y += rowGap
                    }
                }
            }
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.WaterDrop, contentDescription = null, modifier = Modifier.size(21.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(7.dp))
                Text("湿度", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            }
            AnimatedNumberText(
                modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 14.dp),
                value = humidityPercent.toDouble(),
                formatter = { "${it.roundToInt()}%" },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            dewPointC?.let { dewPoint ->
                Text(
                    text = "露点 ${temperatureUnit.format(dewPoint)}",
                    modifier = Modifier.align(Alignment.BottomStart).padding(start = 14.dp, bottom = 12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
internal fun rememberPressureNeedleWobble(): Float {
    val context = LocalContext.current
    val wobble = remember { ComposeAnimatable(0f) }
    var shakeSequence by remember { mutableIntStateOf(0) }
    var shakeDirection by remember { mutableFloatStateOf(1f) }

    DisposableEffect(context) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val motionSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var lastShakeAt = 0L
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val magnitude = sqrt(
                    event.values[0] * event.values[0] +
                        event.values[1] * event.values[1] +
                        event.values[2] * event.values[2]
                )
                val movement = if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    abs(magnitude - SensorManager.GRAVITY_EARTH)
                } else {
                    magnitude
                }
                val now = SystemClock.elapsedRealtime()
                if (movement >= 5.8f && now - lastShakeAt >= 150L) {
                    shakeDirection = if (event.values[0] + event.values[1] >= 0f) 1f else -1f
                    shakeSequence += 1
                    lastShakeAt = now
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        motionSensor?.let {
            sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME)
        }
        onDispose { sensorManager?.unregisterListener(listener) }
    }

    LaunchedEffect(shakeSequence) {
        if (shakeSequence == 0) return@LaunchedEffect
        val direction = shakeDirection
        wobble.stop()
        wobble.snapTo(direction * 0.021f)
        wobble.animateTo(-direction * 0.017f, tween(90, easing = FastOutSlowInEasing))
        wobble.animateTo(direction * 0.013f, tween(105, easing = FastOutSlowInEasing))
        wobble.animateTo(-direction * 0.009f, tween(120, easing = FastOutSlowInEasing))
        wobble.animateTo(direction * 0.006f, tween(140, easing = FastOutSlowInEasing))
        wobble.animateTo(-direction * 0.0035f, tween(165, easing = FastOutSlowInEasing))
        wobble.animateTo(direction * 0.0015f, tween(190, easing = FastOutSlowInEasing))
        wobble.animateTo(0f, tween(240, easing = FastOutSlowInEasing))
    }
    return wobble.value
}

@Composable
internal fun PressureMetricCard(
    modifier: Modifier,
    pressureHpa: Double,
    nightCloudTheme: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val targetProgress = ((pressureHpa - 960.0) / 100.0).toFloat().coerceIn(0f, 1f)
    var gaugeReady by remember { mutableStateOf(false) }
    LaunchedEffect(pressureHpa) { gaugeReady = true }
    val needleProgress by animateFloatAsState(
        targetValue = if (gaugeReady) targetProgress else 0.5f,
        animationSpec = tween(1_350, easing = FastOutSlowInEasing),
        label = "pressureNeedle",
    )
    val needleWobble = rememberPressureNeedleWobble()
    val displayedNeedleProgress = (needleProgress + needleWobble).coerceIn(0f, 1f)
    val gaugeColor = if (nightCloudTheme) Color(0xFF65D5FF) else MaterialTheme.colorScheme.secondary
    val trackColor = if (nightCloudTheme) {
        Color(0xFFC4E2FF).copy(alpha = 0.9f)
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.58f)
    }
    val needleHubColor = if (nightCloudTheme) Color(0xFFF4FAFF) else MaterialTheme.colorScheme.onSecondaryContainer
    val pressureCardColor = if (nightCloudTheme) {
        Color(0xFF203B57).copy(alpha = 0.9f)
    } else {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.82f)
    }
    val pressureContentColor = if (nightCloudTheme) Color(0xFFF1F7FF) else MaterialTheme.colorScheme.onSecondaryContainer
    Box(modifier = modifier.height(162.dp), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.size(150.dp).floatingCardMotion(260).clip(CircleShape)
                .metricCardGestures(onClick, onLongClick),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = pressureCardColor,
                contentColor = pressureContentColor,
            ),
        ) {
            Box(Modifier.fillMaxSize()) {
                Canvas(Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val outerRadius = size.minDimension / 2f - 5.dp.toPx()
                    val radius = outerRadius - 10.dp.toPx()
                    drawCircle(
                        color = trackColor.copy(alpha = 0.42f),
                        radius = outerRadius,
                        center = center,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx()),
                    )
                    drawArc(
                        color = trackColor,
                        startAngle = 140f,
                        sweepAngle = 260f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(4.dp.toPx(), cap = StrokeCap.Round),
                    )
                    repeat(11) { index ->
                        val angle = Math.toRadians((140.0 + index * 26.0))
                        val outer = Offset(
                            center.x + cos(angle).toFloat() * radius,
                            center.y + sin(angle).toFloat() * radius,
                        )
                        val tickLength = if (index % 5 == 0) 8.dp.toPx() else 5.dp.toPx()
                        val inner = Offset(
                            center.x + cos(angle).toFloat() * (radius - tickLength),
                            center.y + sin(angle).toFloat() * (radius - tickLength),
                        )
                        drawLine(trackColor, inner, outer, 2.dp.toPx(), StrokeCap.Round)
                    }
                    val needleAngle = Math.toRadians(140.0 + displayedNeedleProgress * 260.0)
                    val needleEnd = Offset(
                        center.x + cos(needleAngle).toFloat() * radius * 0.72f,
                        center.y + sin(needleAngle).toFloat() * radius * 0.72f,
                    )
                    drawLine(gaugeColor, center, needleEnd, 4.dp.toPx(), StrokeCap.Round)
                    drawCircle(gaugeColor, radius = 6.dp.toPx(), center = center)
                    drawCircle(
                        color = needleHubColor,
                        radius = 2.dp.toPx(),
                        center = center,
                    )
                }
                Text(
                    text = "气压",
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 14.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
                AnimatedNumberText(
                    value = pressureHpa,
                    formatter = { "${it.roundToInt()} hPa" },
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 14.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
