package com.weathermixer.sixq

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherStoreTest {
    private fun createStore() = WeatherStore(
        WeatherContract.UiState(
            currentPage = AppPage.Dashboard,
            isRefreshing = false,
            backgroundMotionEnabled = true,
            dashboardOrder = listOf(DashboardBlock.Daily, DashboardBlock.Hourly),
            metricOrder = listOf(DashboardDetail.Wind, DashboardDetail.Humidity),
            temperatureUnit = TemperatureUnit.Celsius,
            reverseTemperatureSwipe = false,
            hapticFeedbackEnabled = false,
        )
    )

    @Test
    fun navigationAndRefreshIntentsReduceToNewState() {
        val store = createStore()

        store.accept(WeatherContract.Intent.Navigate(AppPage.Tools))
        store.accept(WeatherContract.Intent.SetRefreshing(true))

        assertEquals(AppPage.Tools, store.state.currentPage)
        assertEquals(1, store.state.currentPage.navigationDepth)
        assertTrue(store.state.isRefreshing)
    }

    @Test
    fun preferenceIntentsKeepAUniqueOrder() {
        val store = createStore()

        store.accept(
            WeatherContract.Intent.SetMetricOrder(
                listOf(DashboardDetail.Wind, DashboardDetail.Humidity, DashboardDetail.Wind)
            )
        )
        store.accept(WeatherContract.Intent.SetTemperatureUnit(TemperatureUnit.Kelvin))
        store.accept(WeatherContract.Intent.SetHapticFeedback(true))

        assertEquals(listOf(DashboardDetail.Wind, DashboardDetail.Humidity), store.state.metricOrder)
        assertEquals(TemperatureUnit.Kelvin, store.state.temperatureUnit)
        assertTrue(store.state.hapticFeedbackEnabled)
        assertFalse(store.state.reverseTemperatureSwipe)
    }

    @Test
    fun dashboardBlockCanMoveFarForwardThenBackWithoutLosingItsIdentity() {
        val original = listOf(
            DashboardBlock.Daily,
            DashboardBlock.Hourly,
            DashboardBlock.Precipitation,
            DashboardBlock.Wind,
            DashboardBlock.Humidity,
            DashboardBlock.Pressure,
        )
        val movedToSix = moveDashboardBlock(original, 2, 5)
        val movedBackToFour = moveDashboardBlock(
            movedToSix,
            movedToSix.indexOf(DashboardBlock.Precipitation),
            3,
        )

        assertEquals(DashboardBlock.Precipitation, movedBackToFour[3])
        assertEquals(original.toSet(), movedBackToFour.toSet())
        assertEquals(original.size, movedBackToFour.distinct().size)
    }

    @Test
    fun everyDashboardDetailHasAnIntroduction() {
        DashboardDetail.entries.forEach { detail ->
            assertTrue(detailIntroduction(detail).isNotBlank())
        }
    }
}
