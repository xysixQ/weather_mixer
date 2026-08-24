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
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
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
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
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
import androidx.compose.ui.unit.Constraints
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
internal fun UnifiedDashboardModuleLayout(
    order: List<DashboardBlock>,
    expandedMetric: DashboardDetail?,
    modifier: Modifier = Modifier,
    content: @Composable (DashboardBlock) -> Unit,
) {
    Layout(
        modifier = modifier,
        content = {
            order.forEach { block ->
                key(block) { content(block) }
            }
        },
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val horizontalGap = 10.dp.roundToPx()
        val verticalGap = 16.dp.roundToPx()
        val compactWidth = ((width - horizontalGap) / 2).coerceAtLeast(1)
        val childConstraints = Constraints(
            minWidth = 0,
            maxWidth = width,
            minHeight = 0,
            maxHeight = Constraints.Infinity,
        )
        val placeables = measurables.map { it.measure(childConstraints) }
        val positions = Array(placeables.size) { IntOffset.Zero }
        var y = 0
        var halfRowOccupied = false
        var halfRowHeight = 0

        placeables.forEachIndexed { index, placeable ->
            val block = order[index]
            val occupiesFullRow = !block.isMetric || block.metricDetail == expandedMetric
            if (occupiesFullRow) {
                if (halfRowOccupied) {
                    y += halfRowHeight + verticalGap
                    halfRowOccupied = false
                    halfRowHeight = 0
                }
                positions[index] = IntOffset(0, y)
                y += placeable.height + verticalGap
            } else if (!halfRowOccupied) {
                positions[index] = IntOffset(0, y)
                halfRowOccupied = true
                halfRowHeight = placeable.height
            } else {
                positions[index] = IntOffset(compactWidth + horizontalGap, y)
                halfRowHeight = max(halfRowHeight, placeable.height)
                y += halfRowHeight + verticalGap
                halfRowOccupied = false
                halfRowHeight = 0
            }
        }
        if (halfRowOccupied) y += halfRowHeight + verticalGap
        val height = (y - verticalGap).coerceAtLeast(0)
        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                val position = positions[index]
                placeable.placeRelative(position.x, position.y)
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun DashboardScreen(
    modifier: Modifier = Modifier,
    profile: UserProfile,
    selectedRegion: District,
    snapshot: WeatherSnapshot,
    isRefreshing: Boolean,
    testTimeMillis: Long?,
    testIsNight: Boolean?,
    disabledApiStatus: String?,
    blockOrder: List<DashboardBlock>,
    metricOrder: List<DashboardDetail>,
    onBlockOrderChanged: (List<DashboardBlock>) -> Unit,
    onMetricOrderChanged: (List<DashboardDetail>) -> Unit,
    locationMethod: LocationMethod,
    locationMessage: String?,
    citySearchResults: List<District>,
    savedRegions: List<District>,
    citySearchMessage: String?,
    isCitySearching: Boolean,
    onRegionSelected: (District) -> Unit,
    onSavedRegionRemoved: (District) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenWeatherTest: () -> Unit,
    onOpenTools: () -> Unit,
    onRefresh: () -> Unit,
    onBackgroundMotionChange: (Boolean) -> Unit,
    onCitySearch: (String) -> Unit,
    onLocationMethodChanged: (LocationMethod) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val context = LocalContext.current
    val expandedLayout = LocalConfiguration.current.smallestScreenWidthDp >= 600
    val view = LocalView.current
    val temperatureUnit = LocalTemperatureUnit.current
    val isOnline = rememberIsOnline()
    val density = LocalDensity.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    val listState = rememberLazyListState()
    val reorderScope = rememberCoroutineScope()
    val latestOnBlockOrderChanged by rememberUpdatedState(onBlockOrderChanged)
    var homeDraftOrder by remember { mutableStateOf(blockOrder) }
    val latestHomeDraftOrder by rememberUpdatedState(homeDraftOrder)
    var homeDraggedBlock by remember { mutableStateOf<DashboardBlock?>(null) }
    var homeDragOffset by remember { mutableStateOf(Offset.Zero) }
    var homePointerRoot by remember { mutableStateOf(Offset.Zero) }
    var homeDragDirection by remember { mutableStateOf(Offset.Zero) }
    var homeReorderHapticJob by remember { mutableStateOf<Job?>(null) }
    var homeLastReorderPointer by remember { mutableStateOf(Offset.Unspecified) }
    var homePrimaryPointerId by remember { mutableStateOf<PointerId?>(null) }
    var homeAutoScrollSpeed by remember { mutableFloatStateOf(0f) }
    var suppressDetailUntilMillis by remember { mutableLongStateOf(0L) }
    val homeModuleBounds = remember { mutableMapOf<DashboardBlock, Rect>() }
    var searchExpanded by remember { mutableStateOf(false) }
    var selectedDetail by remember { mutableStateOf<DashboardDetail?>(null) }
    var editingMetric by remember { mutableStateOf<DashboardDetail?>(null) }
    var expandedMetric by remember { mutableStateOf<DashboardDetail?>(null) }
    var editingMetricBounds by remember { mutableStateOf(Rect.Zero) }
    var detailOriginBounds by remember { mutableStateOf(Rect.Zero) }
    var selectedForecastDay by remember { mutableStateOf<DailyForecast?>(null) }
    var selectedForecastHour by remember { mutableStateOf<HourlyForecast?>(null) }
    var displayedDetail by remember { mutableStateOf(DashboardDetail.Advice) }
    var pullDistance by remember { mutableFloatStateOf(0f) }
    val refreshTriggered = remember { mutableStateOf(false) }
    val latestOnRefresh by rememberUpdatedState(onRefresh)
    var systemTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val currentTimeMillis = testTimeMillis ?: systemTimeMillis
    val isNight = testIsNight ?: isNightTime(currentTimeMillis, snapshot.astronomy, snapshot.region.longitude)
    val weatherTextColor = weatherSceneForegroundColor(
        snapshot.fused.condition,
        snapshot.fused.alert.level,
        isNight,
    )
    var expandedTemperatureAnchor by remember { mutableStateOf<Offset?>(null) }
    var compactTemperatureAnchor by remember { mutableStateOf<Offset?>(null) }
    val collapseProgress by remember {
        derivedStateOf {
            val expandedAnchor = expandedTemperatureAnchor
            val compactAnchor = compactTemperatureAnchor
            if (listState.firstVisibleItemIndex > 0) {
                1f
            } else {
                val travel = if (expandedAnchor != null && compactAnchor != null) {
                    abs(expandedAnchor.y - compactAnchor.y).coerceAtLeast(1f)
                } else {
                    260f
                }
                (listState.firstVisibleItemScrollOffset / travel).coerceIn(0f, 1f)
            }
        }
    }
    val compactHeader by remember { derivedStateOf { collapseProgress >= 0.82f } }
    val dashboardContentAlpha by animateFloatAsState(
        targetValue = if ((selectedDetail == null && detailOriginBounds == Rect.Zero) || expandedLayout) 1f else 0f,
        animationSpec = if ((selectedDetail == null && detailOriginBounds == Rect.Zero) || expandedLayout) {
            tween(160, easing = FastOutSlowInEasing)
        } else {
            tween(140, easing = FastOutSlowInEasing)
        },
        label = "dashboardContentBehindDetail",
    )
    val pullTranslation = pullDistance
    val pullThreshold = with(density) { 96.dp.toPx() }
    val maximumPull = with(density) { 176.dp.toPx() }
    val refreshHoldOffset = with(density) { 54.dp.toPx() }

    LaunchedEffect(blockOrder, homeDraggedBlock) {
        if (homeDraggedBlock == null) homeDraftOrder = blockOrder
    }

    suspend fun animatePullTo(target: Float) {
        val animation = ComposeAnimatable(pullDistance)
        animation.animateTo(
            targetValue = target,
            animationSpec = spring(
                dampingRatio = 0.64f,
                stiffness = Spring.StiffnessMediumLow,
            ),
        ) {
            pullDistance = value
        }
        pullDistance = target
    }

    val pullConnection = remember(
        isRefreshing,
        listState,
        pullThreshold,
        maximumPull,
        refreshHoldOffset,
        hapticFeedbackEnabled,
    ) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (
                    source == NestedScrollSource.Drag &&
                    available.y > 0f &&
                    !listState.canScrollBackward &&
                    !isRefreshing
                ) {
                    pullDistance = min(maximumPull, pullDistance + available.y * 0.56f)
                    return Offset(0f, available.y)
                }
                if (available.y < 0 && pullDistance > 0f) {
                    val consumed = max(available.y, -pullDistance)
                    pullDistance += consumed
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (
                    source == NestedScrollSource.Drag &&
                    !listState.canScrollBackward &&
                    available.y > 0 &&
                    !isRefreshing
                ) {
                    pullDistance = min(maximumPull, pullDistance + available.y * 0.48f)
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (pullDistance <= 0f) return Velocity.Zero
                val shouldRefresh = pullDistance >= pullThreshold && !isRefreshing && !refreshTriggered.value
                if (shouldRefresh) {
                    refreshTriggered.value = true
                    if (hapticFeedbackEnabled) {
                        context.performAppVibration(AppVibration.StrongImpact)
                    }
                    latestOnRefresh()
                }
                animatePullTo(if (shouldRefresh) refreshHoldOffset else 0f)
                return available
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (pullDistance > 0f && !isRefreshing) {
                    val shouldRefresh = pullDistance >= pullThreshold && !refreshTriggered.value
                    if (shouldRefresh) {
                        refreshTriggered.value = true
                        if (hapticFeedbackEnabled) {
                            context.performAppVibration(AppVibration.StrongImpact)
                        }
                        latestOnRefresh()
                    }
                    animatePullTo(if (shouldRefresh) refreshHoldOffset else 0f)
                }
                return Velocity.Zero
            }
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            if (pullDistance > refreshHoldOffset) animatePullTo(refreshHoldOffset)
        } else {
            if (pullDistance > 0f) animatePullTo(0f)
            refreshTriggered.value = false
        }
    }

    LaunchedEffect(snapshot.updatedAtMillis) {
        systemTimeMillis = System.currentTimeMillis()
        while (true) {
            delay(30_000)
            systemTimeMillis = System.currentTimeMillis()
        }
    }

    LaunchedEffect(compactHeader, isRefreshing, searchExpanded, selectedDetail) {
        onBackgroundMotionChange(
            !compactHeader && !isRefreshing && !searchExpanded &&
                (selectedDetail == null || expandedLayout)
        )
    }

    fun closeDetail() {
        selectedDetail = null
        reorderScope.launch {
            delay(560L)
            if (selectedDetail == null) detailOriginBounds = Rect.Zero
        }
    }

    var dashboardBackProgress by remember { mutableFloatStateOf(0f) }
    var dashboardBackDirection by remember { mutableFloatStateOf(1f) }
    PredictiveBackHandler(enabled = searchExpanded || selectedDetail != null) { progress ->
        try {
            progress.collect { backEvent ->
                dashboardBackProgress = backEvent.progress
                dashboardBackDirection = if (backEvent.swipeEdge == BackEventCompat.EDGE_RIGHT) -1f else 1f
            }
            if (selectedDetail != null) {
                val completion = ComposeAnimatable(dashboardBackProgress)
                completion.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = ((1f - dashboardBackProgress) * 190f).roundToInt().coerceAtLeast(40),
                        easing = FastOutSlowInEasing,
                    ),
                ) { dashboardBackProgress = value }
                closeDetail()
                delay(540)
            } else {
                searchExpanded = false
            }
        } catch (_: CancellationException) {
            withContext(NonCancellable) {
                val rollback = ComposeAnimatable(dashboardBackProgress)
                rollback.animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = 0.82f,
                        stiffness = Spring.StiffnessMedium,
                    ),
                ) { dashboardBackProgress = value }
            }
        } finally {
            dashboardBackProgress = 0f
        }
    }

    LaunchedEffect(selectedDetail) {
        selectedDetail?.let { displayedDetail = it }
    }

    fun updateHomeBlockTarget(block: DashboardBlock, direction: Offset) {
        if (direction.getDistance() < 0.01f) return
        val currentIndex = latestHomeDraftOrder.indexOf(block)
        if (currentIndex < 0) return
        val currentBounds = homeModuleBounds[block] ?: return
        val minimumPointerTravel = with(density) { 22.dp.toPx() }
        if (homeLastReorderPointer != Offset.Unspecified &&
            (homePointerRoot - homeLastReorderPointer).getDistance() < minimumPointerTravel
        ) return
        val hitSlop = with(density) { 6.dp.toPx() }
        val candidates = latestHomeDraftOrder.mapIndexedNotNull { index, candidate ->
            if (candidate == block) return@mapIndexedNotNull null
            val bounds = homeModuleBounds[candidate] ?: return@mapIndexedNotNull null
            Triple(index, candidate, bounds)
        }
        val target = candidates.filter { (_, _, bounds) ->
            homePointerRoot.x in (bounds.left - hitSlop)..(bounds.right + hitSlop) &&
                homePointerRoot.y in (bounds.top - hitSlop)..(bounds.bottom + hitSlop)
        }.minByOrNull { (_, _, bounds) -> (homePointerRoot - bounds.center).getDistance() }
            ?: return
        val targetIndex = target.first
        if (targetIndex == currentIndex) return
        val targetBounds = target.third
        val horizontalMove = abs(direction.x) > abs(direction.y) * 1.15f
        val crossedTargetCenter = if (horizontalMove) {
            if (targetBounds.center.x >= currentBounds.center.x) {
                homePointerRoot.x > targetBounds.center.x
            } else {
                homePointerRoot.x < targetBounds.center.x
            }
        } else {
            if (targetBounds.center.y >= currentBounds.center.y) {
                homePointerRoot.y > targetBounds.center.y
            } else {
                homePointerRoot.y < targetBounds.center.y
            }
        }
        if (!crossedTargetCenter) return
        if (hapticFeedbackEnabled) {
            homeReorderHapticJob?.cancel()
            homeReorderHapticJob = reorderScope.launch {
                context.performAppVibration(AppVibration.ReorderBuzz)
            }
        }
        homeDraftOrder = moveDashboardBlock(latestHomeDraftOrder, currentIndex, targetIndex)
        homeLastReorderPointer = homePointerRoot
    }

    LaunchedEffect(homeDraggedBlock, homeAutoScrollSpeed) {
        val draggedBlock = homeDraggedBlock
        while (draggedBlock != null && abs(homeAutoScrollSpeed) > 0.5f) {
            val consumed = listState.scrollBy(homeAutoScrollSpeed)
            if (abs(consumed) < 0.1f) {
                homeAutoScrollSpeed = 0f
                break
            }
            homeDragOffset += Offset(0f, consumed)
            updateHomeBlockTarget(draggedBlock, Offset(0f, homeAutoScrollSpeed))
            delay(8L)
        }
    }

    fun openDetail(detail: DashboardDetail, bounds: Rect) {
        if (SystemClock.uptimeMillis() < suppressDetailUntilMillis) return
        if (hapticFeedbackEnabled) {
            context.performAppVibration(AppVibration.StrongImpact)
        }
        detailOriginBounds = bounds
        selectedDetail = detail
    }

    fun Modifier.homeBlockReorderGesture(block: DashboardBlock): Modifier {
        return pointerInput(block) {
            detectDragGesturesAfterLongPress(
                onDragStart = { touchOffset ->
                    if (hapticFeedbackEnabled) context.performAppVibration(AppVibration.StrongImpact)
                    suppressDetailUntilMillis = SystemClock.uptimeMillis() + 1_000L
                    block.metricDetail?.let { editingMetric = it }
                    val bounds = homeModuleBounds[block] ?: Rect.Zero
                    homeDraggedBlock = block
                    homeDragOffset = Offset.Zero
                    homePointerRoot = Offset(bounds.left + touchOffset.x, bounds.top + touchOffset.y)
                    homeDragDirection = Offset.Zero
                    homeLastReorderPointer = Offset.Unspecified
                    homePrimaryPointerId = null
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    homePrimaryPointerId = change.id
                    homeDragOffset += dragAmount
                    homePointerRoot += dragAmount
                    if (dragAmount.getDistance() > 0.01f) homeDragDirection = dragAmount
                    val edgeRange = with(density) { 156.dp.toPx() }
                    val viewportStart = 0f
                    val viewportEnd = view.height.toFloat()
                    homeAutoScrollSpeed = when {
                        homePointerRoot.y < viewportStart + edgeRange -> {
                            val penetration = (
                                (viewportStart + edgeRange - homePointerRoot.y) / edgeRange
                                ).coerceIn(0f, 1f)
                            -(penetration * (8f + 6f * penetration))
                        }
                        homePointerRoot.y > viewportEnd - edgeRange -> {
                            val penetration = (
                                (homePointerRoot.y - (viewportEnd - edgeRange)) / edgeRange
                                ).coerceIn(0f, 1f)
                            penetration * (8f + 6f * penetration)
                        }
                        else -> 0f
                    }
                    updateHomeBlockTarget(block, homeDragDirection)
                },
                onDragEnd = {
                    latestOnBlockOrderChanged(latestHomeDraftOrder)
                    homeAutoScrollSpeed = 0f
                    suppressDetailUntilMillis = SystemClock.uptimeMillis() + 450L
                    val startOffset = homeDragOffset
                    reorderScope.launch {
                        val settle = ComposeAnimatable(0f)
                        settle.animateTo(1f, tween(220, easing = FastOutSlowInEasing)) {
                            homeDragOffset = startOffset * (1f - value)
                        }
                        homeDragOffset = Offset.Zero
                        homeDraggedBlock = null
                        homeDragDirection = Offset.Zero
                        homePrimaryPointerId = null
                    }
                },
                onDragCancel = {
                    homeDraftOrder = blockOrder
                    homeAutoScrollSpeed = 0f
                    suppressDetailUntilMillis = SystemClock.uptimeMillis() + 450L
                    homeDragOffset = Offset.Zero
                    homeDraggedBlock = null
                    homeDragDirection = Offset.Zero
                    homePrimaryPointerId = null
                },
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val dashboardPaneWidth = if (expandedLayout) {
            (maxWidth * 0.44f).coerceIn(360.dp, 520.dp).coerceAtMost(maxWidth)
        } else {
            maxWidth
        }
        Box(
            modifier = Modifier
                .width(dashboardPaneWidth)
                .fillMaxHeight()
                .pointerInput(editingMetric, editingMetricBounds) {
                if (editingMetric != null) {
                    awaitEachGesture {
                        val down = awaitFirstDown(
                            requireUnconsumed = false,
                            pass = PointerEventPass.Initial,
                        )
                        if (!editingMetricBounds.contains(down.position)) {
                            down.consume()
                            editingMetric = null
                            editingMetricBounds = Rect.Zero
                            do {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                event.changes.forEach { it.consume() }
                            } while (event.changes.any { it.pressed })
                        }
                    }
                }
            }
                .pointerInput(homeDraggedBlock, homePrimaryPointerId) {
                    if (homeDraggedBlock != null) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Final)
                                val secondary = event.changes.firstOrNull { change ->
                                    change.pressed && change.id != homePrimaryPointerId && !change.isConsumed
                                }
                                val deltaY = secondary?.positionChange()?.y ?: 0f
                                if (abs(deltaY) > 0.5f) {
                                    secondary?.consume()
                                    val consumed = listState.dispatchRawDelta(-deltaY)
                                    homeDragOffset += Offset(0f, consumed)
                                    homeDraggedBlock?.let { block ->
                                        updateHomeBlockTarget(block, Offset(0f, -deltaY))
                                    }
                                }
                                if (event.changes.none { it.pressed }) break
                            }
                        }
                    }
                },
    ) {
        if (searchExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { searchExpanded = false },
            )
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(pullConnection)
                .graphicsLayer {
                    translationY = pullTranslation
                    alpha = dashboardContentAlpha
                },
            contentPadding = PaddingValues(
                start = 20.dp,
                top = 76.dp,
                end = 20.dp,
                bottom = 28.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                WeatherHero(
                    region = snapshot.region,
                    snapshot = snapshot,
                    profile = profile,
                    currentTimeMillis = currentTimeMillis,
                    isNight = isNight,
                    collapseProgress = collapseProgress,
                    foregroundColor = weatherTextColor,
                    onTemperaturePositioned = { position ->
                        if (expandedTemperatureAnchor == null || (!listState.canScrollBackward && pullDistance < 1f)) {
                            expandedTemperatureAnchor = position
                        }
                    },
                )
            }
            if (snapshot.fused.alert.level != AlertLevel.None) {
                item {
                    DetailCardAnchor(DashboardDetail.Alert, ::openDetail) { onClick ->
                        WeatherAlertCard(alert = snapshot.fused.alert, onClick = onClick)
                    }
                }
            }
            item(key = "dashboard-modules") {
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val fullWidth = maxWidth
                    val compactWidth = (fullWidth - 10.dp) / 2f
                    UnifiedDashboardModuleLayout(
                        order = homeDraftOrder,
                        expandedMetric = expandedMetric,
                        modifier = Modifier.fillMaxWidth(),
                    ) { block ->
                        val isDragging = homeDraggedBlock == block
                        var previousLayoutPosition by remember(block) { mutableStateOf<Offset?>(null) }
                        var placementOffset by remember(block) { mutableStateOf(Offset.Zero) }
                        var placementJob by remember(block) { mutableStateOf<Job?>(null) }
                        val placementScope = rememberCoroutineScope()
                        Box(
                            modifier = Modifier
                                .zIndex(if (isDragging) 7f else 0f)
                                .onGloballyPositioned { coordinates ->
                                    val nextPosition = coordinates.positionInParent()
                                    val previous = previousLayoutPosition
                                    homeModuleBounds[block] = coordinates.boundsInRoot()
                                    if (previous != null) {
                                        val layoutDelta = previous - nextPosition
                                        if (layoutDelta.getDistance() > 0.5f) {
                                            if (isDragging) {
                                                homeDragOffset += layoutDelta
                                            } else {
                                                placementJob?.cancel()
                                                val start = placementOffset + layoutDelta
                                                placementOffset = start
                                                placementJob = placementScope.launch {
                                                    val animation = ComposeAnimatable(0f)
                                                    animation.animateTo(
                                                        1f,
                                                        tween(360, easing = FastOutSlowInEasing),
                                                    ) {
                                                        placementOffset = start * (1f - value)
                                                    }
                                                    placementOffset = Offset.Zero
                                                }
                                            }
                                        }
                                    }
                                    previousLayoutPosition = nextPosition
                                },
                        ) {
                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        val offset = if (isDragging) homeDragOffset else placementOffset
                                        translationX = offset.x
                                        translationY = offset.y
                                        scaleX = if (isDragging) 1.018f else 1f
                                        scaleY = if (isDragging) 1.018f else 1f
                                    }
                                    .homeBlockReorderGesture(block),
                            ) {
                                val metricDetail = block.metricDetail
                                if (metricDetail != null) {
                                    WeatherMetricModule(
                                        snapshot = snapshot,
                                        isNight = isNight,
                                        listState = listState,
                                        detail = metricDetail,
                                        compactWidth = compactWidth,
                                        expandedWidth = fullWidth,
                                        expanded = expandedMetric == metricDetail,
                                        editing = editingMetric == metricDetail,
                                        onEditingChange = { enabled ->
                                            editingMetric = if (enabled) metricDetail else null
                                        },
                                        onEditingBoundsChange = { editingMetricBounds = it },
                                        onResize = { expanded ->
                                            expandedMetric = if (expanded) metricDetail else null
                                        },
                                        onOpenDetail = ::openDetail,
                                    )
                                } else {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        when (block) {
                                            DashboardBlock.Advice -> DetailCardAnchor(
                                                DashboardDetail.Advice,
                                                ::openDetail,
                                            ) { onClick ->
                                                AdviceCard(
                                                    title = "个性化建议",
                                                    subtitle = "${profile.occupation.label} · ${profile.commuteMode.label}",
                                                    advice = snapshot.advice,
                                                    onClick = onClick,
                                                )
                                            }
                                            DashboardBlock.Daily -> if (snapshot.dailyForecast.isNotEmpty()) {
                                                DetailCardAnchor(DashboardDetail.Daily, ::openDetail) { onClick ->
                                                    DailyForecastCard(
                                                        days = snapshot.dailyForecast,
                                                        onClick = {
                                                            selectedForecastDay = snapshot.dailyForecast.firstOrNull { !it.isYesterday }
                                                                ?: snapshot.dailyForecast.firstOrNull()
                                                            selectedForecastHour = null
                                                            onClick()
                                                        },
                                                        onDayClick = { day ->
                                                            selectedForecastDay = day
                                                            selectedForecastHour = null
                                                            onClick()
                                                        },
                                                    )
                                                }
                                            }
                                            DashboardBlock.Hourly -> if (snapshot.hourlyForecast.isNotEmpty()) {
                                                DetailCardAnchor(DashboardDetail.Hourly, ::openDetail) { onClick ->
                                                    HourlyForecastCard(
                                                        snapshot.hourlyForecast,
                                                        snapshot.astronomy,
                                                        snapshot.region.longitude,
                                                        onClick = {
                                                            selectedForecastHour = null
                                                            selectedForecastDay = null
                                                            onClick()
                                                        },
                                                        onHourClick = { hour ->
                                                            selectedForecastHour = hour
                                                            selectedForecastDay = null
                                                            onClick()
                                                        },
                                                    )
                                                }
                                            }
                                            DashboardBlock.Astronomy -> DetailCardAnchor(
                                                DashboardDetail.Astronomy,
                                                ::openDetail,
                                            ) { onClick ->
                                                AstronomyCard(snapshot.astronomy, snapshot.region.longitude, onClick)
                                            }
                                            DashboardBlock.Sources -> DetailCardAnchor(
                                                DashboardDetail.Sources,
                                                ::openDetail,
                                            ) { onClick -> SourceStatusCard(snapshot, onClick) }
                                            DashboardBlock.Fusion -> DetailCardAnchor(
                                                DashboardDetail.Fusion,
                                                ::openDetail,
                                            ) { onClick -> SourceFusionCard(snapshot, onClick) }
                                            @Suppress("DEPRECATION")
                                            DashboardBlock.Metrics,
                                            DashboardBlock.Precipitation,
                                            DashboardBlock.Wind,
                                            DashboardBlock.AirQuality,
                                            DashboardBlock.Ultraviolet,
                                            DashboardBlock.Humidity,
                                            DashboardBlock.Pressure -> Unit
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item(key = "dashboard-footer") {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.28f),
                    )
                    Text(
                        "Made by xysixQ@2026",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.64f),
                    )
                }
            }
        }
        val expandedAnchor = expandedTemperatureAnchor
        val compactAnchor = compactTemperatureAnchor
        val dockingStartProgress = if (expandedAnchor != null && compactAnchor != null) {
            val stagingY = compactAnchor.y + with(density) { 42.dp.toPx() }
            val totalTravel = expandedAnchor.y - compactAnchor.y
            if (abs(totalTravel) > 1f) {
                ((expandedAnchor.y - stagingY) / totalTravel).coerceIn(0.45f, 0.82f)
            } else {
                0.68f
            }
        } else {
            0.68f
        }
        val dockingProgress = (
            (collapseProgress - dockingStartProgress) / (1f - dockingStartProgress)
            ).coerceIn(0f, 1f)
        val pullIndicatorProgress = (pullDistance / pullThreshold).coerceIn(0f, 1f)
        AnimatedVisibility(
            visible = pullDistance > 1f || isRefreshing,
            enter = fadeIn(tween(160)),
            exit = fadeOut(tween(180)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 10.dp)
                .offset { IntOffset(0, (pullDistance * 0.34f).roundToInt()) }
                .zIndex(7f),
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 6.dp,
                shadowElevation = 3.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp,
                        )
                    } else {
                        CircularProgressIndicator(
                            progress = { pullIndicatorProgress },
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp,
                        )
                    }
                }
            }
        }
        LocationSearchControl(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                .zIndex(4f),
            expanded = searchExpanded,
            compactLabel = snapshot.region.locationButtonName,
            temperatureText = temperatureUnit.format(snapshot.fused.temperatureC),
            headerProgress = dockingProgress,
            onCompactTemperaturePositioned = { compactTemperatureAnchor = it },
            predictiveBackProgress = if (searchExpanded) dashboardBackProgress else 0f,
            predictiveBackDirection = dashboardBackDirection,
            refreshLabel = if (isRefreshing) "正在刷新" else formatRefreshAge(snapshot.updatedAtMillis, currentTimeMillis),
            refreshSecondaryLabel = apiFailureStatusForRegion(disabledApiStatus, selectedRegion),
            refreshLabelColor = weatherTextColor.copy(alpha = 0.78f),
            onExpandedChange = { searchExpanded = it },
            selectedRegion = selectedRegion,
            locationMethod = locationMethod,
            locationMessage = locationMessage,
            citySearchResults = citySearchResults,
            savedRegions = savedRegions,
            citySearchMessage = citySearchMessage,
            isCitySearching = isCitySearching,
            onRegionSelected = onRegionSelected,
            onSavedRegionRemoved = onSavedRegionRemoved,
            onCitySearch = onCitySearch,
            onLocationMethodChanged = onLocationMethodChanged,
            onUseCurrentLocation = onUseCurrentLocation,
            onPermissionDenied = onPermissionDenied,
        )
        AnimatedVisibility(
            visible = !isOnline && !searchExpanded && (selectedDetail == null || expandedLayout),
            enter = fadeIn(tween(220)) + slideInVertically(tween(320)) { -it / 2 },
            exit = fadeOut(tween(160)) + slideOutVertically(tween(240)) { -it / 2 },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 66.dp)
                .zIndex(8f),
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.94f),
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                tonalElevation = 6.dp,
                shadowElevation = 3.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(Icons.Filled.Cloud, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("离线模式 · 显示缓存数据", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        if (expandedAnchor != null && compactAnchor != null) {
            val scrollProgress = collapseProgress.coerceIn(0f, 1f)
            val stagingY = compactAnchor.y + with(density) { 42.dp.toPx() }
            val firstStageProgress = if (dockingStartProgress > 0f) {
                (scrollProgress / dockingStartProgress).coerceIn(0f, 1f)
            } else {
                1f
            }
            val temperatureX = if (dockingProgress <= 0f) {
                expandedAnchor.x
            } else {
                expandedAnchor.x + (compactAnchor.x - expandedAnchor.x) * dockingProgress
            }
            val temperatureY = if (dockingProgress <= 0f) {
                expandedAnchor.y + (stagingY - expandedAnchor.y) * firstStageProgress
            } else {
                stagingY + (compactAnchor.y - stagingY) * dockingProgress
            }
            val temperatureScale = 1f - dockingProgress * (1f - 16f / 92f)
            val compactLineHeightCorrection = with(density) { 2.dp.toPx() } * dockingProgress
            val temperatureColorProgress = ((dockingProgress - 0.38f) / 0.62f).coerceIn(0f, 1f)
            AnimatedNumberText(
                value = snapshot.fused.temperatureC,
                formatter = { temperatureUnit.format(it) },
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 92.sp, lineHeight = 92.sp),
                fontWeight = FontWeight.Bold,
                color = lerp(weatherTextColor, MaterialTheme.colorScheme.onSurface, temperatureColorProgress),
                maxLines = 1,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = temperatureX.roundToInt(),
                            y = (temperatureY + compactLineHeightCorrection + pullTranslation).roundToInt(),
                        )
                    }
                    .graphicsLayer {
                        scaleX = temperatureScale
                        scaleY = temperatureScale
                        alpha = if (searchExpanded) 0f else 1f
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
                    .zIndex(6f),
            )
        }
        AnimatedVisibility(
            visible = !searchExpanded && (selectedDetail == null || expandedLayout),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 8.dp, end = 18.dp)
                .zIndex(5f),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOpenTools,
                    modifier = Modifier.semantics { contentDescription = "小工具" },
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = null, tint = weatherTextColor)
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription = "设置"
                            onClick {
                                onOpenSettings()
                                true
                            }
                        }
                        .pointerInput(onOpenSettings, onOpenWeatherTest) {
                            while (true) {
                                val down = awaitPointerEventScope {
                                    awaitFirstDown(requireUnconsumed = false).also { it.consume() }
                                }
                                val up = withTimeoutOrNull(5_000L) {
                                    awaitPointerEventScope { waitForUpOrCancellation() }
                                }
                                if (up == null) {
                                    if (hapticFeedbackEnabled) {
                                        context.performAppVibration(AppVibration.StrongImpact)
                                    }
                                    onOpenWeatherTest()
                                    awaitPointerEventScope { waitForUpOrCancellation() }
                                } else {
                                    when (settingsPressAction(up.uptimeMillis - down.uptimeMillis)) {
                                        SettingsPressAction.OpenSettings -> onOpenSettings()
                                        SettingsPressAction.OpenWeatherTest -> onOpenWeatherTest()
                                        SettingsPressAction.Ignore -> Unit
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.Settings, contentDescription = null, tint = weatherTextColor)
                }
            }
        }
        AnimatedVisibility(
            visible = selectedDetail != null && !expandedLayout,
            enter = EnterTransition.None,
            exit = ExitTransition.None,
            modifier = Modifier.fillMaxSize().zIndex(12f),
        ) {
            val expansionProgress by transition.animateFloat(
                transitionSpec = {
                    if (targetState == EnterExitState.Visible) {
                        tween(690, easing = CubicBezierEasing(0.05f, 0.72f, 0.12f, 1f))
                    } else {
                        tween(540, easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f))
                    }
                },
                label = "detailContainerExpansion",
            ) { state -> if (state == EnterExitState.Visible) 1f else 0f }
            val containerProgress = min(expansionProgress, 1f - dashboardBackProgress)
            val detailContentAlpha = if (transition.targetState == EnterExitState.Visible) {
                ((containerProgress - 0.18f) / 0.54f).coerceIn(0f, 1f)
            } else {
                ((containerProgress - 0.72f) / 0.18f).coerceIn(0f, 1f)
            }
            val detailContainerAlpha = if (transition.targetState == EnterExitState.Visible) {
                (containerProgress / 0.34f).coerceIn(0f, 1f)
            } else {
                (containerProgress / 0.24f).coerceIn(0f, 1f)
            }
            val activeDetail = selectedDetail ?: displayedDetail
            val detailContainerColor = MaterialTheme.colorScheme.surface.copy(
                alpha = activeDetail.sourceContainerAlpha,
            )
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
            ) {
                val screenWidth = constraints.maxWidth.toFloat().coerceAtLeast(1f)
                val screenHeight = constraints.maxHeight.toFloat().coerceAtLeast(1f)
                val validOrigin = detailOriginBounds.width > 0f && detailOriginBounds.height > 0f
                val startWidth = if (validOrigin) detailOriginBounds.width else screenWidth * 0.72f
                val startHeight = if (validOrigin) detailOriginBounds.height else screenHeight * 0.18f
                val startLeft = if (validOrigin) detailOriginBounds.left else screenWidth * 0.14f
                val startTop = if (validOrigin) detailOriginBounds.top else screenHeight * 0.41f
                val panelMargin = with(density) { 20.dp.toPx() }
                val targetLeft = panelMargin
                val targetTop = WindowInsets.statusBars.getTop(density) + with(density) { 76.dp.toPx() }
                val targetWidth = screenWidth - panelMargin * 2f
                val targetHeight = screenHeight - targetTop - panelMargin
                val animatedLeft = startLeft + (targetLeft - startLeft) * containerProgress
                val animatedTop = startTop + (targetTop - startTop) * containerProgress
                val animatedWidth = startWidth + (targetWidth - startWidth) * containerProgress
                val animatedHeight = startHeight + (targetHeight - startHeight) * containerProgress
                val animatedCorner = 30.dp + 2.dp * containerProgress
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(animatedLeft.roundToInt(), animatedTop.roundToInt())
                        }
                        .width(with(density) { animatedWidth.toDp() })
                        .height(with(density) { animatedHeight.toDp() })
                        .graphicsLayer {
                            shape = RoundedCornerShape(animatedCorner)
                            clip = true
                        }
                        .background(detailContainerColor.copy(alpha = detailContainerColor.alpha * detailContainerAlpha)),
                ) {
                    DashboardDetailScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = detailContentAlpha },
                        detail = selectedDetail ?: displayedDetail,
                        snapshot = snapshot,
                        profile = profile,
                        selectedForecastDay = selectedForecastDay,
                        selectedForecastHour = selectedForecastHour,
                        containerAlpha = activeDetail.sourceContainerAlpha,
                        onSelectedForecastDayChange = { selectedForecastDay = it },
                        onBack = { closeDetail() },
                    )
                }
            }
        }
        }
        if (expandedLayout) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(maxWidth - dashboardPaneWidth)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedContent(
                    targetState = selectedDetail,
                    transitionSpec = {
                        (fadeIn(tween(360)) + slideInHorizontally(tween(420)) { it / 12 }) togetherWith
                            fadeOut(tween(180))
                    },
                    label = "tabletDashboardDetail",
                ) { detail ->
                    if (detail == null) {
                        TabletDetailPlaceholder(color = weatherTextColor)
                    } else {
                        Surface(
                            modifier = Modifier
                                .padding(start = 12.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
                                .statusBarsPadding()
                                .navigationBarsPadding()
                                .fillMaxSize(),
                            shape = RoundedCornerShape(32.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = detail.sourceContainerAlpha),
                            shadowElevation = 8.dp,
                        ) {
                            DashboardDetailScreen(
                                modifier = Modifier.fillMaxSize(),
                                detail = detail,
                                snapshot = snapshot,
                                profile = profile,
                                selectedForecastDay = selectedForecastDay,
                                selectedForecastHour = selectedForecastHour,
                                containerAlpha = detail.sourceContainerAlpha,
                                onSelectedForecastDayChange = { selectedForecastDay = it },
                                onBack = { closeDetail() },
                            )
                        }
                    }
                }
            }
        }
    }
}

