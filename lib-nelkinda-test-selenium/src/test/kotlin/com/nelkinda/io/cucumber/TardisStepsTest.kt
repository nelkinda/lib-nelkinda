package com.nelkinda.io.cucumber

import com.nelkinda.test.java.time.Tardis
import io.cucumber.java.ParameterType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.devtools.Command
import org.openqa.selenium.devtools.DevTools
import java.time.*
import java.util.regex.Pattern

class TardisStepsTest {
    companion object {
        private val latitudePattern: Pattern = TardisSteps::class.java.getParameterTypePattern("latitude")
        private val longitudePattern = TardisSteps::class.java.getParameterTypePattern("longitude")

        private fun Class<out Any>.getParameterTypePattern(methodName: String): Pattern =
            Pattern.compile(getDeclaredMethod(methodName, String::class.java).getAnnotation(ParameterType::class.java).value)
    }

    abstract inner class `base tests` {
        internal val tardis: Tardis = spy(Tardis())
        internal abstract val tardisSteps: TardisSteps
        @Test
        fun `parses an instant`() {
            assertEquals(Instant.EPOCH, tardisSteps.instant("1970-01-01T00:00:00Z"))
        }

        @Test
        fun `parses a LocalDateTime`() {
            assertEquals(
                LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC),
                tardisSteps.localDateTime("1970-01-01T00:00:00")
            )
        }

        @Test
        fun `parses a ZoneId`() {
            assertEquals(ZoneOffset.UTC, tardisSteps.zoneId("Z"))
            assertEquals(ZoneId.of("UTC"), tardisSteps.zoneId("UTC"))
            assertEquals(ZoneId.of("Asia/Calcutta"), tardisSteps.zoneId("Asia/Calcutta"))
        }

        @ParameterizedTest
        @CsvSource(
            "0.0, 0.0",
            "0.0, 0.0N",
            "0.0, 0.0S",
            "90.0, 90.0",
            "90.0, 90.0N",
            "-90.0, -90.0",
            "-90.0, 90.0S",
        )
        fun `valid latitudes`(expected: Double, text: String) {
            assertEquals(expected, tardisSteps.latitude(text), 0.0)
            assertTrue(latitudePattern.matcher(text).matches())
        }

        @ParameterizedTest
        @ValueSource(strings = ["90.1", "-90.1", "90.1N", "90.1S"])
        fun `invalid latitudes`(text: String) {
            assertThrows<IllegalArgumentException> { tardisSteps.latitude(text) }
            assertTrue(latitudePattern.matcher(text).matches()) // Can be removed
        }

        @ParameterizedTest
        @CsvSource(
            "0.0, 0.0",
            "0.0, 0.0E",
            "0.0, 0.0W",
            "180.0, 180.0",
            "180.0, 180.0E",
            "-180.0, -180.0",
            "-180.0, 180.0W",
        )
        fun `valid longitudes`(expected: Double, text: String) {
            assertEquals(expected, tardisSteps.longitude(text), 0.0)
            assertTrue(longitudePattern.matcher(text).matches())
        }

        @ParameterizedTest
        @ValueSource(strings = ["180.1", "-180.1", "180.1E", "180.1W"])
        fun `invalid longitudes`(text: String) {
            assertThrows<IllegalArgumentException> { tardisSteps.longitude(text) }
            assertTrue(longitudePattern.matcher(text).matches()) // Can be removed
        }

        @ParameterizedTest
        @CsvSource(
            "0.0, 0.0",
            "0.000001, 0.000001",
            "0.1, 0.1",
            "1.0, 1.0",
            "1.1, 1.1",
        )
        fun `valid accuracies`(expected: Double, text: String) {
            assertEquals(text.toDouble(), tardisSteps.accuracy(text), 0.0)
        }

        @ParameterizedTest
        @ValueSource(strings = ["-0.00001"])
        fun `invalid accuracies`(text: String) {
            assertThrows<IllegalArgumentException> { tardisSteps.accuracy(text) }
        }

        @Test
        fun `remembers the Tardis`() {
            assertSame(tardis, tardisSteps.tardis)
        }

        @Test
        fun `setting up the regular clock makes the Tardis run on UTC`() {
            tardisSteps.setupRegularClock()
            assertEquals(ZoneOffset.UTC, tardis.clock.zone)
            assertEquals(tardis.clock, Clock.systemUTC())
        }
    }

    @Nested
    inner class `null WebDriver` : `base tests`() {
        override val tardisSteps = TardisSteps(tardis, null)

        @Test
        fun `uses mock DevTools`() {
            assertTrue(mockingDetails(tardisSteps.devTools).isMock)
        }
    }

    @Nested
    inner class `mock WebDriver` : `base tests`() {
        override val tardisSteps = TardisSteps(tardis, mock(WebDriver::class.java))

        @Test
        fun `uses mock DevTools`() {
            assertTrue(mockingDetails(tardisSteps.devTools).isMock)
        }
    }

    @Nested
    inner class `mock ChromeDriver` : `base tests`() {
        private val mockDevTools = mock(DevTools::class.java)
        private val mockChromeDriver = mock(ChromeDriver::class.java).apply {
            `when`(this.devTools).thenReturn(mockDevTools)
        }
        override val tardisSteps = TardisSteps(tardis, mockChromeDriver)

        @Test
        fun `uses ChromeDriver DevTools`() {
            assertTrue(mockingDetails(tardisSteps.devTools).isMock)
            assertSame(mockDevTools, tardisSteps.devTools)
        }

        @Test
        fun `setting up the regular Clock resets DevTools to defaults`() {
            tardisSteps.setupRegularClock()
            val inOrder = inOrder(mockDevTools)
            inOrder.verify(mockDevTools).createSession()
            inOrder.verify(mockDevTools, times(3)).send(any<Command<Void>>())
            inOrder.verifyNoMoreInteractions()
        }
    }
}
