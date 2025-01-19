package com.nelkinda.test.acceptance

import io.cucumber.java.After
import io.cucumber.java.AfterStep
import io.cucumber.java.Scenario
import jakarta.inject.Inject
import org.openqa.selenium.OutputType.BYTES
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.annotation.Value
import java.nio.file.Files.createDirectories
import java.nio.file.Files.write
import java.nio.file.Path
import java.time.Instant.now

@Suppress("FunctionName")
class ScreenshotFailedTestsHook @Inject constructor (
    private val webDriver: WebDriver,
    @Value("\${cucumber.screenshots.directory:build/reports/tests/screenshots}")
    private val screenshotDirectory: Path,
    @Value("\${cucumber.screenshots.save-each-step:false}")
    private val saveStepScreenshots: Boolean,
) {
    @After
    fun Scenario.`take screenshot if failed`() {
        if (isFailed)
            screenshot(webDriver)
    }

    @AfterStep
    fun Scenario.`take screenshot after each step, if configured`() {
        if (saveStepScreenshots)
            screenshot(webDriver)
    }

    private fun Scenario.screenshot(webDriver: WebDriver) {
        val timestamp = now()
        val scenarioDirectory = screenshotDirectory.resolve(name)
        createDirectories(scenarioDirectory)

        val screenshotAsBinaryPng = (webDriver as TakesScreenshot).getScreenshotAs(BYTES)
        attach(screenshotAsBinaryPng, "image/png", "screenshot")
        write(scenarioDirectory.resolve("${timestamp}.png"), screenshotAsBinaryPng)

        val pageSource = webDriver.pageSource!!
        attach(pageSource, "image/png", "page-source")
        write(scenarioDirectory.resolve("${timestamp}.html"), pageSource.toByteArray())
    }
}
