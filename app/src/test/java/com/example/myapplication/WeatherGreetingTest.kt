package com.weathermixer.sixq

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherGreetingTest {
    @Test
    fun shanghaiEarlyMorningUsesStillAwakeGreeting() {
        val greeting = weatherGreetingFor(
            timeMillis = Instant.parse("2026-08-20T18:30:00Z").toEpochMilli(),
            region = District(
                countryCode = "CN",
                province = "江苏省",
                city = "南京市",
                district = "雨花台区",
                latitude = 31.995,
                longitude = 118.78,
            ),
            condition = WeatherCondition.Sunny,
            profile = DefaultUserProfile,
        )

        assertEquals("你还在啊？！（打哈欠）", greeting.salutation)
    }

    @Test
    fun losAngelesSummerEveningUsesLocalTimeAndStudentPrompt() {
        val greeting = weatherGreetingFor(
            timeMillis = Instant.parse("2026-08-21T04:00:00Z").toEpochMilli(),
            region = District(
                countryCode = "US",
                province = "California",
                city = "Los Angeles",
                district = "Los Angeles",
                latitude = 34.0522,
                longitude = -118.2437,
            ),
            condition = WeatherCondition.Sunny,
            profile = DefaultUserProfile.copy(occupation = Occupation.Student),
        )

        assertEquals("晚上好~", greeting.salutation)
        assertEquals("现在天气不错", greeting.weatherLine)
        assertTrue(greeting.prompt.contains("学习"))
    }

    @Test
    fun shanghaiLateNightUsesNightGreetingAndConversationalPrompt() {
        val greeting = weatherGreetingFor(
            timeMillis = Instant.parse("2026-08-21T15:30:00Z").toEpochMilli(),
            region = District(
                countryCode = "CN",
                province = "江苏省",
                city = "南京市",
                district = "雨花台区",
                latitude = 31.995,
                longitude = 118.78,
            ),
            condition = WeatherCondition.Cloudy,
            profile = DefaultUserProfile,
        )

        assertEquals("晚安~~", greeting.salutation)
        assertEquals("现在没有月亮哦", greeting.weatherLine)
        assertTrue(
            greeting.prompt in setOf(
                "还没睡呢？",
                "明天天气怎么样？让我看看~",
                "该让眼睛休息一下啦~",
            )
        )
    }

    @Test
    fun shanghaiCloudyDayMentionsSun() {
        val greeting = weatherGreetingFor(
            timeMillis = Instant.parse("2026-08-21T04:00:00Z").toEpochMilli(),
            region = District(
                countryCode = "CN",
                province = "江苏省",
                city = "南京市",
                district = "雨花台区",
                latitude = 31.995,
                longitude = 118.78,
            ),
            condition = WeatherCondition.Cloudy,
            isNight = false,
            profile = DefaultUserProfile,
        )

        assertEquals("现在没有太阳哦", greeting.weatherLine)
    }

    @Test
    fun reminderUsesOnlyTheNextThreeHoursAndPrioritizesRain() {
        val now = Instant.parse("2026-08-21T02:00:00Z").toEpochMilli()
        val reminderCondition = reminderConditionForNextThreeHours(
            timeMillis = now,
            hourlyForecast = listOf(
                HourlyForecast(now + 60 * 60 * 1_000L, 28.0, WeatherCondition.Sunny, 5.0, null, null, null),
                HourlyForecast(now + 2 * 60 * 60 * 1_000L, 27.0, WeatherCondition.Rain, 70.0, null, null, null),
                HourlyForecast(now + 4 * 60 * 60 * 1_000L, 26.0, WeatherCondition.Storm, 90.0, null, null, null),
            ),
            fallback = WeatherCondition.Sunny,
        )

        assertEquals(WeatherCondition.Rain, reminderCondition)
    }
}
