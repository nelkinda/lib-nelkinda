package com.nelkinda.java.util

import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class UUIDGeneratorTest {
    @Test
    fun `generates a random UUID`() {
        val generator = RandomUUIDGenerator()
        assertNotEquals(generator.generate<Any>(), generator.generate<Any>())
    }
}
