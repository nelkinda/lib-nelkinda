package com.nelkinda.io.cucumber

import io.cucumber.java.Scenario
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

class CucumberConfigurationStepsTest {

    private val steps = CucumberConfigurationSteps()

    @Test
    fun `remember scenario sets sourceTagNames`() {
        val mockScenario = mock(Scenario::class.java)
        val tags = setOf("@tag1", "@tag2")
        `when`(mockScenario.sourceTagNames).thenReturn(tags)

        steps.`remember scenario`(mockScenario)

        assertEquals(tags, steps.sourceTagNames)
    }

    @Test
    fun `tag ParameterType correctly parses a tag`() {
        val result = steps.tag("@example_tag")
        assertEquals("@example_tag", result)
    }

    @Test
    fun `a scenario tagged sets rememberedTag and asserts tag is present`() {
        val mockScenario = mock(Scenario::class.java)
        val tags = setOf("@tag1", "@tag2")
        `when`(mockScenario.sourceTagNames).thenReturn(tags)
        steps.`remember scenario`(mockScenario)

        assertDoesNotThrow {
            steps.`a scenario tagged`("@tag1")
        }
        assertEquals("@tag1", steps.rememberedTag)
    }

    @Test
    fun `a scenario tagged throws assertion error if tag is absent`() {
        val mockScenario = mock(Scenario::class.java)
        val tags = setOf("@tag1", "@tag2")
        `when`(mockScenario.sourceTagNames).thenReturn(tags)
        steps.`remember scenario`(mockScenario)

        val exception = assertThrows<AssertionError> {
            steps.`a scenario tagged`("@non_existent_tag")
        }
        assertEquals("expected: <true> but was: <false>", exception.message)
    }

    @Test
    fun `running the tests does nothing`() {
        assertDoesNotThrow {
            steps.`running the tests`()
        }
    }

    @Test
    fun `the scenario MUST NOT be executed throws AssertionError with rememberedTag`() {
        steps.rememberedTag = "@example_tag"

        val exception = assertThrows<AssertionError> {
            steps.`the scenario MUST NOT be executed`()
        }
        assertEquals("A scenario tagged @example_tag MUST NOT be executed", exception.message)
    }

    @Test
    fun `the scenario MUST NOT be executed throws AssertionError without rememberedTag`() {
        val exception = assertThrows<AssertionError> {
            steps.`the scenario MUST NOT be executed`()
        }
        assertEquals("The scenario MUST NOT be executed", exception.message)
    }
}
