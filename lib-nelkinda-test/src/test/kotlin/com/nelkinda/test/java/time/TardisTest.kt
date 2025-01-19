package com.nelkinda.test.java.time

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.*

class TardisTest {
    @Test
    fun `by default, a Tardis gives me the system time`() {
        val tardis: Clock = Tardis.systemUTC()
        val now = tardis.instant()
        val systemNow = Clock.systemDefaultZone().instant()
        val duration = Duration.between(now, systemNow)
        assertTrue(duration.toMillis() < 200)
    }

    @Test
    fun `Tardis TimeZone`() {
        assertEquals(Clock.systemDefaultZone().zone, Tardis.systemDefaultZone().zone)
        assertEquals(Clock.systemUTC().zone, Tardis.systemUTC().zone)
        assertEquals(ZoneId.of("Europe/Berlin"), Tardis.systemDefaultZone().withZone(ZoneId.of("Europe/Berlin")).zone)
    }

    @Test
    fun `Tardis Time`() {
        val tardis = Tardis.fixed(Instant.EPOCH, ZoneId.of("UTC"))
        assertEquals(Instant.EPOCH, tardis.instant())
        assertEquals(ZoneId.of("UTC"), tardis.zone)
    }

    @Test
    fun `Tardis can change zone`() {
        val tardis = Tardis.fixed(Instant.EPOCH, ZoneId.of("UTC"))
        tardis.travelToZone(ZoneId.of("Asia/Kolkata"))
        assertEquals(ZoneId.of("Asia/Kolkata"), tardis.zone)
        assertEquals(Instant.EPOCH, tardis.instant())
    }

    @Test
    fun `Tardis can be fixed to a specific time`() {
        val tardis = Tardis.systemUTC().freezeTo(Instant.EPOCH)
        assertEquals(Instant.EPOCH, tardis.instant())
    }
}
