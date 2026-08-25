package com.weathermixer.sixq

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import android.net.Uri
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
import androidx.compose.material.icons.filled.Public
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ProfileSettingsScreen(
    modifier: Modifier = Modifier,
    profile: UserProfile?,
    onBack: () -> Unit,
    onSave: (UserProfile) -> Unit,
) {
    var occupation by remember(profile) { mutableStateOf(profile?.occupation) }
    var commute by remember(profile) { mutableStateOf(profile?.commuteMode) }
    var allergens by remember(profile) { mutableStateOf(profile?.allergens ?: emptySet()) }
    var vehicleRestrictionEnabled by remember(profile) {
        mutableStateOf(profile?.vehicleRestrictionEnabled ?: false)
    }
    var restrictionDetailsExpanded by remember { mutableStateOf(false) }
    val completion = listOf(occupation != null, commute != null).count { it } / 2f
    val animatedProgress by animateFloatAsState(
        targetValue = completion,
        animationSpec = tween(durationMillis = 450),
        label = "onboardingProgress",
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回设置")
                }
                Spacer(Modifier.width(6.dp))
                Text("用户画像", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            }
        }
        item {
            AnimatedWeatherHeader(
                title = "用户画像设置",
                subtitle = "职业、通勤和过敏原会影响穿衣、出行、防晒和空气质量提醒。",
            )
        }
        item {
            Card(
                modifier = Modifier.floatingCardMotion(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Text("画像信息", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    PreferenceSection(title = "职业") {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Occupation.entries.forEach { item ->
                                SelectableChip(
                                    label = item.label,
                                    icon = item.icon,
                                    selected = occupation == item,
                                    onClick = { occupation = item },
                                )
                            }
                        }
                    }
                    PreferenceSection(title = "通勤方式") {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            CommuteMode.entries.forEach { item ->
                                SelectableChip(
                                    label = item.label,
                                    icon = item.icon,
                                    selected = commute == item,
                                    onClick = { commute = item },
                                )
                            }
                        }
                    }
                    AnimatedVisibility(visible = commute == CommuteMode.Car) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("显示机动车限行信息", fontWeight = FontWeight.SemiBold)
                                    Text(
                                        "查到当天限行时，会置顶显示在个性化建议中。",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                Switch(
                                    checked = vehicleRestrictionEnabled,
                                    onCheckedChange = { vehicleRestrictionEnabled = it },
                                )
                            }
                            TextButton(onClick = { restrictionDetailsExpanded = !restrictionDetailsExpanded }) {
                                Text(if (restrictionDetailsExpanded) "收起详细信息" else "详细信息")
                                Icon(
                                    if (restrictionDetailsExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                )
                            }
                            AnimatedVisibility(visible = restrictionDetailsExpanded) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    LocalVehicleRestrictions.allDetails().forEach { detail ->
                                        Text(
                                            text = detail,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                        HorizontalDivider()
                                    }
                                }
                            }
                        }
                    }
                    PreferenceSection(title = "常见过敏原") {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Allergen.entries.forEach { item ->
                                SelectableChip(
                                    label = item.label,
                                    icon = item.icon,
                                    selected = item in allergens,
                                    onClick = {
                                        allergens = if (item in allergens) allergens - item else allergens + item
                                    },
                                )
                            }
                        }
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = occupation != null && commute != null,
                        onClick = {
                            onSave(
                                UserProfile(
                                    occupation = requireNotNull(occupation),
                                    commuteMode = requireNotNull(commute),
                                    allergens = allergens,
                                    vehicleRestrictionEnabled = vehicleRestrictionEnabled,
                                )
                            )
                        },
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("保存并返回设置")
                    }
                }
            }
        }
    }
}

