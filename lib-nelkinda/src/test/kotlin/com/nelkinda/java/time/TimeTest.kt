package com.nelkinda.java.time

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalTime

class TimeTest {
    @Test
    fun `test toLocalTime`() {
        assertEquals(LocalTime.MIDNIGHT, Duration.ZERO.toLocalTime())
        assertEquals(LocalTime.NOON, Duration.ofHours(12).toLocalTime())
    }

    @Test
    fun `test toDuration`() {
        assertEquals(Duration.ZERO, LocalTime.MIDNIGHT.toDuration())
        assertEquals(Duration.ofHours(12), LocalTime.NOON.toDuration())
    }
}
