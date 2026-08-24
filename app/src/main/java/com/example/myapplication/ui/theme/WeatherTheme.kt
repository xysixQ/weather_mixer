package com.weathermixer.sixq

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import android.app.Activity
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private val LightColors = lightColorScheme(
    primary = Color(0xFF1769E0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9E7FF),
    onPrimaryContainer = Color(0xFF082C65),
    secondary = Color(0xFF0F7B6C),
    secondaryContainer = Color(0xFFC7F0E8),
    tertiary = Color(0xFFB45F06),
    tertiaryContainer = Color(0xFFFFDEC2),
    background = Color(0xFFF7F9FC),
    onBackground = Color(0xFF1F2937),
    surface = Color.White,
    onSurface = Color(0xFF1F2937),
    surfaceVariant = Color(0xFFE5ECF3),
    onSurfaceVariant = Color(0xFF536170),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9FC9FF),
    onPrimary = Color(0xFF00325C),
    primaryContainer = Color(0xFF0B477F),
    onPrimaryContainer = Color(0xFFD5E9FF),
    secondary = Color(0xFF8AD8CB),
    secondaryContainer = Color(0xFF155F55),
    tertiary = Color(0xFFFFB77A),
    tertiaryContainer = Color(0xFF82490E),
    background = Color(0xFF080B10),
    onBackground = Color(0xFFE8EEF5),
    surface = Color(0xFF11161C),
    onSurface = Color(0xFFE8EEF5),
    surfaceVariant = Color(0xFF3F4752),
    onSurfaceVariant = Color(0xFFC0C8D2),
)

internal enum class ThemeMode(val label: String) {
    Light("关"),
    Dark("开"),
    System("跟随系统"),
}

internal fun ThemeMode.resolvesToDarkTheme(systemDarkTheme: Boolean): Boolean = when (this) {
    ThemeMode.Light -> false
    ThemeMode.Dark -> true
    ThemeMode.System -> systemDarkTheme
}

internal val LocalAppDarkTheme = staticCompositionLocalOf { false }

internal val BreezyUvShape = GenericShape { size, _ ->
    val center = Offset(size.width / 2f, size.height / 2f)
    val outerRadius = min(size.width, size.height) * 0.5f
    val innerRadius = outerRadius * 0.86f
    val points = List(24) { index ->
        val angle = -Math.PI / 2.0 + index * Math.PI * 2.0 / 24.0
        val radius = if (index % 2 == 0) outerRadius else innerRadius
        Offset(
            x = center.x + cos(angle).toFloat() * radius,
            y = center.y + sin(angle).toFloat() * radius,
        )
    }
    fun pointToward(from: Offset, to: Offset, fraction: Float) = Offset(
        x = from.x + (to.x - from.x) * fraction,
        y = from.y + (to.y - from.y) * fraction,
    )
    val incoming = points.indices.map { index ->
        pointToward(points[index], points[(index - 1 + points.size) % points.size], 0.24f)
    }
    val outgoing = points.indices.map { index ->
        pointToward(points[index], points[(index + 1) % points.size], 0.24f)
    }
    moveTo(outgoing.first().x, outgoing.first().y)
    for (step in 1..points.size) {
        val index = step % points.size
        lineTo(incoming[index].x, incoming[index].y)
        quadraticBezierTo(
            points[index].x,
            points[index].y,
            outgoing[index].x,
            outgoing[index].y,
        )
    }
    close()
}

@Composable
internal fun WeatherFusionTheme(
    themeMode: ThemeMode = ThemeMode.System,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val systemDarkTheme = isSystemInDarkTheme()
    val darkTheme = themeMode.resolvesToDarkTheme(systemDarkTheme)
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme -> dynamicDarkColorScheme(context)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window ?: return@SideEffect
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = !darkTheme
            isAppearanceLightNavigationBars = !darkTheme
        }
    }
    CompositionLocalProvider(LocalAppDarkTheme provides darkTheme) {
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}
