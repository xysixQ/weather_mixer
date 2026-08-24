package com.weathermixer.sixq

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

internal object WeatherContract {
    data class UiState(
        val currentPage: AppPage,
        val isRefreshing: Boolean,
        val backgroundMotionEnabled: Boolean,
        val dashboardOrder: List<DashboardBlock>,
        val metricOrder: List<DashboardDetail>,
        val temperatureUnit: TemperatureUnit,
        val reverseTemperatureSwipe: Boolean,
        val hapticFeedbackEnabled: Boolean,
    )

    sealed interface Intent {
        data class Navigate(val page: AppPage) : Intent
        data class SetRefreshing(val refreshing: Boolean) : Intent
        data class SetBackgroundMotion(val enabled: Boolean) : Intent
        data class SetDashboardOrder(val order: List<DashboardBlock>) : Intent
        data class SetMetricOrder(val order: List<DashboardDetail>) : Intent
        data class SetTemperatureUnit(val unit: TemperatureUnit) : Intent
        data class SetReverseTemperatureSwipe(val reversed: Boolean) : Intent
        data class SetHapticFeedback(val enabled: Boolean) : Intent
        data class ShowMessage(val message: String) : Intent
    }

    sealed interface Effect {
        data class Snackbar(val message: String) : Effect
    }
}

internal class WeatherStore(initialState: WeatherContract.UiState) {
    var state by mutableStateOf(initialState)
        private set

    private val effectChannel = Channel<WeatherContract.Effect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    fun accept(intent: WeatherContract.Intent) {
        state = reduce(state, intent)
        if (intent is WeatherContract.Intent.ShowMessage) {
            effectChannel.trySend(WeatherContract.Effect.Snackbar(intent.message))
        }
    }

    private fun reduce(
        current: WeatherContract.UiState,
        intent: WeatherContract.Intent,
    ): WeatherContract.UiState = when (intent) {
        is WeatherContract.Intent.Navigate -> current.copy(currentPage = intent.page)
        is WeatherContract.Intent.SetRefreshing -> current.copy(isRefreshing = intent.refreshing)
        is WeatherContract.Intent.SetBackgroundMotion -> current.copy(backgroundMotionEnabled = intent.enabled)
        is WeatherContract.Intent.SetDashboardOrder -> current.copy(dashboardOrder = intent.order.distinct())
        is WeatherContract.Intent.SetMetricOrder -> current.copy(metricOrder = intent.order.distinct())
        is WeatherContract.Intent.SetTemperatureUnit -> current.copy(temperatureUnit = intent.unit)
        is WeatherContract.Intent.SetReverseTemperatureSwipe -> current.copy(reverseTemperatureSwipe = intent.reversed)
        is WeatherContract.Intent.SetHapticFeedback -> current.copy(hapticFeedbackEnabled = intent.enabled)
        is WeatherContract.Intent.ShowMessage -> current
    }
}
