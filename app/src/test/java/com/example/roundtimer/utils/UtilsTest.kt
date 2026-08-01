package com.example.roundtimer.utils

import org.junit.Assert
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class UtilsTest {

    private val clock = Clock.fixed(
        Instant.parse("2026-08-01T12:00:00Z"),
        ZoneOffset.UTC
    )

    @Test
    fun formatTimeTest() {
        val expect = "01:30"
        val result = Utils.formatTime(90)
        Assert.assertEquals(
            expect,
            result
        )
    }

    @Test
    fun `isToday true case`() {
        val result = Utils.isToday(
            date = "2026-08-01",
            clock = clock
        )
        Assert.assertTrue(result)
    }

    @Test
    fun `isToday false case`() {
        val result = Utils.isToday(
            date = "2026-08-02",
            clock = clock
        )
        Assert.assertFalse(result)
    }

}