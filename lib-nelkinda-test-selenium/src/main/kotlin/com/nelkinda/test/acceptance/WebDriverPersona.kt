package com.nelkinda.test.acceptance

import io.cucumber.java.ParameterType
import jakarta.inject.Inject
import org.openqa.selenium.WebDriver

class WebDriverPersona @Inject constructor(
    private val webDriver: WebDriver,
) {
    @Suppress("UnusedReceiverParameter")
    @ParameterType("I")
    fun String.subject() = webDriver
}
