package com.nelkinda.java.lang

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ClassTest {
    @Test
    fun `binaryResource reads the content of a binary resource`() {
        val bytes = binaryResource("test-file.txt")
        assertEquals("Hello, world!\n", String(bytes))
    }

    @Test
    fun `throws NPE if the resource is not found`() {
        val exception = assertThrows<AssertionError> {
            binaryResource("non-existent-file.txt")
        }
        assertEquals("Resource not found: com/nelkinda/java/lang/non-existent-file.txt", exception.message)
    }
}
