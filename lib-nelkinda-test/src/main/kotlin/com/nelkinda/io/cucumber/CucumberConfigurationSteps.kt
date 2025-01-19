package com.nelkinda.io.cucumber

import io.cucumber.java.Before
import io.cucumber.java.ParameterType
import io.cucumber.java.Scenario
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertTrue

@Suppress("FunctionName")
class CucumberConfigurationSteps {
    internal lateinit var sourceTagNames: Collection<String>
    internal lateinit var rememberedTag: String

    @Before
    fun `remember scenario`(scenario: Scenario) {
        sourceTagNames = scenario.sourceTagNames
    }

    @ParameterType("@[a-zA-Z0-9_]+")
    fun tag(tag: String) = tag

    @Given("a scenario tagged {tag}")
    fun `a scenario tagged`(tag: String) {
        rememberedTag = tag
        assertTrue(sourceTagNames.contains(tag))
    }

    @Suppress("EmptyFunctionBlock")
    @When("running the tests")
    fun `running the tests`() {
    }

    @Then("the scenario MUST NOT be executed")
    fun `the scenario MUST NOT be executed`() {
        // Not using Assertions.fail() here because of Jacoco coverage.
        throw AssertionError(
            if (this::rememberedTag.isInitialized)
                "A scenario tagged $rememberedTag MUST NOT be executed"
            else
                "The scenario MUST NOT be executed"
        )
    }
}