@Composable
internal fun SettingsHomeScreen(
    modifier: Modifier = Modifier,
    themeMode: ThemeMode,
    reverseThemeSwipe: Boolean,
    temperatureUnit: TemperatureUnit,
    reverseTemperatureSwipe: Boolean,
    hapticFeedbackEnabled: Boolean,
    onBack: () -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onReverseThemeSwipeChanged: (Boolean) -> Unit,
    onTemperatureUnitChanged: (TemperatureUnit) -> Unit,
    onReverseTemperatureSwipeChanged: (Boolean) -> Unit,
    onHapticFeedbackChanged: (Boolean) -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSources: () -> Unit,
    onOpenBlockOrder: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenLanguage: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回天气")
                }
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "设置",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        item {
            ThemeModeSwipeCard(
                themeMode = themeMode,
                reverseSwipeDirection = reverseThemeSwipe,
                onThemeModeChanged = onThemeModeChanged,
                onReverseSwipeDirectionChanged = onReverseThemeSwipeChanged,
            )
        }
        item {
            TemperatureUnitSwipeCard(
                temperatureUnit = temperatureUnit,
                reverseSwipeDirection = reverseTemperatureSwipe,
                onTemperatureUnitChanged = onTemperatureUnitChanged,
                onReverseSwipeDirectionChanged = onReverseTemperatureSwipeChanged,
            )
        }
        item {
            SettingsToggleItem(
                title = "震动反馈",
                subtitle = "主要操作、页面切换和单位滑动",
                icon = Icons.Filled.Vibration,
                checked = hapticFeedbackEnabled,
                onCheckedChange = onHapticFeedbackChanged,
            )
        }
        item {
            SettingsMenuItem(
                title = "用户画像",
                subtitle = "职业、通勤方式和过敏原",
                icon = Icons.Filled.Person,
                onClick = onOpenProfile,
            )
        }
        item {
            SettingsMenuItem(
                title = "主页模块顺序",
                subtitle = "调整天气块显示位置",
                icon = Icons.Filled.Today,
                onClick = onOpenBlockOrder,
            )
        }
        item {
            SettingsMenuItem(
                title = "天气源配置",
                subtitle = "Endpoint、API Host 和 Key",
                icon = Icons.Filled.Cloud,
                onClick = onOpenSources,
            )
        }
        item {
            SettingsMenuItem(
                title = "语言",
                subtitle = "自定义界面文字并导入/导出",
                icon = Icons.Filled.Public,
                onClick = onOpenLanguage,
            )
        }
        item {
            SettingsMenuItem(
                title = "关于应用",
                subtitle = "版本、作者和 SDK 信息",
                icon = Icons.Filled.Info,
                onClick = onOpenAbout,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ThemeModeSwipeCard(
    themeMode: ThemeMode,
    reverseSwipeDirection: Boolean,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onReverseSwipeDirectionChanged: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val density = LocalDensity.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    val scope = rememberCoroutineScope()
    val cardShape = RoundedCornerShape(28.dp)
    val maximumDrag = with(density) { 84.dp.toPx() }
    val switchThreshold = with(density) { 22.dp.toPx() }
    val visualResistance = with(density) { 14.dp.toPx() }
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current
    val pressed by interactionSource.collectIsPressedAsState()
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var motionJob by remember { mutableStateOf<Job?>(null) }
    var lastTickIndex by remember { mutableIntStateOf(0) }
    var showDirectionSettings by remember { mutableStateOf(false) }
    val visualDragOffset = dragOffset * visualResistance / (visualResistance + abs(dragOffset))
    val modes = ThemeMode.entries
    val pressedElevation by animateDpAsState(
        targetValue = if (pressed || abs(dragOffset) > 0.5f) 8.dp else 0.dp,
        animationSpec = tween(120),
        label = "themeModePressedElevation",
    )

    fun changeMode(step: Int, emitHaptic: Boolean = true) {
        val nextIndex = (modes.indexOf(themeMode) + step + modes.size) % modes.size
        onThemeModeChanged(modes[nextIndex])
        if (emitHaptic && hapticFeedbackEnabled) context.performScaleTick(view, 0.56f)
    }

    fun release(velocity: Float) {
        motionJob?.cancel()
        motionJob = scope.launch {
            val animation = ComposeAnimatable(dragOffset)
            animation.animateTo(
                targetValue = 0f,
                animationSpec = spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessMediumLow),
                initialVelocity = velocity,
            ) { dragOffset = value }
            dragOffset = 0f
        }
    }

    val draggableState = rememberDraggableState { delta ->
        dragOffset = (dragOffset + delta).coerceIn(-maximumDrag, maximumDrag)
        val normalizedDistance = (abs(dragOffset) / maximumDrag).coerceIn(0f, 1f)
        val nonlinearTicks = ln(1f + normalizedDistance * 70f) / ln(71f) * 15f
        val tickIndex = nonlinearTicks.toInt() * if (dragOffset < 0f) -1 else 1
        if (hapticFeedbackEnabled && tickIndex != lastTickIndex) {
            if (normalizedDistance < 0.18f) context.performSoftResistancePulse()
            else context.performScaleTick(view, 0.24f + normalizedDistance * 0.26f)
            lastTickIndex = tickIndex
        }
    }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .clip(cardShape)
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = indication,
                    onClick = {},
                    onLongClick = {
                        if (hapticFeedbackEnabled) context.performAppVibration(AppVibration.StrongImpact)
                        showDirectionSettings = true
                    },
                )
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStarted = {
                        motionJob?.cancel()
                        lastTickIndex = 0
                    },
                    onDragStopped = { velocity ->
                        val projected = dragOffset + velocity * 0.065f
                        if (abs(projected) >= switchThreshold) {
                            val physicalStep = if (projected < 0f) -1 else 1
                            changeMode(if (reverseSwipeDirection) -physicalStep else physicalStep, emitHaptic = false)
                        }
                        release(velocity)
                    },
                ),
            shape = cardShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = pressedElevation),
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = {
                    changeMode(-1)
                    dragOffset = with(density) { (-10).dp.toPx() }
                    release(with(density) { (-260).dp.toPx() })
                }) { Icon(Icons.Filled.ChevronLeft, contentDescription = "上一个主题模式") }
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier.graphicsLayer { translationX = visualDragOffset },
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("主题模式", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    modes.forEach { mode ->
                        val icon = when (mode) {
                            ThemeMode.Light -> Icons.Filled.WbSunny
                            ThemeMode.Dark -> Icons.Filled.NightsStay
                            ThemeMode.System -> Icons.Filled.Public
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = mode.label,
                            modifier = Modifier.size(22.dp),
                            tint = if (mode == themeMode) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.58f),
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    changeMode(1)
                    dragOffset = with(density) { 10.dp.toPx() }
                    release(with(density) { 260.dp.toPx() })
                }) { Icon(Icons.Filled.ChevronRight, contentDescription = "下一个主题模式") }
            }
        }
        if (showDirectionSettings) {
            Dialog(onDismissRequest = { showDirectionSettings = false }) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    shadowElevation = 20.dp,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("主题模式滑动", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("反向滑动", fontWeight = FontWeight.SemiBold)
                                Text(
                                    "交换主题模式控件左右滑动对应的切换方向",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Switch(checked = reverseSwipeDirection, onCheckedChange = onReverseSwipeDirectionChanged)
                        }
                        TextButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = { showDirectionSettings = false },
                        ) { Text("完成") }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TemperatureUnitSwipeCard(
    temperatureUnit: TemperatureUnit,
    reverseSwipeDirection: Boolean,
    onTemperatureUnitChanged: (TemperatureUnit) -> Unit,
    onReverseSwipeDirectionChanged: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val density = LocalDensity.current
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    val scope = rememberCoroutineScope()
    val maximumDrag = with(density) { 84.dp.toPx() }
    val switchThreshold = with(density) { 22.dp.toPx() }
    val visualResistance = with(density) { 14.dp.toPx() }
    val cardShape = RoundedCornerShape(28.dp)
    val unitInteractionSource = remember { MutableInteractionSource() }
    val unitIndication = LocalIndication.current
    val unitPressed by unitInteractionSource.collectIsPressedAsState()
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val pressedElevation by animateDpAsState(
        targetValue = if (unitPressed || abs(dragOffset) > 0.5f) 8.dp else 0.dp,
        animationSpec = tween(120),
        label = "temperatureUnitPressedElevation",
    )
    val visualDragOffset = dragOffset * visualResistance / (visualResistance + abs(dragOffset))
    var motionJob by remember { mutableStateOf<Job?>(null) }
    var lastTickIndex by remember { mutableStateOf(0) }
    var lastMotionNanos by remember { mutableLongStateOf(0L) }
    var showDirectionSettings by remember { mutableStateOf(false) }
    val draggableState = rememberDraggableState { delta ->
        dragOffset = (dragOffset + delta).coerceIn(-maximumDrag, maximumDrag)
        val normalizedDistance = (abs(dragOffset) / maximumDrag).coerceIn(0f, 1f)
        val nonlinearTicks = ln(1f + normalizedDistance * 70f) / ln(71f) * 15f
        val tickIndex = nonlinearTicks.toInt() * if (dragOffset < 0f) -1 else 1
        if (hapticFeedbackEnabled && tickIndex != lastTickIndex) {
            val now = System.nanoTime()
            val elapsedSeconds = if (lastMotionNanos > 0L) {
                ((now - lastMotionNanos) / 1_000_000_000f).coerceAtLeast(0.001f)
            } else {
                0.016f
            }
            val velocityFactor = (abs(delta) / elapsedSeconds / 3_600f).coerceIn(0f, 1f)
            val resistanceFactor = normalizedDistance
            if (normalizedDistance < 0.18f) {
                context.performSoftResistancePulse()
            } else {
                context.performScaleTick(
                    view = view,
                    strength = 0.2f + velocityFactor * 0.12f + resistanceFactor * 0.3f,
                )
            }
            lastTickIndex = tickIndex
            lastMotionNanos = now
        }
    }

    fun changeUnit(step: Int, emitHaptic: Boolean) {
        val units = TemperatureUnit.entries
        val nextIndex = (units.indexOf(temperatureUnit) + step + units.size) % units.size
        onTemperatureUnitChanged(units[nextIndex])
        if (emitHaptic && hapticFeedbackEnabled) {
            context.performScaleTick(view, 0.56f)
        }
    }

    fun releaseWithVelocity(initialVelocity: Float) {
        motionJob?.cancel()
        val releaseDistance = dragOffset
        val energy = (
            abs(releaseDistance) / maximumDrag + abs(initialVelocity) / 8_000f
            ).coerceIn(0f, 1.1f)
        motionJob = scope.launch {
            val animation = ComposeAnimatable(releaseDistance)
            animation.animateTo(
                targetValue = 0f,
                animationSpec = spring(
                    dampingRatio = (0.68f - energy * 0.09f).coerceAtLeast(0.52f),
                    stiffness = Spring.StiffnessMediumLow + energy * 96f,
                ),
                initialVelocity = initialVelocity,
            ) { dragOffset = value }
            dragOffset = 0f
        }
    }

    fun changeUnitFromButton(step: Int) {
        changeUnit(step, emitHaptic = true)
        motionJob?.cancel()
        dragOffset = with(density) { 11.dp.toPx() } * if (step < 0) -1f else 1f
        releaseWithVelocity(with(density) { 300.dp.toPx() } * if (step < 0) -1f else 1f)
    }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .clip(cardShape)
                .combinedClickable(
                    interactionSource = unitInteractionSource,
                    indication = unitIndication,
                    onClick = {},
                    onLongClick = {
                        if (hapticFeedbackEnabled) context.performAppVibration(AppVibration.StrongImpact)
                        showDirectionSettings = true
                    },
                )
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStarted = {
                        motionJob?.cancel()
                        lastTickIndex = 0
                        lastMotionNanos = System.nanoTime()
                    },
                    onDragStopped = { velocity ->
                        val projectedDistance = dragOffset + velocity * 0.065f
                        if (abs(projectedDistance) >= switchThreshold) {
                            val physicalStep = if (projectedDistance < 0f) -1 else 1
                            val logicalStep = if (reverseSwipeDirection) -physicalStep else physicalStep
                            changeUnit(logicalStep, emitHaptic = false)
                        }
                        releaseWithVelocity(velocity)
                    },
                ),
            shape = cardShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = pressedElevation),
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { changeUnitFromButton(-1) }) {
                    Icon(
                        Icons.Filled.ChevronLeft,
                        contentDescription = "上一个温度单位",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.68f),
                    )
                }
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier.graphicsLayer { translationX = visualDragOffset },
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("温度单位", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    TemperatureUnit.entries.forEach { unit ->
                        Text(
                            unit.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (unit == temperatureUnit) FontWeight.Bold else FontWeight.Medium,
                            color = if (unit == temperatureUnit) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.58f),
                            maxLines = 1,
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { changeUnitFromButton(1) }) {
                    Icon(
                        Icons.Filled.ChevronRight,
                        contentDescription = "下一个温度单位",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.68f),
                    )
                }
            }
        }
        if (showDirectionSettings) {
            Dialog(onDismissRequest = { showDirectionSettings = false }) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    shadowElevation = 20.dp,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text("温度单位滑动", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("反向滑动", fontWeight = FontWeight.SemiBold)
                                Text(
                                    "交换温度单位控件左右滑动对应的切换方向",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Switch(
                                checked = reverseSwipeDirection,
                                onCheckedChange = onReverseSwipeDirectionChanged,
                            )
                        }
                        TextButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = { showDirectionSettings = false },
                        ) { Text("完成") }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SettingsSubpageHeader(title: String, onBack: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回设置")
        }
        Spacer(Modifier.width(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
internal fun SourceSettingsScreen(
    modifier: Modifier = Modifier,
    configs: List<WeatherApiConfig>,
    onBack: () -> Unit,
    onSave: (List<WeatherApiConfig>) -> Unit,
    onReset: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            SettingsSubpageHeader(title = "天气源配置", onBack = onBack)
        }
        item { ApiSettingsCard(configs = configs, onSave = onSave, onReset = onReset) }
        item { DataPlanCard() }
    }
}

@Composable
internal fun LanguageSettingsScreen(
    modifier: Modifier = Modifier,
    activeOverrides: Map<String, String>,
    onBack: () -> Unit,
    onSave: (Map<String, String>) -> Unit,
    onImportSaved: (Map<String, String>) -> Unit,
    onMessage: (String) -> Unit,
) {
    val context = LocalContext.current
    var draftOverrides by remember(activeOverrides) { mutableStateOf(activeOverrides) }
    var query by remember { mutableStateOf("") }
    var newSource by remember { mutableStateOf("") }
    var statusText by remember { mutableStateOf<String?>(null) }
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            LanguageOverrideStore.exportToUri(context, uri, draftOverrides)
        }.onSuccess {
            statusText = "语言文件已导出。"
            onMessage("语言文件已导出。")
        }.onFailure {
            statusText = "导出失败：${it.message.orEmpty().ifBlank { "无法写入文件" }}"
        }
    }
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        LanguageOverrideStore.importFromUri(context, uri)
            .onSuccess { imported ->
                onImportSaved(imported)
                statusText = "已导入语言文件，请重启 App 后生效。"
                onMessage("已导入语言文件，请重启 App 后生效。")
            }
            .onFailure {
                statusText = "导入失败：${it.message.orEmpty().ifBlank { "文件格式无法识别" }}"
            }
    }
    val allStrings = remember(draftOverrides) {
        (UiStringCatalog.defaultStrings() + draftOverrides.keys)
            .distinct()
            .sortedBy { it.lowercase() }
    }
    val visibleStrings = remember(allStrings, query) {
        val keyword = query.trim()
        if (keyword.isBlank()) {
            allStrings
        } else {
            allStrings.filter { source ->
                source.contains(keyword, ignoreCase = true) ||
                    draftOverrides[source].orEmpty().contains(keyword, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { SettingsSubpageHeader(title = "语言", onBack = onBack) }
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("自定义界面文字", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "左侧原文作为匹配键；右侧填写替换文字。导入别人写的语言文件后，请重启 App 再查看效果。",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    statusText?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                importLauncher.launch(arrayOf("application/json"))
                            },
                        ) {
                            Icon(Icons.Filled.Public, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("导入")
                        }
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { exportLauncher.launch("weather-mixer-language.json") },
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("导出")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val cleaned = LanguageOverrideStore.clean(draftOverrides)
                                draftOverrides = cleaned
                                onSave(cleaned)
                                statusText = "语言文字已保存。"
                            },
                        ) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("保存")
                        }
                        TextButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                draftOverrides = emptyMap()
                                onSave(emptyMap())
                                statusText = "已清空语言替换。"
                            },
                        ) {
                            Text("清空替换")
                        }
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("搜索字符串") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "清空搜索")
                        }
                    }
                },
                singleLine = true,
            )
        }
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text("新增自定义项", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = newSource,
                        onValueChange = { newSource = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("需要替换的原文") },
                        minLines = 1,
                    )
                    TextButton(
                        enabled = newSource.isNotBlank(),
                        onClick = {
                            val source = newSource.trim()
                            draftOverrides = draftOverrides + (source to (draftOverrides[source] ?: source))
                            query = source
                            newSource = ""
                        },
                    ) {
                        Text("加入列表")
                    }
                }
            }
        }
        items(visibleStrings, key = { it }) { source ->
            val replacement = draftOverrides[source] ?: source
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(source, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    OutlinedTextField(
                        value = replacement,
                        onValueChange = { value -> draftOverrides = draftOverrides + (source to value) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("替换为") },
                        minLines = 1,
                        maxLines = 3,
                    )
                    TextButton(
                        enabled = source in draftOverrides,
                        onClick = { draftOverrides = draftOverrides - source },
                    ) {
                        Text("恢复原文")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun BlockOrderScreen(
    modifier: Modifier = Modifier,
    order: List<DashboardBlock>,
    onBack: () -> Unit,
    onOrderChanged: (List<DashboardBlock>) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val latestOrder by rememberUpdatedState(order)
    val latestOnOrderChanged by rememberUpdatedState(onOrderChanged)
    val hapticFeedbackEnabled = LocalHapticFeedbackEnabled.current
    var draftOrder by remember { mutableStateOf(order) }
    val latestDraftOrder by rememberUpdatedState(draftOrder)
    var draggedBlock by remember { mutableStateOf<DashboardBlock?>(null) }
    var dragDistance by remember { mutableFloatStateOf(0f) }
    var draggedStartOffset by remember { mutableStateOf(0) }
    var draggedSlotOffset by remember { mutableFloatStateOf(0f) }
    var draggedItemSize by remember { mutableStateOf(1) }
    var grabOffsetWithinItem by remember { mutableFloatStateOf(0f) }
    var settingsPointerY by remember { mutableFloatStateOf(0f) }
    var settingsAutoScrollSpeed by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(order, draggedBlock) {
        if (draggedBlock == null) draftOrder = order
    }

    fun moveAndCommit(from: Int, to: Int) {
        if (hapticFeedbackEnabled) {
            context.performAppVibration(AppVibration.ReorderBuzz)
        }
        latestOnOrderChanged(moveDashboardBlock(latestOrder, from, to))
    }

    fun updateSettingsTarget(block: DashboardBlock, direction: Float) {
        if (abs(direction) < 0.01f) return
        val currentIndex = latestDraftOrder.indexOf(block)
        if (currentIndex < 0) return
        val targetIndex = currentIndex + if (direction > 0f) 1 else -1
        if (targetIndex !in latestDraftOrder.indices) return
        val targetBlock = latestDraftOrder[targetIndex]
        val targetInfo = listState.layoutInfo.visibleItemsInfo
            .firstOrNull { it.key == targetBlock.name } ?: return
        val draggedCenter = settingsPointerY - grabOffsetWithinItem + draggedItemSize / 2f
        val targetCenter = targetInfo.offset + targetInfo.size / 2f
        val crossed = if (direction > 0f) draggedCenter > targetCenter else draggedCenter < targetCenter
        if (!crossed) return
        if (hapticFeedbackEnabled) {
            context.performAppVibration(AppVibration.ReorderBuzz)
        }
        draggedSlotOffset = if (targetIndex > currentIndex) {
            (targetInfo.offset + targetInfo.size - draggedItemSize).toFloat()
        } else {
            targetInfo.offset.toFloat()
        }
        draftOrder = moveDashboardBlock(latestDraftOrder, currentIndex, targetIndex)
    }

    LaunchedEffect(draggedBlock, settingsAutoScrollSpeed) {
        val block = draggedBlock
        while (block != null && abs(settingsAutoScrollSpeed) > 0.5f) {
            val consumed = listState.scrollBy(settingsAutoScrollSpeed)
            if (abs(consumed) < 0.1f) {
                settingsAutoScrollSpeed = 0f
                break
            }
            draggedSlotOffset -= consumed
            updateSettingsTarget(block, settingsAutoScrollSpeed)
            delay(16L)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            SettingsSubpageHeader(title = "主页模块顺序", onBack = onBack)
        }
        items(draftOrder, key = { it.name }) { block ->
            val index = draftOrder.indexOf(block)
            val isDragging = draggedBlock == block
            val fingerLockedTranslation = draggedStartOffset + dragDistance - draggedSlotOffset
            val elevation by animateDpAsState(
                targetValue = if (isDragging) 12.dp else 0.dp,
                animationSpec = tween(120),
                label = "blockDragElevation",
            )
            val scale by animateFloatAsState(
                targetValue = if (isDragging) 1.025f else 1f,
                animationSpec = tween(120),
                label = "blockDragScale",
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isDragging || draggedBlock == null) {
                            Modifier
                        } else {
                            Modifier.animateItemPlacement(
                                animationSpec = tween(
                                    durationMillis = 180,
                                    easing = FastOutSlowInEasing,
                                )
                            )
                        }
                    )
                    .zIndex(if (isDragging) 3f else 0f)
                    .graphicsLayer {
                        translationY = if (isDragging) fingerLockedTranslation else 0f
                        scaleX = scale
                        scaleY = scale
                    }
                    .pointerInput(block) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { touchOffset ->
                                if (hapticFeedbackEnabled) {
                                    context.performAppVibration(AppVibration.StrongImpact)
                                }
                                val itemInfo = listState.layoutInfo.visibleItemsInfo
                                    .firstOrNull { it.key == block.name }
                                draggedBlock = block
                                draggedStartOffset = itemInfo?.offset ?: 0
                                draggedSlotOffset = (itemInfo?.offset ?: 0).toFloat()
                                draggedItemSize = itemInfo?.size ?: 1
                                grabOffsetWithinItem = touchOffset.y
                                dragDistance = 0f
                                settingsPointerY = draggedStartOffset + touchOffset.y
                                settingsAutoScrollSpeed = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragDistance += dragAmount.y
                                settingsPointerY += dragAmount.y
                                val viewportHeight = listState.layoutInfo.viewportEndOffset.toFloat()
                                val edgeRange = with(density) { 120.dp.toPx() }
                                settingsAutoScrollSpeed = when {
                                    settingsPointerY < edgeRange -> {
                                        val penetration = ((edgeRange - settingsPointerY) / edgeRange)
                                            .coerceIn(0f, 1f)
                                        -(penetration * (7f + 5f * penetration))
                                    }
                                    settingsPointerY > viewportHeight - edgeRange -> {
                                        val penetration = (
                                            (settingsPointerY - (viewportHeight - edgeRange)) / edgeRange
                                            ).coerceIn(0f, 1f)
                                        penetration * (7f + 5f * penetration)
                                    }
                                    else -> 0f
                                }
                                updateSettingsTarget(block, dragAmount.y)
                            },
                            onDragEnd = {
                                latestOnOrderChanged(latestDraftOrder)
                                draggedBlock = null
                                settingsAutoScrollSpeed = 0f
                                dragDistance = 0f
                                draggedSlotOffset = 0f
                            },
                            onDragCancel = {
                                draftOrder = latestOrder
                                draggedBlock = null
                                settingsAutoScrollSpeed = 0f
                                dragDistance = 0f
                                draggedSlotOffset = 0f
                            },
                        )
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Filled.DragHandle,
                        contentDescription = "长按拖动${block.label}",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("${index + 1}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                    Text(block.label, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    IconButton(enabled = index > 0, onClick = { moveAndCommit(index, index - 1) }) {
                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "上移${block.label}")
                    }
                    IconButton(enabled = index < draftOrder.lastIndex, onClick = { moveAndCommit(index, index + 1) }) {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "下移${block.label}")
                    }
                }
            }
        }
        item {
            TextButton(onClick = { onOrderChanged(DashboardOrderStore.DefaultOrder) }) {
                Text("恢复默认顺序")
            }
        }
    }
}

