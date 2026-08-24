package com.weathermixer.sixq

import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class LocalVehicleRestrictionsTest {
    @Test
    fun beijingUsesCurrentRotation() {
        val detail = LocalVehicleRestrictions.find("北京市", chinaDate("2026-08-20 08:00:00"))

        assertTrue(detail.orEmpty().contains("今日限行尾号：4和9"))
    }

    @Test
    fun regularWorkdayPolicyDoesNotWarnOnWeekend() {
        assertNull(LocalVehicleRestrictions.find("北京市", chinaDate("2026-08-22 08:00:00")))
    }

    @Test
    fun cityWithoutStandingPassengerCarPolicyStaysQuiet() {
        assertNull(LocalVehicleRestrictions.find("南京市", chinaDate("2026-08-20 08:00:00")))
    }

    @Test
    fun expiredPolicyStaysQuiet() {
        assertNull(LocalVehicleRestrictions.find("深圳市", chinaDate("2026-09-17 08:00:00")))
    }

    private fun chinaDate(value: String): Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("Asia/Shanghai")
    }.parse(value) ?: error("Invalid test date")
}
