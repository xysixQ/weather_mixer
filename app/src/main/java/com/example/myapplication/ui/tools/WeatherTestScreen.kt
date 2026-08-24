package com.weathermixer.sixq

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.TimeZone

internal fun weatherTestNightForClock(clockText: String): Boolean? {
    val match = Regex("^(\\d{1,2}):(\\d{2})$").matchEntire(clockText.trim()) ?: return null
    val hour = match.groupValues[1].toIntOrNull() ?: return null
    val minute = match.groupValues[2].toIntOrNull() ?: return null
    if (hour !in 0..23 || minute !in 0..59) return null
    return hour < 6 || hour >= 18
}

internal fun resolveWeatherTestTimeMillis(
    clockText: String,
    timeZoneId: String,
    baseTimeMillis: Long,
): Long? {
    val match = Regex("^(\\d{1,2}):(\\d{2})$").matchEntire(clockText.trim()) ?: return null
    val hour = match.groupValues[1].toIntOrNull() ?: return null
    val minute = match.groupValues[2].toIntOrNull() ?: return null
    if (hour !in 0..23 || minute !in 0..59) return null
    val requestedId = timeZoneId.trim()
    if (requestedId.isBlank()) return null
    val timeZone = TimeZone.getTimeZone(requestedId)
    val validGmtAlias = requestedId.equals("GMT", true) || requestedId.equals("UTC", true) ||
        requestedId.startsWith("GMT+") || requestedId.startsWith("GMT-")
    if (timeZone.id == "GMT" && !validGmtAlias) return null
    return Calendar.getInstance(timeZone).apply {
        timeInMillis = baseTimeMillis
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

internal enum class WeatherTestAlertPreset(
    val label: String,
    private val alertLevel: AlertLevel,
    private val alertTitle: String,
    private val alertDetail: String,
) {
    None(
        label = "无预警",
        alertLevel = AlertLevel.None,
        alertTitle = "无显著预警",
        alertDetail = "测试模式未注入天气预警。",
    ),
    RainBlue(
        label = "暴雨蓝色",
        alertLevel = AlertLevel.Rain,
        alertTitle = "暴雨蓝色预警",
        alertDetail = "未来 3 小时可能出现短时强降雨，低洼道路和地下空间需提前留意积水风险。",
    ),
    RainRed(
        label = "暴雨红色",
        alertLevel = AlertLevel.Severe,
        alertTitle = "暴雨红色预警",
        alertDetail = "强降雨已接近或达到高影响阈值，请减少非必要出行，远离河道、涵洞和低洼积水区域。",
    ),
    HeatOrange(
        label = "高温橙色",
        alertLevel = AlertLevel.Heat,
        alertTitle = "高温橙色预警",
        alertDetail = "白天最高气温可能升至 37℃ 以上，户外作业和长时间通勤需要防暑降温并及时补水。",
    ),
    Thunderstorm(
        label = "雷暴大风",
        alertLevel = AlertLevel.Severe,
        alertTitle = "雷暴大风黄色预警",
        alertDetail = "短时雷雨大风可能伴随强雷电和局地冰雹，请避开广告牌、临时构筑物和高处作业。",
    ),
    Typhoon(
        label = "台风",
        alertLevel = AlertLevel.Severe,
        alertTitle = "台风橙色预警",
        alertDetail = "台风影响期间阵风明显增强，沿海和高层区域需加固门窗，关注交通停运和属地避险通知。",
    ),
    ColdWave(
        label = "寒潮",
        alertLevel = AlertLevel.Severe,
        alertTitle = "寒潮蓝色预警",
        alertDetail = "气温将明显下降并伴随大风，注意添衣保暖，户外管线、农作物和易受寒人群需提前防护。",
    ),
    Fog(
        label = "大雾",
        alertLevel = AlertLevel.Severe,
        alertTitle = "大雾橙色预警",
        alertDetail = "部分路段能见度可能显著下降，驾车请打开雾灯、降低速度并保持更长跟车距离。",
    ),
    RoadIce(
        label = "道路结冰",
        alertLevel = AlertLevel.Severe,
        alertTitle = "道路结冰黄色预警",
        alertDetail = "低温和降水后路面可能结冰，步行与驾车都需避开桥面、坡道和阴影路段。",
    );

    val alert: WeatherAlert
        get() = WeatherAlert(alertLevel, alertTitle, alertDetail)
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun WeatherTestScreen(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    weatherCode: String,
    selectedCondition: WeatherCondition,
    selectedAlertPreset: WeatherTestAlertPreset,
    clockText: String,
    timeZoneId: String,
    onEnabledChange: (Boolean) -> Unit,
    onWeatherCodeChange: (String) -> Unit,
    onConditionSelected: (WeatherCondition) -> Unit,
    onAlertPresetSelected: (WeatherTestAlertPreset) -> Unit,
    onClockTextChange: (String) -> Unit,
    onTimeZoneIdChange: (String) -> Unit,
    onBack: () -> Unit,
) {
    val parsedCode = weatherCode.takeIf(String::isNotBlank)?.let(::xiaomiWeatherCondition)
    val validClock = weatherTestNightForClock(clockText) != null
    val validTimeZone = resolveWeatherTestTimeMillis(clockText, timeZoneId, System.currentTimeMillis()) != null
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
                    "测试页（仅供 Debug 使用）",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onEnabledChange(!enabled) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Science, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("测试数据覆盖", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            if (enabled) "已保存进入测试前的天气数据" else "关闭后恢复原数据且不会自动刷新",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(checked = enabled, onCheckedChange = onEnabledChange)
                }
            }
        }

        if (enabled) {
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = weatherCode,
                    onValueChange = onWeatherCodeChange,
                    label = { Text("天气代码") },
                    supportingText = {
                        Text(
                            when {
                                weatherCode.isBlank() -> "支持小米、和风天气代码及中英文天气描述"
                                parsedCode != null -> "识别为：${parsedCode.label}"
                                else -> "暂时无法识别该代码或描述"
                            }
                        )
                    },
                    singleLine = true,
                )
            }

            item {
                Text("手动选择天气", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.padding(top = 4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    WeatherCondition.entries.forEach { condition ->
                        FilterChip(
                            selected = condition == selectedCondition,
                            onClick = { onConditionSelected(condition) },
                            label = { Text(condition.label) },
                        )
                    }
                }
            }

            item {
                val selectedAlert = selectedAlertPreset.alert
                Text("预警场景", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.padding(top = 4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    WeatherTestAlertPreset.entries.forEach { preset ->
                        FilterChip(
                            selected = preset == selectedAlertPreset,
                            onClick = { onAlertPresetSelected(preset) },
                            label = { Text(preset.label) },
                        )
                    }
                }
                Text(
                    "${selectedAlert.title}：${selectedAlert.detail}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            item { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)) }

            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = clockText,
                    onValueChange = onClockTextChange,
                    label = { Text("时间（HH:mm）") },
                    isError = !validClock,
                    supportingText = { if (!validClock) Text("请输入 00:00 到 23:59") },
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = timeZoneId,
                    onValueChange = onTimeZoneIdChange,
                    label = { Text("时区") },
                    isError = validClock && !validTimeZone,
                    supportingText = { Text("例如 Asia/Shanghai、America/Los_Angeles 或 UTC") },
                    singleLine = true,
                )
            }
        }
    }
}
