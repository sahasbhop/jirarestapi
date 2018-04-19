package app.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun LocalDate.atZeroHours(): LocalDateTime = this.atTime(0, 0, 0, 0)
fun LocalDateTime.inBangkok(): OffsetDateTime = OffsetDateTime.of(this, ZoneOffset.ofHours(7))
fun OffsetDateTime.resetTime() = this.withHour(0).withMinute(0).withSecond(0).withNano(0)

fun String.zeroHoursInBangkok(): OffsetDateTime {
    return LocalDate.parse(this).atZeroHours().inBangkok()
}

/**
 * Append spaces as prefix to expand string length to a specified size
 */
fun String.width(size: Int): String = String.format("%${size}s", this)
