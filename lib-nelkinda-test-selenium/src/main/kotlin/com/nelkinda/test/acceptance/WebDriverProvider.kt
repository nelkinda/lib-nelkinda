package com.nelkinda.test.acceptance

import jakarta.inject.Inject
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration

@Component
class WebDriverProvider @Inject constructor(
    @Value("\${cucumber.downloads.directory:build/reports/tests/Downloads}") private val downloadsDirectory: String,
) {
    @Primary
    @Bean
    fun createWebDriver(): WebDriver = createDriver()

    private fun createDriver(): WebDriver =
        instantiateDriver().apply {
            // manage().window().size = Dimension(375, 667 + (667 - 553)) // iPhone SE according to Chrome
            manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10))
        }

    private fun instantiateDriver(): WebDriver =
        if (useFirefox()) FirefoxDriver(FirefoxOptions().apply {
            addArguments("--headless")
            addArguments("--no-sandbox")
            addArguments("--disable-extensions")
        })
        else ChromeDriver(ChromeOptions().apply {
            addArguments("--headless=new")
            setExperimentalOption("prefs", mapOf(
                "profile.default_content_setting_values.notifications" to 1,
                "download.default_directory" to Paths.get(downloadsDirectory).toAbsolutePath().toString(),
                "download.prompt_for_download" to false,
                "download.directory_upgrade" to true,
            ))
        })

    companion object {
        private fun useFirefox(): Boolean = System.getenv("USE_FIREFOX_DRIVER") != null || Files.exists(Paths.get("/proc/device-tree/model"))
    }
}

val WebDriver.wait get() = WebDriverWait(this, Duration.ofMillis(500), Duration.ofMillis(5))
