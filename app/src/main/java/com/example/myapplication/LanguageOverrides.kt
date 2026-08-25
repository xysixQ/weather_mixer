package com.weathermixer.sixq

import android.content.Context
import android.net.Uri
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text as MaterialText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.core.content.edit
import org.json.JSONObject

internal val LocalUiStringOverrides = staticCompositionLocalOf<Map<String, String>> { emptyMap() }

internal fun localizeUiString(text: String, overrides: Map<String, String>): String =
    overrides[text]?.takeIf { it != text } ?: text

@Composable
internal fun localizedUiString(text: String): String = localizeUiString(text, LocalUiStringOverrides.current)

@Composable
internal fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    MaterialText(
        text = localizedUiString(text),
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout,
        style = style,
    )
}

@Composable
internal fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, androidx.compose.foundation.text.InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    val replacement = LocalUiStringOverrides.current[text.text]?.takeIf { it != text.text }
    if (replacement != null) {
        MaterialText(
            text = replacement,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style,
        )
    } else {
        MaterialText(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            inlineContent = inlineContent,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
}

internal object UiStringCatalog {
    fun defaultStrings(): List<String> = buildList {
        addAll(
            listOf(
                "设置",
                "小工具",
                "语言",
                "用户画像",
                "主页模块顺序",
                "天气源配置",
                "关于应用",
                "震动反馈",
                "主题模式",
                "温度单位",
                "反向滑动",
                "完成",
                "保存配置",
                "恢复默认",
                "恢复默认顺序",
                "保存并返回设置",
                "天气融合助手",
                "每日预报",
                "逐小时预报",
                "日月升落",
                "天气源状态",
                "多源融合分析",
                "天气提醒",
                "个性化建议",
                "降雨",
                "风况",
                "空气质量",
                "紫外线",
                "湿度",
                "气压",
                "露点",
                "信息来源",
                "离线模式 · 显示缓存数据",
                "定位当前",
                "收起",
                "展开详情",
                "收起详情",
                "测试页（仅供 Debug 使用）",
                "测试数据覆盖",
                "手动选择天气",
                "预警场景",
                "天气代码",
                "时间（HH:mm）",
                "时区",
                "请输入 00:00 到 23:59",
                "例如 Asia/Shanghai、America/Los_Angeles 或 UTC",
                "职业",
                "通勤方式",
                "常见过敏原",
                "显示机动车限行信息",
                "详细信息",
                "收起详细信息",
                "接口预留",
                "天气源接入状态",
                "Endpoint",
                "API Host",
                "API Key / Token",
                "API Key / Token（可选）",
                "留空时使用默认 Key",
                "留空时使用默认 Host",
                "留空时使用默认 Endpoint；填写后使用自定义地址",
                "Endpoint、API Host 和 Key",
                "职业、通勤方式和过敏原",
                "调整天气块显示位置",
                "版本",
                "开",
                "关",
                "跟随系统",
            )
        )
        addAll(Occupation.entries.map { it.label })
        addAll(CommuteMode.entries.map { it.label })
        addAll(Allergen.entries.map { it.label })
        addAll(LocationMethod.entries.map { it.label })
        addAll(ThemeMode.entries.map { it.label })
        addAll(TemperatureUnit.entries.map { it.symbol })
        addAll(DashboardBlock.entries.map { it.label })
        addAll(DashboardDetail.entries.map { it.label })
        addAll(WeatherCondition.entries.map { it.label })
        addAll(WeatherTestAlertPreset.entries.flatMap { preset ->
            val alert = preset.alert
            listOf(preset.label, alert.title, alert.detail)
        })
        addAll(ApiConfigDefaults.defaultConfigs().flatMap { listOf(it.displayName, it.statusLabel, it.note) })
    }
        .map(String::trim)
        .filter(String::isNotBlank)
        .distinct()
        .sortedBy { it.lowercase() }
}

internal object LanguageOverrideStore {
    private const val PrefName = "weather_language_overrides"
    private const val OverridesKey = "overrides_json"

    fun load(context: Context): Map<String, String> {
        val raw = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .getString(OverridesKey, null)
            .orEmpty()
        return parseDocument(raw).getOrElse { emptyMap() }
    }

    fun save(context: Context, overrides: Map<String, String>) {
        context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)
            .edit { putString(OverridesKey, encodeOverrides(clean(overrides))) }
    }

    fun clean(overrides: Map<String, String>): Map<String, String> = overrides
        .mapKeys { it.key.trim() }
        .mapValues { it.value }
        .filterKeys(String::isNotBlank)
        .filter { (key, value) -> value != key }
        .toSortedMap()

    fun exportDocument(overrides: Map<String, String>): String {
        val allStrings = (UiStringCatalog.defaultStrings() + overrides.keys).distinct().sorted()
        val values = JSONObject()
        allStrings.forEach { key ->
            values.put(key, overrides[key] ?: key)
        }
        return JSONObject()
            .put("version", 1)
            .put("note", "导入后请重启 M-Weather。")
            .put("strings", values)
            .toString(2)
    }

    fun exportToUri(context: Context, uri: Uri, overrides: Map<String, String>) {
        context.contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(exportDocument(overrides).toByteArray(Charsets.UTF_8))
        }
    }

    fun importFromUri(context: Context, uri: Uri): Result<Map<String, String>> = runCatching {
        val raw = context.contentResolver.openInputStream(uri)?.use { stream ->
            stream.readBytes().toString(Charsets.UTF_8)
        }.orEmpty()
        parseDocument(raw).getOrThrow()
    }

    fun parseDocument(raw: String): Result<Map<String, String>> = runCatching {
        val trimmed = raw.trim()
        if (trimmed.isBlank()) return@runCatching emptyMap()
        if (trimmed.startsWith("{")) {
            val root = JSONObject(trimmed)
            val strings = root.optJSONObject("strings") ?: root
            buildMap {
                strings.keys().forEach { key ->
                    strings.opt(key)
                        ?.takeUnless { it == JSONObject.NULL }
                        ?.toString()
                        ?.let { value -> put(key, value) }
                }
            }
        } else {
            trimmed
                .lineSequence()
                .map(String::trim)
                .filter { it.isNotBlank() && !it.startsWith("#") }
                .mapNotNull { line ->
                    val separator = when {
                        '\t' in line -> "\t"
                        "=" in line -> "="
                        ":" in line -> ":"
                        else -> return@mapNotNull null
                    }
                    val source = line.substringBefore(separator).trim()
                    val replacement = line.substringAfter(separator).trim()
                    source.takeIf(String::isNotBlank)?.let { it to replacement }
                }
                .toMap()
        }
    }.map(::clean)

    private fun encodeOverrides(overrides: Map<String, String>): String {
        val strings = JSONObject()
        overrides.forEach { (key, value) -> strings.put(key, value) }
        return JSONObject().put("version", 1).put("strings", strings).toString()
    }
}
