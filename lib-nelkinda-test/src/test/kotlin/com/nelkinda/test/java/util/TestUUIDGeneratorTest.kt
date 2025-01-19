package com.nelkinda.test.java.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestUUIDGeneratorTest {
    @Test
    fun `two calls produce consecutive pseudo-UUIDs`() {
        val generator = TestUUIDGenerator()
        val uuid1 = generator.generate<Any>()
        val uuid2 = generator.generate<Any>()
        assertEquals("00000000-0000-0000-0000-000000000001", uuid1.toString())
        assertEquals("00000000-0000-0000-0000-000000000002", uuid2.toString())
    }
}
