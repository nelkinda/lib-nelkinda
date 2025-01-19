package com.nelkinda.io.cucumber

import com.nelkinda.test.java.time.Tardis
import io.cucumber.java.Before
import io.cucumber.java.ParameterType
import io.cucumber.java.en.Given
import jakarta.inject.Inject
import org.mockito.Mockito
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.devtools.DevTools
import org.openqa.selenium.devtools.v131.emulation.Emulation
import org.openqa.selenium.devtools.v131.emulation.model.VirtualTimePolicy
import org.openqa.selenium.devtools.v131.network.model.TimeSinceEpoch
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.annotation.Nullable

@Suppress("FunctionName")
class TardisSteps @Inject constructor(
    internal val tardis: Tardis,
    @Nullable
    webDriver: WebDriver?,
) {
    internal val devTools: DevTools =
        if (webDriver is ChromeDriver) webDriver.devTools.apply { createSession() }
        else {
            if (webDriver != null)
                System.err.println("warnings: WebDriver not a ChromeDriver, using mock DeTools instead of real DevTools, Tardis not fully functional")
            Mockito.mock()
        }

    @Before
    fun setupRegularClock() {
        tardis.clock = Clock.systemUTC()
        devTools.send(Emulation.setLocaleOverride(Optional.empty()))
        devTools.send(Emulation.setTimezoneOverride(""))
        devTools.send(Emulation.setVirtualTimePolicy(VirtualTimePolicy.ADVANCE, Optional.empty(), Optional.empty(), Optional.empty()))
    }

    @ParameterType("\\d{4,}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z")
    fun instant(text: String): Instant = Instant.parse(text)

    @ParameterType("\\d{4,}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")
    fun localDateTime(text: String): LocalDateTime = LocalDateTime.parse(text)

    @ParameterType("([A-Za-z]+/[A-Za-z_]+|UTC|Z)")
    fun zoneId(text: String): ZoneId = ZoneId.of(text)

    @ParameterType("(-?\\d+\\.\\d+|\\d+\\.\\d+[NS])?")
    fun latitude(text: String): Double {
        val suffix = text.last()
        val hasSuffix = suffix in "NS"
        val value = (if (hasSuffix) text.dropLast(1) else text).toDouble()
        require(value in -90.0..90.0) { "Invalid latitude: $value. Must be between -90.0 and +90.0" }
        return if (suffix == 'S') -value else value
    }

    @ParameterType("(-?\\d+\\.\\d+|\\d+\\.\\d+[EW])?")
    fun longitude(text: String): Double {
        val suffix = text.last()
        val hasSuffix = suffix in "EW"
        val value = (if (hasSuffix) text.dropLast(1) else text).toDouble()
        require(value in -180.0..180.0) { "Invalid longitude: $value. Must be between -180.0 and +180.0" }
        return if (suffix == 'W') -value else value
    }

    @ParameterType("\\d+\\.\\d+")
    fun accuracy(text: String): Double = text.toDouble().apply { require(this >= 0.0) { "Invalid accuracy: $this. Must be non-negative" } }

    @Given("it is {instant} at {zoneId}")
    fun `set Tardis to`(instant: Instant, zoneId: ZoneId) {
        tardis.freezeTo(instant, zoneId)
        devTools.send(Emulation.setLocaleOverride(Optional.of("en-US")))
        devTools.send(Emulation.setTimezoneOverride(zoneId.id))
        devTools.send(Emulation.setVirtualTimePolicy(VirtualTimePolicy.ADVANCE, Optional.empty(), Optional.empty(), Optional.of(TimeSinceEpoch(instant.toEpochMilli() / 1000))))
    }

    @Given("it is {localDateTime} at {zoneId}")
    fun `set Tardis to`(localDateTime: LocalDateTime, zoneId: ZoneId) {
        tardis.freezeTo(localDateTime.atZone(zoneId).toInstant(), zoneId)
    }

    @Given("my locale is {string}")
    fun `set Locale to`(locale: String) {
        devTools.send(Emulation.setLocaleOverride(Optional.of(locale)))
        devTools.send(Emulation.setUserAgentOverride("Selenium ChromeDriver w/ Nelkinda Tardis", Optional.of(locale), Optional.of(locale), Optional.empty()))
    }

    @Given("my local timezone is {zoneId}")
    fun `set Timezone to`(zoneId: ZoneId) {
        devTools.send(Emulation.setTimezoneOverride(zoneId.id))
    }

    @Given("my location is {latitude}, {longitude}, {accuracy}")
    fun `set Location to`(latitude: Double, longitude: Double, accuracy: Double) {
        devTools.send(Emulation.setGeolocationOverride(Optional.of(latitude), Optional.of(longitude), Optional.of(accuracy)))
    }
}
