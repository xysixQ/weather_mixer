package com.weathermixer.sixq

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonalizedAdviceTest {
    private val region = District("CN", "江苏省", "南京市", "雨花台区", 31.995, 118.78)

    @Test
    fun studentSunAdviceDoesNotUseOutdoorWorkerCopy() {
        val advice = adviceFor(Occupation.Student, allergens = setOf(Allergen.Uv), uv = 9.0)
        val sun = advice.first { it.title == "防晒" }.detail

        assertFalse(sun.contains("户外工作者"))
        assertTrue(sun.contains("上学前"))
    }

    @Test
    fun homebodySunAdviceDoesNotAskForOutdoorReapplication() {
        val advice = adviceFor(Occupation.Homebody, allergens = setOf(Allergen.Uv), uv = 9.0)
        val sun = advice.first { it.title == "防晒" }.detail

        assertFalse(sun.contains("补涂"))
        assertTrue(sun.contains("不外出"))
    }

    @Test
    fun ultravioletSensitivityAloneDoesNotCreatePollenAdvice() {
        val advice = adviceFor(Occupation.Student, allergens = setOf(Allergen.Uv), uv = 3.0)

        assertFalse(advice.any { it.title == "过敏原" })
    }

    @Test
    fun pollenSelectionStillCreatesSpecificAllergenAdvice() {
        val advice = adviceFor(Occupation.Homebody, allergens = setOf(Allergen.Pollen), uv = 3.0)

        assertTrue(advice.first { it.title == "过敏原" }.detail.contains("花粉"))
    }

    private fun adviceFor(
        occupation: Occupation,
        allergens: Set<Allergen>,
        uv: Double,
    ): List<PersonalizedAdvice> = OfflineAdviceEngine.buildAdvice(
        profile = UserProfile(occupation, CommuteMode.Other, allergens, false),
        region = region,
        weather = FusedWeather(
            temperatureC = 26.0,
            feelsLikeC = 27.0,
            rainProbability = 10.0,
            rainNextHourMm = 0.0,
            windKph = 8.0,
            aqi = 55,
            pm25 = 18.0,
            uvIndex = uv,
            humidityPercent = 62,
            pollenLevel = 2,
            sporeLevel = 1,
            condition = WeatherCondition.Sunny,
            alert = WeatherAlert(AlertLevel.None, "无显著预警", ""),
            confidencePercent = 90,
        ),
    )
}
