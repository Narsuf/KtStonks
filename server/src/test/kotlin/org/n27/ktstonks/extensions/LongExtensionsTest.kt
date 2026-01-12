package org.n27.ktstonks.extensions

import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LongExtensionsTest {

    @Test
    fun `isToday should return true for current timestamp`() {
        val todayTimestamp = Instant.now().toEpochMilli()
        assertTrue(todayTimestamp.isToday())
    }

    @Test
    fun `isToday should return false for a past timestamp`() {
        val yesterdayTimestamp = Instant.now().minus(1, ChronoUnit.DAYS).toEpochMilli()
        assertFalse(yesterdayTimestamp.isToday())
    }

    @Test
    fun `isToday should return false for a future timestamp`() {
        val tomorrowTimestamp = Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()
        assertFalse(tomorrowTimestamp.isToday())
    }
}