private var _menu: ImageVector? = null

public val Icons.Filled.Menu: ImageVector
    get() {
        if (_menu != null) {
            return _menu!!
        }
        _menu = materialIcon(name = "Filled.Menu") {
            materialPath {
                moveTo(2.0f, 6.0f)
                horizontalLineToRelative(20.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineTo(2.0f)
                close()

                moveTo(3.0f, 13.0f)
                horizontalLineToRelative(18.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineTo(3.0f)
                close()

                moveTo(2.0f, 20.0f)
                horizontalLineToRelative(20.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineTo(2.0f)
                close()
            }
        }
        return _menu!!
    }


@Composable
private fun TabletDetailPlaceholder(color: Color) {
    Canvas(modifier = Modifier.size(112.dp)) {
        val strokeWidth = 7.dp.toPx()
        val iconColor = color.copy(alpha = 0.58f)
        drawCircle(
            color = iconColor,
            radius = 5.5.dp.toPx(),
            center = Offset(size.width * 0.28f, size.height * 0.36f),
        )
        drawCircle(
            color = iconColor,
            radius = 5.5.dp.toPx(),
            center = Offset(size.width * 0.28f, size.height * 0.64f),
        )
        drawArc(
            color = iconColor,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.34f, size.height * 0.16f),
            size = Size(size.width * 0.44f, size.height * 0.68f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
            ),
        )
    }
}

@Composable
internal fun DashboardDetailScreen(
    modifier: Modifier = Modifier,
    detail: DashboardDetail,
    snapshot: WeatherSnapshot,
    profile: UserProfile,
    selectedForecastDay: DailyForecast?,
    selectedForecastHour: HourlyForecast?,
    containerAlpha: Float,
    onSelectedForecastDayChange: (DailyForecast) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val temperatureUnit = LocalTemperatureUnit.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    val weather = snapshot.fused
    val selectedDayIndex = selectedForecastDay?.let { selected ->
        snapshot.dailyForecast.indexOfFirst { it.timeMillis == selected.timeMillis }
    } ?: -1
    val sourceNames = remember(detail, snapshot) {
        val matching = when (detail) {
            DashboardDetail.Alert -> snapshot.readings.filter { it.alert != null }
            DashboardDetail.Daily -> snapshot.readings.filter { it.dailyForecast.isNotEmpty() }
            DashboardDetail.Hourly -> snapshot.readings.filter { it.hourlyForecast.isNotEmpty() }
            DashboardDetail.Precipitation -> snapshot.readings.filter { it.rainProbability != null || it.rainNextHourMm != null }
            DashboardDetail.Wind -> snapshot.readings.filter { it.windKph != null }
            DashboardDetail.AirQuality -> snapshot.readings.filter { it.aqi != null || it.pm25 != null }
            DashboardDetail.Ultraviolet -> snapshot.readings.filter { it.uvIndex != null }
            DashboardDetail.Humidity -> snapshot.readings.filter { it.humidityPercent != null || it.dewPointC != null }
            DashboardDetail.Pressure -> snapshot.readings.filter { it.pressureHpa != null }
            DashboardDetail.Astronomy -> snapshot.readings.filter { it.astronomy != AstronomyInfo.Empty }
            DashboardDetail.Sources, DashboardDetail.Fusion -> snapshot.readings
            DashboardDetail.Advice -> emptyList()
        }
        matching.map { it.source.displayName }.distinct()
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        if (hapticFeedbackEnabled) {
                            context.performAppVibration(AppVibration.StrongImpact)
                        }
                        onBack()
                    },
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回天气")
                }
                Spacer(Modifier.width(8.dp))
                Text(detail.label, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = containerAlpha),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text(
                        text = detailIntroduction(detail),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f))
                    when (detail) {
                        DashboardDetail.Alert -> {
                            Text(weather.alert.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(weather.alert.detail, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        DashboardDetail.Advice -> {
                            Text("${profile.occupation.label} · ${profile.commuteMode.label}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            snapshot.advice.sortedByDescending { it.level.ordinal }.forEach { AdviceItem(it) }
                        }
                        DashboardDetail.Daily -> if (selectedForecastDay != null) {
                            val day = selectedForecastDay
                            var dailySwipeDistance by remember(day.timeMillis) { mutableFloatStateOf(0f) }
                            Row(
                                modifier = Modifier.pointerInput(day.timeMillis, selectedDayIndex, snapshot.dailyForecast) {
                                    detectDragGestures(
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dailySwipeDistance += dragAmount.x
                                        },
                                        onDragEnd = {
                                            val threshold = with(density) { 46.dp.toPx() }
                                            val nextIndex = when {
                                                dailySwipeDistance < -threshold -> (selectedDayIndex + 1)
                                                dailySwipeDistance > threshold -> (selectedDayIndex - 1)
                                                else -> selectedDayIndex
                                            }
                                            if (nextIndex in snapshot.dailyForecast.indices && nextIndex != selectedDayIndex) {
                                                onSelectedForecastDayChange(snapshot.dailyForecast[nextIndex])
                                            }
                                            dailySwipeDistance = 0f
                                        },
                                        onDragCancel = { dailySwipeDistance = 0f },
                                    )
                                },
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                WeatherMiniIcon(day.condition)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(forecastDayLabel(day).replace("\n", " "), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Text(day.condition.label, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(
                                    enabled = selectedDayIndex > 0,
                                    onClick = { onSelectedForecastDayChange(snapshot.dailyForecast[selectedDayIndex - 1]) },
                                ) {
                                    Icon(Icons.Filled.ChevronLeft, contentDescription = "前一天")
                                }
                                IconButton(
                                    enabled = selectedDayIndex in 0 until snapshot.dailyForecast.lastIndex,
                                    onClick = { onSelectedForecastDayChange(snapshot.dailyForecast[selectedDayIndex + 1]) },
                                ) {
                                    Icon(Icons.Filled.ChevronRight, contentDescription = "后一天")
                                }
                            }
                            DetailDivider()
                            DetailValueRow("温度", "${temperatureUnit.format(day.lowC)} ~ ${temperatureUnit.format(day.highC)}")
                            DetailDivider()
                            DetailValueRow("降水概率", day.rainProbability?.let { "${it.roundToInt()}%" } ?: "暂无数据")
                            DetailDivider()
                            DetailValueRow("风速", day.windKph?.let { "${oneDecimal(it)} km/h" } ?: "暂无数据")
                            DetailDivider()
                            DetailValueRow("空气质量", day.aqi?.let { "AQI $it" } ?: "暂无数据")
                            DetailDivider()
                            DetailValueRow("日出 / 日落", "${day.sunrise ?: "--:--"} / ${day.sunset ?: "--:--"}")
                        } else {
                            snapshot.dailyForecast.forEachIndexed { index, day ->
                                if (index > 0) {
                                    DetailDivider()
                                }
                                DetailValueRow(
                                    label = forecastDayLabel(day).replace("\n", " "),
                                    value = "${temperatureUnit.format(day.lowC)} ~ ${temperatureUnit.format(day.highC)} · ${day.condition.label}",
                                )
                            }
                        }
                        DashboardDetail.Hourly -> if (selectedForecastHour != null) {
                            val hour = selectedForecastHour
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                WeatherMiniIcon(hour.condition)
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(formatForecastTime(hour.timeMillis), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Text(hour.condition.label, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            DetailDivider()
                            DetailValueRow("温度", temperatureUnit.format(hour.temperatureC))
                            DetailDivider()
                            DetailValueRow("降水概率", hour.rainProbability?.let { "${it.roundToInt()}%" } ?: "暂无数据")
                            DetailDivider()
                            DetailValueRow("风速", hour.windKph?.let { "${oneDecimal(it)} km/h" } ?: "暂无数据")
                            DetailDivider()
                            DetailValueRow("风向", hour.windDirection?.let { "${it.roundToInt()}°" } ?: "暂无数据")
                            DetailDivider()
                            DetailValueRow("空气质量", hour.aqi?.let { "AQI $it" } ?: "暂无数据")
                        } else {
                            snapshot.hourlyForecast.take(24).forEachIndexed { index, hour ->
                                if (index > 0) DetailDivider()
                                DetailValueRow(
                                    label = formatForecastTime(hour.timeMillis),
                                    value = "${temperatureUnit.format(hour.temperatureC)} · ${hour.condition.label} · 降雨 ${hour.rainProbability?.roundToInt() ?: 0}% · 风 ${hour.windKph?.let { oneDecimal(it) } ?: "-"} km/h",
                                )
                            }
                        }
                        DashboardDetail.Precipitation -> {
                            AnimatedNumberText(
                                value = weather.rainProbability,
                                formatter = { "${it.roundToInt()}%" },
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            DetailValueRow("未来一小时雨量", "${oneDecimal(weather.rainNextHourMm)} mm")
                            Text("概率表示当前时段出现降水的可能性；雨量表示预计累计降水深度。", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        DashboardDetail.Wind -> {
                            AnimatedNumberText(
                                value = weather.windKph,
                                formatter = { "${it.roundToInt()} km/h" },
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Text("风速由可用天气源加权融合。逐小时风况可在逐小时预报中查看。", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        DashboardDetail.AirQuality -> {
                            AnimatedNumberText(
                                value = weather.aqi.toDouble(),
                                formatter = { "AQI ${it.roundToInt()}" },
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(aqiDescription(weather.aqi), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        DashboardDetail.Ultraviolet -> {
                            AnimatedNumberText(
                                value = weather.uvIndex,
                                formatter = { "UV ${oneDecimal(it)}" },
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(uvDescription(weather.uvIndex), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        DashboardDetail.Humidity -> {
                            AnimatedNumberText(
                                value = weather.humidityPercent.toDouble(),
                                formatter = { "${it.roundToInt()}%" },
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                            )
                            weather.dewPointC?.let { dewPoint ->
                                DetailValueRow("露点", temperatureUnit.format(dewPoint, decimal = true))
                            }
                            Text("相对湿度表示当前空气距离饱和状态的百分比。", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        DashboardDetail.Pressure -> {
                            AnimatedNumberText(
                                value = weather.pressureHpa,
                                formatter = { "${it.roundToInt()} hPa" },
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Text("气压值由可用天气源统一换算为百帕后加权融合。", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        DashboardDetail.Astronomy -> {
                            DetailValueRow("日出", snapshot.astronomy.sunrise ?: "--:--")
                            DetailValueRow("日落", snapshot.astronomy.sunset ?: "--:--")
                            DetailValueRow("月出", snapshot.astronomy.moonrise ?: "--:--")
                            DetailValueRow("月落", snapshot.astronomy.moonset ?: "--:--")
                            DetailValueRow("月相", snapshot.astronomy.moonPhase ?: "暂无数据")
                        }
                        DashboardDetail.Sources -> snapshot.readings.forEach { reading ->
                            DetailValueRow(
                                reading.source.displayName,
                                "${reading.temperatureC?.let { temperatureUnit.format(it, decimal = true) } ?: "-"} · ${reading.source.category}",
                            )
                        }
                        DashboardDetail.Fusion -> {
                            Text(snapshot.fusionSummary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            snapshot.sourceWeights.forEach { SourceWeightRow(it) }
                            snapshot.anomalyNotes.forEach { Text(it, style = MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                HorizontalDivider()
                Text("信息来源", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = if (detail == DashboardDetail.Advice) {
                        "多源融合天气结果、本地用户画像与出行规则"
                    } else {
                        sourceNames.ifEmpty { listOf("当前融合天气结果") }.joinToString(" · ")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

internal fun detailIntroduction(detail: DashboardDetail): String = when (detail) {
    DashboardDetail.Alert -> "天气提醒汇总可能影响安全与出行的预警信息，实际行动请以属地官方发布为准。"
    DashboardDetail.Advice -> "个性化建议根据当前融合天气、职业、通勤方式和已选择的敏感因素生成。"
    DashboardDetail.Daily -> "每日预报用于比较未来各天的高低温、天气、降水、风况与日照时间。"
    DashboardDetail.Hourly -> "逐小时预报展示一天内天气和温度的变化，适合安排具体出行时段。"
    DashboardDetail.Precipitation -> "降水概率表示某时段出现降水的可能性，预计雨量表示对应时段的累计降水深度。"
    DashboardDetail.Wind -> "风况由风速和风向共同描述；风向表示风吹来的方向，阵风可能高于平均风速。"
    DashboardDetail.AirQuality -> "空气质量指数综合反映主要污染物水平，数值越高，户外暴露风险通常越大。"
    DashboardDetail.Ultraviolet -> "紫外线指数衡量日晒强度，等级升高时应缩短直晒时间并加强遮挡。"
    DashboardDetail.Humidity -> "相对湿度表示空气接近饱和的程度；露点越接近气温，体感通常越潮湿。"
    DashboardDetail.Pressure -> "这里显示换算到海平面的气压；约 1013.25 hPa 通常视为标准海平面气压。"
    DashboardDetail.Astronomy -> "日月升落时间按当前城市位置与日期展示；月相描述月面受光部分的变化。"
    DashboardDetail.Sources -> "天气源状态列出本次融合中实际返回数据的来源及其主要观测值。"
    DashboardDetail.Fusion -> "多源融合会按地区、指标和来源特性分配权重，并标记明显的数据差异。"
}

@Composable
internal fun DetailDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 6.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.52f),
    )
}

@Composable
internal fun DetailValueRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(label, modifier = Modifier.weight(0.36f), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, modifier = Modifier.weight(0.64f), fontWeight = FontWeight.SemiBold)
    }
}

@Composable
internal fun AnimatedNumberText(
    value: Double,
    formatter: (Double) -> String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight? = null,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
) {
    val dataReady = LocalWeatherDataReady.current
    val animatedValue = remember { ComposeAnimatable(0f) }
    var hasStarted by remember { mutableStateOf(false) }
    LaunchedEffect(value, dataReady) {
        if (!dataReady) {
            animatedValue.snapTo(0f)
            hasStarted = false
            return@LaunchedEffect
        }
        val firstAnimation = !hasStarted
        if (firstAnimation) {
            animatedValue.snapTo(0f)
            hasStarted = true
        }
        animatedValue.animateTo(
            targetValue = value.toFloat(),
            animationSpec = tween(
                durationMillis = if (firstAnimation) 2_200 else 1_050,
                easing = CubicBezierEasing(0.12f, 0.78f, 0.16f, 1f),
            ),
        )
    }
    Text(
        modifier = modifier,
        text = formatter(animatedValue.value.toDouble()),
        style = style,
        fontWeight = fontWeight,
        color = color,
        maxLines = maxLines,
    )
}

internal fun aqiDescription(aqi: Int): String = when {
    aqi <= 50 -> "空气质量优，适合正常户外活动。"
    aqi <= 100 -> "空气质量良，敏感人群可酌情减少长时间剧烈活动。"
    aqi <= 150 -> "轻度污染，敏感人群建议减少户外活动。"
    aqi <= 200 -> "中度污染，建议减少长时间户外活动。"
    else -> "污染较重，尽量减少户外停留并做好防护。"
}

internal fun uvDescription(uv: Double): String = when {
    uv < 3 -> "紫外线较弱。"
    uv < 6 -> "紫外线中等，外出建议使用基础防晒。"
    uv < 8 -> "紫外线较强，注意遮阳和补涂防晒。"
    else -> "紫外线很强，尽量避开正午长时间暴露。"
}

@Composable
internal fun Modifier.floatingCardMotion(delayMillis: Int = 0): Modifier {
    if (LocalSuppressEntryMotion.current) return this
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(delayMillis) {
        visible = false
        if (delayMillis > 0) delay(delayMillis.toLong())
        visible = true
    }
    val progress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 920, easing = FastOutSlowInEasing),
        label = "floatingCard",
    )
    return graphicsLayer {
        alpha = progress
        translationY = (1f - progress) * 42f
        scaleX = 0.985f + progress * 0.015f
        scaleY = 0.985f + progress * 0.015f
    }
}

@Composable
internal fun WeatherAlertCard(alert: WeatherAlert, onClick: () -> Unit) {
    var expanded by remember(alert) { mutableStateOf(false) }
    val canExpand = alert.detail.length > 72
    val containerColor = when (alert.level) {
        AlertLevel.Severe -> Color(0xFFC81E1E)
        AlertLevel.Heat -> Color(0xFFC05621)
        AlertLevel.Rain -> Color(0xFF075985)
        AlertLevel.None -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (alert.level == AlertLevel.None) MaterialTheme.colorScheme.onSurface else Color.White
    val detailColor = if (alert.level == AlertLevel.None) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        Color.White.copy(alpha = 0.88f)
    }
    Card(
        modifier = Modifier
            .animateContentSize()
            .floatingCardMotion()
            .clip(RoundedCornerShape(26.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = contentColor,
            )
            Spacer(Modifier.width(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "提醒",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                )
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor.copy(alpha = 0.92f),
                )
                Text(
                    text = alert.detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = detailColor,
                    maxLines = if (expanded || !canExpand) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (canExpand) {
                    Row(
                        modifier = Modifier.clickable { expanded = !expanded },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(if (expanded) "收起详情" else "展开详情", style = MaterialTheme.typography.labelMedium, color = contentColor)
                        Icon(
                            if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun PreferenceSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        content()
    }
}

@Composable
internal fun SelectableChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        },
    )
}

internal enum class SettingsPressAction {
    OpenSettings,
    OpenWeatherTest,
    Ignore,
}

internal fun settingsPressAction(durationMillis: Long): SettingsPressAction = when {
    durationMillis <= 350L -> SettingsPressAction.OpenSettings
    durationMillis >= 5_000L -> SettingsPressAction.OpenWeatherTest
    else -> SettingsPressAction.Ignore
}