@Composable
internal fun SettingsMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}

@Composable
internal fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        }
    }
}

@Composable
internal fun AboutScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var updateChecking by remember { mutableStateOf(false) }
    var updateMessage by remember { mutableStateOf<String?>(null) }
    var updateReleaseUrl by remember { mutableStateOf<String?>(null) }
    val sdkItems = remember {
        listOf(
            SdkInfo(
                name = "Android Jetpack Compose / Material 3",
                usage = "界面渲染、动画、设置页与首页交互",
                info = "本地 UI 框架，随应用打包运行。",
            ),
            SdkInfo(
                name = "OkHttp",
                usage = "天气源、定位与外部服务网络请求",
                info = "统一负责 HTTP 请求、超时和响应读取。",
            ),
            SdkInfo(
                name = "彩云天气服务",
                usage = "国内城市识别、实况天气和预报数据",
                info = "国内地区优先展示，后续再与其他源融合。",
            ),
            SdkInfo(
                name = "百度 IP 定位服务",
                usage = "首次启动时辅助识别当前大致区域",
                info = "仅用于初始定位兜底，不替代系统精确定位权限。",
            ),
            SdkInfo(
                name = "MSN Weather / NWS / Visual Crossing / 和风天气",
                usage = "多天气源融合与国外地区预报补充",
                info = "按地区、可用字段和返回质量动态参与融合。",
            ),
            SdkInfo(
                name = "Meteostat via RapidAPI",
                usage = "历史与逐小时气象数据补充",
                info = "需要用户提供已订阅的 RapidAPI Key。",
            ),
            SdkInfo(
                name = "坤舆航图 SDK",
                usage = "小工具页航图与气象图入口",
                info = "通过 WebView 加载官方 JS SDK 与内置航图图层。",
            ),
            SdkInfo(
                name = "Variflight ADS-B 页面组件",
                usage = "小工具页实时航迹入口",
                info = "通过桌面 UA 的交互式 WebView 打开官方航迹页面。",
            ),
        )
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            SettingsSubpageHeader(title = "关于应用", onBack = onBack)
        }
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(Icons.Filled.WbSunny, contentDescription = null, modifier = Modifier.size(42.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("M-Weather", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("版本 ${BuildConfig.VERSION_NAME}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("作者 xysixQ", style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider()
                    Text("预报仅供生活参考。遇到灾害性天气，请以当地气象部门发布的信息为准。", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            SettingsMenuItem(
                title = "GitHub 仓库",
                subtitle = "github.com/xysixQ/weather_mixer",
                icon = Icons.Filled.Public,
                onClick = { context.openExternalUrl(WeatherMixerRepositoryUrl) },
            )
        }
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("检查更新", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("当前版本 ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            enabled = !updateChecking,
                            onClick = {
                                updateChecking = true
                                updateMessage = "正在检查 GitHub Releases..."
                                updateReleaseUrl = null
                                scope.launch {
                                    val result = runCatching { checkWeatherMixerReleaseUpdate(BuildConfig.VERSION_NAME) }
                                        .getOrElse { error -> ReleaseCheckResult("检查失败：${error.message ?: "网络请求异常"}") }
                                    updateMessage = result.message
                                    updateReleaseUrl = result.releaseUrl
                                    updateChecking = false
                                }
                            },
                        ) {
                            if (updateChecking) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Text("检查")
                            }
                        }
                    }
                    updateMessage?.let { message ->
                        Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    updateReleaseUrl?.let { url ->
                        TextButton(onClick = { context.openExternalUrl(url) }) {
                            Text("打开 Release")
                        }
                    }
                }
            }
        }
        item {
            SettingsMenuItem(
                title = "反馈渠道",
                subtitle = "通过 GitHub Issues 提交反馈",
                icon = Icons.Filled.Info,
                onClick = { context.openExternalUrl(WeatherMixerIssuesUrl) },
            )
        }
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
                ),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("SDK / 服务列表", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    sdkItems.forEachIndexed { index, item ->
                        SdkInfoRow(item)
                        if (index != sdkItems.lastIndex) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f))
                    }
                }
            }
        }
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.84f),
                ),
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("随 ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                            append("AviumUI")
                        }
                        append(" For Waffle 一同开发")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { context.openExternalUrl(AviumUiOfficialUrl) }
                        .padding(18.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private const val WeatherMixerRepositoryUrl = "https://github.com/xysixQ/weather_mixer"
private const val WeatherMixerIssuesUrl = "https://github.com/xysixQ/weather_mixer/issues"
private const val WeatherMixerReleasesApiUrl = "https://api.github.com/repos/xysixQ/weather_mixer/releases?per_page=10"
private const val AviumUiOfficialUrl = "https://aviumui.org"

private val GitHubReleaseHttpClient = OkHttpClient.Builder()
    .callTimeout(12, TimeUnit.SECONDS)
    .build()

private fun Context.openExternalUrl(url: String) {
    runCatching {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }
}

private data class ReleaseCheckResult(
    val message: String,
    val releaseUrl: String? = null,
)

private data class GitHubReleaseInfo(
    val tagName: String,
    val name: String,
    val htmlUrl: String,
    val publishedAt: String,
    val prerelease: Boolean,
)

private suspend fun checkWeatherMixerReleaseUpdate(currentVersion: String): ReleaseCheckResult = withContext(Dispatchers.IO) {
    val request = Request.Builder()
        .url(WeatherMixerReleasesApiUrl)
        .header("Accept", "application/vnd.github+json")
        .header("X-GitHub-Api-Version", "2022-11-28")
        .header("User-Agent", "M-Weather/${BuildConfig.VERSION_NAME}")
        .build()
    GitHubReleaseHttpClient.newCall(request).execute().use { response ->
        val body = response.body.string()
        when (response.code) {
            404 -> return@withContext ReleaseCheckResult("暂时无法读取 Release。私有仓库需要 GitHub Token，或将 Release 公开后再检查。")
            403 -> return@withContext ReleaseCheckResult("GitHub API 暂时拒绝请求，可能是频率限制。稍后再试。")
        }
        if (!response.isSuccessful) {
            return@withContext ReleaseCheckResult("检查失败：GitHub 返回 ${response.code} ${response.message}")
        }
        val releases = JSONArray(body)
        val parsed = (0 until releases.length()).mapNotNull { index ->
            val item = releases.optJSONObject(index) ?: return@mapNotNull null
            if (item.optBoolean("draft", false)) return@mapNotNull null
            GitHubReleaseInfo(
                tagName = item.optString("tag_name"),
                name = item.optString("name"),
                htmlUrl = item.optString("html_url"),
                publishedAt = item.optString("published_at"),
                prerelease = item.optBoolean("prerelease", false),
            )
        }
        val latest = parsed.firstOrNull { !it.prerelease } ?: parsed.firstOrNull()
            ?: return@withContext ReleaseCheckResult("仓库暂时没有可用 Release。")
        val latestLabel = latest.name.ifBlank { latest.tagName }
        val publishedDate = latest.publishedAt.take(10).takeIf { it.isNotBlank() }?.let { " · $it" }.orEmpty()
        val compare = compareVersionTags(latest.tagName, currentVersion)
        val message = when {
            compare > 0 -> "发现新版本：$latestLabel$publishedDate"
            compare == 0 -> "当前已是最新版本：$latestLabel$publishedDate"
            else -> "当前版本高于最新 Release：$latestLabel$publishedDate"
        }
        ReleaseCheckResult(message, latest.htmlUrl.takeIf { it.isNotBlank() })
    }
}

private fun compareVersionTags(left: String, right: String): Int {
    val leftParts = versionNumberParts(left)
    val rightParts = versionNumberParts(right)
    if (leftParts.isEmpty() || rightParts.isEmpty()) return left.compareTo(right, ignoreCase = true)
    val size = max(leftParts.size, rightParts.size)
    for (index in 0 until size) {
        val l = leftParts.getOrElse(index) { 0 }
        val r = rightParts.getOrElse(index) { 0 }
        if (l != r) return l.compareTo(r)
    }
    return 0
}

private fun versionNumberParts(value: String): List<Int> = Regex("\\d+")
    .findAll(value)
    .mapNotNull { it.value.toIntOrNull() }
    .toList()

private data class SdkInfo(
    val name: String,
    val usage: String,
    val info: String,
)

@Composable
private fun SdkInfoRow(item: SdkInfo) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(item.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        Text(item.usage, style = MaterialTheme.typography.bodySmall)
        Text(item.info, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
