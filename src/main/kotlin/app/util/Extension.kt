package app.util

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun String.zeroHoursInBangkok(): OffsetDateTime {
    val localDateTime = LocalDate.parse(this).atTime(0, 0, 0, 0)
    return OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(7))
}

fun OffsetDateTime.resetTime() = this.withHour(0).withMinute(0).withSecond(0).withNano(0)

/**
 * Append spaces as prefix to expand string length to a specified size
 */
fun String.width(size: Int): String = String.format("%${size}s", this)
