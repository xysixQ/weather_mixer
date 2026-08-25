package com.weathermixer.sixq

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherRepositoryTest {
    @Test
    fun xiaomiAndMsnDefaultsUseHiddenEndpointCredentialsWithoutUserKeys() {
        val configs = ApiConfigDefaults.defaultConfigs()
        val xiaomi = configs.first { it.sourceId == SourceId.XiaomiWeather }
        val msn = configs.first { it.sourceId == SourceId.MsnWeather }

        assertFalse(xiaomi.requiresKey)
        assertTrue(xiaomi.apiKey.isBlank())
        assertTrue(xiaomi.endpoint.contains("appKey="))
        assertTrue(xiaomi.endpoint.contains("sign="))
        assertFalse(msn.requiresKey)
        assertTrue(msn.apiKey.isBlank())
        assertTrue(msn.endpoint.contains("apiKey="))

        val hiddenXiaomiEndpoint = hideBuiltInEndpointCredentials(xiaomi.endpoint, xiaomi.endpoint)
        val hiddenMsnEndpoint = hideBuiltInEndpointCredentials(msn.endpoint, msn.endpoint)
        assertTrue(hiddenXiaomiEndpoint.contains("appKey=$BuiltInCredentialPlaceholder"))
        assertTrue(hiddenXiaomiEndpoint.contains("sign=$BuiltInCredentialPlaceholder"))
        assertTrue(hiddenMsnEndpoint.contains("apiKey=$BuiltInCredentialPlaceholder"))
        assertEquals(xiaomi.endpoint, restoreBuiltInEndpointCredentials(hiddenXiaomiEndpoint, xiaomi.endpoint))
        assertEquals(msn.endpoint, restoreBuiltInEndpointCredentials(hiddenMsnEndpoint, msn.endpoint))
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

    @Test
    fun builtInNoKeyWeatherSourcesAreNotAutoDisabledByApiFailures() {
        val configs = ApiConfigDefaults.defaultConfigs()
        val xiaomi = configs.first { it.sourceId == SourceId.XiaomiWeather }
        val msn = configs.first { it.sourceId == SourceId.MsnWeather }
        val amap = configs.first { it.sourceId == SourceId.Amap }

        assertFalse(shouldAutoDisableApiSource(xiaomi))
        assertFalse(shouldAutoDisableApiSource(msn))
        assertTrue(shouldAutoDisableApiSource(amap))
    }

    @Test
    fun baiduIpLocationCanRemainSelectedWhenConfigIsNotReady() {
        val configs = ApiConfigDefaults.defaultConfigs().map { config ->
            if (config.sourceId == SourceId.BaiduIpLocation) config.copy(apiKey = "") else config
        }

        assertTrue(configs.first { it.sourceId == SourceId.BaiduIpLocation }.requiresKey)
        assertFalse(configs.first { it.sourceId == SourceId.BaiduIpLocation }.isReady)
        assertFalse(isBaiduIpLocationReady(configs))
    }
}