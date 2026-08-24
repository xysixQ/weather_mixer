package com.weathermixer.sixq

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

internal object LocalVehicleRestrictions {
    private val chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai")

    private data class Policy(
        val city: String,
        val vehicleScope: String,
        val time: String,
        val area: String,
        val rule: String,
        val sourceName: String,
        val validThrough: String? = null,
        val workdaysOnly: Boolean = true,
        val rotatingTailNumbers: Boolean = false,
        val fixedWeekdayTailNumbers: Boolean = false,
    )

    private val policies = listOf(
        Policy(
            city = "北京",
            vehicleScope = "北京市号牌机动车；外埠载客汽车另受早晚高峰及进京证规定约束",
            time = "工作日 7:00-20:00",
            area = "五环路以内道路（不含五环路）",
            rule = "按工作日轮换两个尾号，纯电动小客车等按规定豁免",
            sourceName = "北京交警",
            validThrough = "2027-03-28",
            rotatingTailNumbers = true,
        ),
        Policy(
            city = "天津",
            vehicleScope = "本市及外埠号牌机动车；外埠及区域号牌小客车另受早晚高峰限制",
            time = "工作日 7:00-19:00",
            area = "外环线以内道路（不含外环线）",
            rule = "按工作日轮换两个尾号",
            sourceName = "天津交警",
            validThrough = "2027-03-28",
            rotatingTailNumbers = true,
        ),
        Policy(
            city = "成都",
            vehicleScope = "川A、川G及外地籍小型和微型载客汽车",
            time = "工作日 7:30-20:00",
            area = "成都绕城高速公路 G4202（不含）以内所有道路",
            rule = "星期一至星期五依次限行 1和6、2和7、3和8、4和9、5和0",
            sourceName = "成都交警",
            fixedWeekdayTailNumbers = true,
        ),
        Policy(
            city = "杭州",
            vehicleScope = "浙A区域号牌及非浙A号牌小客车",
            time = "工作日高架快速路 7:00-10:00、16:00-19:00；其他错峰道路 7:00-9:00、16:30-18:30",
            area = "绕城高速以内高架、快速路及规定错峰区域",
            rule = "适用车辆在错峰时段全号段限制通行；浙A普通号牌按现行尾号规则执行",
            sourceName = "杭州市公安局交通警察局",
        ),
        Policy(
            city = "兰州",
            vehicleScope = "核载不足 9 人的小型、微型载客汽车",
            time = "工作日 7:30-20:00",
            area = "城区规定限行区域",
            rule = "星期一至星期五依次限行 1和6、2和7、3和8、4和9、5和0",
            sourceName = "兰州公安交警",
            fixedWeekdayTailNumbers = true,
        ),
        Policy(
            city = "西安",
            vehicleScope = "限行区域内行驶的本地及外埠机动车，新能源汽车等按规定豁免",
            time = "工作日 7:00-20:00",
            area = "西安绕城高速以内及通告向南、向北扩展的围合区域",
            rule = "星期一至星期五依次限行 1和6、2和7、3和8、4和9、5和0",
            sourceName = "西安市公安局交通管理局",
            validThrough = "2026-11-06",
            fixedWeekdayTailNumbers = true,
        ),
        Policy(
            city = "重庆",
            vehicleScope = "所有渝籍和非渝籍号牌汽车，新能源号牌等按规定豁免",
            time = "工作日 7:00-9:00、17:00-19:30",
            area = "中心城区通告列明的桥梁和隧道",
            rule = "星期一至星期五依次限行 1和6、2和7、3和8、4和9、5和0",
            sourceName = "重庆市公安局交通管理局",
            validThrough = "2027-02-28",
            fixedWeekdayTailNumbers = true,
        ),
        Policy(
            city = "上海",
            vehicleScope = "外省市号牌小客车、临时号牌小客车等",
            time = "工作日 7:00-20:00；内环地面部分时段另有限制",
            area = "主要高架、城市快速路、桥梁和隧道",
            rule = "适用车辆在规定时段禁止进入公布的高架和快速路",
            sourceName = "上海市公安局",
        ),
        Policy(
            city = "深圳",
            vehicleScope = "非深圳号牌载客汽车",
            time = "工作日 7:00-9:00、17:30-19:30",
            area = "全市道路，通告列明的口岸、机场通道及深汕合作区道路除外",
            rule = "高峰时段限制通行，可按规定申请临时通行",
            sourceName = "深圳市公安局交通管理局",
            validThrough = "2026-09-16",
        ),
        Policy(
            city = "广州",
            vehicleScope = "非广州市籍小客车",
            time = "工作日 7:30-9:00、17:00-19:00",
            area = "高峰限行管控区域",
            rule = "高峰时段限制通行，每自然年可按规定预约部分工作日通行",
            sourceName = "广州市公安局",
        ),
    )

    fun find(cityName: String, now: Date = Date()): String? {
        val city = cityName.removeSuffix("市").trim()
        val policy = policies.firstOrNull { it.city == city } ?: return null
        val calendar = Calendar.getInstance(chinaTimeZone, Locale.CHINA).apply { time = now }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        if (policy.workdaysOnly && dayOfWeek in setOf(Calendar.SATURDAY, Calendar.SUNDAY)) return null
        if (policy.validThrough != null && now.after(endOfDay(policy.validThrough))) return null

        val tailNumber = when {
            policy.rotatingTailNumbers -> currentRotatingTail(now)
            policy.fixedWeekdayTailNumbers -> fixedWeekdayTail(calendar)
            else -> null
        }
        val tailText = tailNumber?.let { "；今日限行尾号：$it" }.orEmpty()
        return "${policy.city}存在机动车限行措施。适用车辆：${policy.vehicleScope}；时间：${policy.time}；区域：${policy.area}；${policy.rule}$tailText。" +
            "本地资料核验于 2026-08-20（${policy.sourceName}），法定节假日、调休和临时调整请以当地交管通知为准。"
    }

    fun allDetails(): List<String> = policies.map { policy ->
        buildString {
            append(policy.city)
            append("：适用车辆：${policy.vehicleScope}；时间：${policy.time}；区域：${policy.area}；${policy.rule}。")
            policy.validThrough?.let { append(" 当前资料有效期至 $it。") }
            append(" 信息来源：${policy.sourceName}，本地资料核验于 2026-08-20。")
        }
    }

    private fun currentRotatingTail(now: Date): String? {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { timeZone = chinaTimeZone }.format(now)
        val mondayIndex = when {
            date in "2026-03-30".."2026-06-28" -> 4
            date in "2026-06-29".."2026-09-27" -> 0
            date in "2026-09-28".."2026-12-27" -> 1
            date in "2026-12-28".."2027-03-28" -> 2
            else -> return null
        }
        val calendar = Calendar.getInstance(chinaTimeZone, Locale.CHINA).apply { time = now }
        val weekdayOffset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY
        if (weekdayOffset !in 0..4) return null
        return listOf("1和6", "2和7", "3和8", "4和9", "5和0")[(mondayIndex + weekdayOffset) % 5]
    }

    private fun fixedWeekdayTail(calendar: Calendar): String? {
        val weekdayOffset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY
        if (weekdayOffset !in 0..4) return null
        return listOf("1和6", "2和7", "3和8", "4和9", "5和0")[weekdayOffset]
    }

    private fun endOfDay(date: String): Date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply {
        timeZone = chinaTimeZone
    }.parse("$date 23:59:59") ?: Date(0)
}
