package com.weathermixer.sixq

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRepositoryTest {
    @Test
    fun xiaomiAndMsnDefaultsDoNotRequireKeys() {
        val configs = ApiConfigDefaults.defaultConfigs()
        val xiaomi = configs.first { it.sourceId == SourceId.XiaomiWeather }
        val msn = configs.first { it.sourceId == SourceId.MsnWeather }

        assertFalse(xiaomi.requiresKey)
        assertTrue(xiaomi.apiKey.isBlank())
        assertFalse(xiaomi.endpoint.contains("appKey="))
        assertFalse(xiaomi.endpoint.contains("sign="))
        assertFalse(msn.requiresKey)
        assertTrue(msn.apiKey.isBlank())
        assertFalse(msn.endpoint.contains("apiKey="))
    }

    @Test
    fun snapshotKeepsForecastCardsVisibleWithoutForecastSources() {
        val snapshot = WeatherRepository().buildSnapshot(
            region = District(
                countryCode = "CN",
                province = "北京市",
                city = "北京市",
                district = "通州区",
                latitude = 39.9025,
                longitude = 116.6564,
            ),
            profile = null,
        )

        assertFalse(snapshot.dailyForecast.isEmpty())
        assertFalse(snapshot.hourlyForecast.isEmpty())
    }
    @Test
    fun themeModeOnlyChangesAppearanceWhenResolvedDarkStateChanges() {
        assertFalse(ThemeMode.Light.resolvesToDarkTheme(systemDarkTheme = false))
        assertFalse(ThemeMode.System.resolvesToDarkTheme(systemDarkTheme = false))
        assertTrue(ThemeMode.Dark.resolvesToDarkTheme(systemDarkTheme = false))
        assertTrue(ThemeMode.System.resolvesToDarkTheme(systemDarkTheme = true))
    }
}