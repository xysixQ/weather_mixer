package com.weathermixer.sixq

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherConditionTest {
    @Test
    fun meteostatUsesRapidApiAndStationHourlyFallback() {
        val config = ApiConfigDefaults.defaultConfigs().first { it.sourceId == SourceId.Meteostat }

        assertTrue(config.enabled)
        assertTrue(config.requiresKey)
        assertTrue(config.endpoint.contains("point/hourly"))
        assertTrue(config.note.contains("station/hourly"))
    }

    @Test
    fun fusionKeepsDewPointWhenASourceProvidesIt() {
        val region = District("CN", "江苏省", "南京市", "雨花台区", 31.995, 118.78)
        val source = reading(SourceId.XiaomiWeather, WeatherCondition.Cloudy).copy(dewPointC = 15.2)

        assertEquals(15.2, WeatherFusionEngine.fuse(region, listOf(source)).weather.dewPointC!!, 0.001)
    }

    @Test
    fun explicitCodesAndDescriptionsKeepRareWeatherTypes() {
        assertEquals(WeatherCondition.ThunderShower, xiaomiWeatherCondition("雷阵雨"))
        assertEquals(WeatherCondition.Sleet, xiaomiWeatherCondition("rain and snow"))
        assertEquals(WeatherCondition.Snow, xiaomiWeatherCondition("heavy snow"))
        assertEquals(WeatherCondition.Haze, xiaomiWeatherCondition("53"))
        assertEquals(WeatherCondition.Sandstorm, xiaomiWeatherCondition("508"))
        assertEquals(WeatherCondition.Fog, meteostatCondition(5))
        assertEquals(WeatherCondition.Hail, meteostatCondition(24))
    }

    @Test
    fun hourlyPrecipitationRefinesOnlyMeasuredRain() {
        assertEquals(WeatherCondition.LightRain, xiaomiWeatherCondition("rain", 1.2))
        assertEquals(WeatherCondition.ModerateRain, xiaomiWeatherCondition("rain", 5.0))
        assertEquals(WeatherCondition.HeavyRain, xiaomiWeatherCondition("rain", 12.0))
        assertEquals(WeatherCondition.Rainstorm, xiaomiWeatherCondition("rain", 20.0))
        assertEquals(WeatherCondition.Rain, xiaomiWeatherCondition("rain"))
        assertEquals(WeatherCondition.Snow, xiaomiWeatherCondition("snow", 20.0))
    }

    @Test
    fun fusionPoolsRainVariantsBeforeChoosingACondition() {
        val region = District("CN", "江苏省", "南京市", "雨花台区", 31.995, 118.78)
        val readings = listOf(
            reading(SourceId.XiaomiWeather, WeatherCondition.LightRain),
            reading(SourceId.Amap, WeatherCondition.ModerateRain),
            reading(SourceId.MsnWeather, WeatherCondition.Cloudy),
        )

        assertEquals(WeatherVisualFamily.Rain, WeatherFusionEngine.fuse(region, readings).weather.condition.visualFamily)
    }

    private fun reading(sourceId: SourceId, condition: WeatherCondition) = WeatherReading(
        source = WeatherSource(sourceId, sourceId.name, "实时"),
        temperatureC = 20.0,
        feelsLikeC = 20.0,
        rainProbability = null,
        rainNextHourMm = null,
        windKph = null,
        aqi = null,
        pm25 = null,
        uvIndex = null,
        humidityPercent = null,
        pollenLevel = null,
        sporeLevel = null,
        condition = condition,
        alert = null,
    )
}
