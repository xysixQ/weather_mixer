package com.weathermixer.sixq

import java.time.Instant
import java.util.Calendar
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherTestModeTest {
    @Test
    fun settingsPressRequiresTheFullFiveSecondsForWeatherTest() {
        assertEquals(SettingsPressAction.OpenSettings, settingsPressAction(120L))
        assertEquals(SettingsPressAction.Ignore, settingsPressAction(351L))
        assertEquals(SettingsPressAction.Ignore, settingsPressAction(4_999L))
        assertEquals(SettingsPressAction.OpenWeatherTest, settingsPressAction(5_000L))
    }

    @Test
    fun testClockUsesExpectedDayAndNightBoundaries() {
        assertTrue(weatherTestNightForClock("01:00") == true)
        assertFalse(weatherTestNightForClock("06:00") == true)
        assertFalse(weatherTestNightForClock("17:59") == true)
        assertTrue(weatherTestNightForClock("18:00") == true)
        assertNull(weatherTestNightForClock("24:00"))
        assertNull(weatherTestNightForClock("not-a-time"))
    }

    @Test
    fun testTimeKeepsTheRequestedLocalClockAndTimeZone() {
        val resolved = resolveWeatherTestTimeMillis(
            clockText = "21:35",
            timeZoneId = "America/Los_Angeles",
            baseTimeMillis = Instant.parse("2026-08-24T04:00:00Z").toEpochMilli(),
        )
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles")).apply {
            timeInMillis = requireNotNull(resolved)
        }

        assertEquals(21, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(35, calendar.get(Calendar.MINUTE))
        assertEquals(0, calendar.get(Calendar.SECOND))
    }

    @Test
    fun invalidTestTimeZoneDoesNotSilentlyBecomeGmt() {
        assertNull(resolveWeatherTestTimeMillis("12:00", "Mars/Olympus", 0L))
        assertNull(resolveWeatherTestTimeMillis("12:60", "Asia/Shanghai", 0L))
    }

    @Test
    fun warningPresetsCoverEveryAlertLevelUsedByTheDashboard() {
        val levels = WeatherTestAlertPreset.entries.map { it.alert.level }.toSet()
        assertTrue(AlertLevel.None in levels)
        assertTrue(AlertLevel.Rain in levels)
        assertTrue(AlertLevel.Heat in levels)
        assertTrue(AlertLevel.Severe in levels)
        val uniqueLabels = WeatherTestAlertPreset.entries.map { it.label }.distinct()
        assertEquals(WeatherTestAlertPreset.entries.size, uniqueLabels.size)
    }
}
