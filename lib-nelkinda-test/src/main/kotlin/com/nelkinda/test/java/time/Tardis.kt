package com.nelkinda.test.java.time

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

/**
 * A Tardis is a Clock that can travel in time and space.
 * It is useful for testing time-dependent code and can be used as a drop-in replacement for a Clock bean.
 */
data class Tardis(
    var clock: Clock = Clock.systemDefaultZone()
) : Clock() {
    override fun instant(): Instant = clock.instant()
    override fun withZone(zone: ZoneId): Tardis = copy(clock = clock.withZone(zone))
    override fun getZone(): ZoneId = clock.zone

    fun plus(duration: Duration) {
        clock = Clock.fixed(clock.instant().plus(duration), zone)
    }

    companion object {
        fun systemDefaultZone(): Tardis = from(Clock.systemDefaultZone())
        fun systemUTC(): Tardis = from(Clock.systemUTC())
        fun fixed(instant: Instant, zone: ZoneId): Tardis = from(Clock.fixed(instant, zone))
        fun from(clock: Clock): Tardis = Tardis(clock)
    }

    /**
     * Tell this Tardis to travel to a different time zone.
     * This will not travel in time.
     * @param zone Zone to which to travel
     * @return this (fluent API)
     */
    fun travelToZone(zone: ZoneId): Tardis {
        clock = clock.withZone(zone)
        return this
    }

    /**
     * Tell this Tardis to stay frozen in time.
     * @return this (fluent API)
     */
    fun freezeTo(instant: Instant = clock.instant(), zone: ZoneId = clock.zone): Tardis {
        clock = Clock.fixed(instant, zone)
        return this
    }
}
