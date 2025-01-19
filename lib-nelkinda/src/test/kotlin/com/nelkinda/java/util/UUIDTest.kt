package com.nelkinda.java.util

import com.nelkinda.java.util.UUID.Companion.fromString
import com.nelkinda.java.util.UUID.Companion.randomUUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class UUIDTest {
    @Test
    fun `construct from longs`() {
        val mostSignificantBits = Random.nextLong()
        val leastSignificantBits = Random.nextLong()
        val uuid = UUID<Any>(mostSignificantBits, leastSignificantBits)
        assertEquals(mostSignificantBits, uuid.value.mostSignificantBits)
        assertEquals(leastSignificantBits, uuid.value.leastSignificantBits)
    }

    @Test
    fun `test toString()`() {
        val uuid = fromString<Any>("12345678-9ABC-DEF0-1234-56789ABCDEF0")
        assertEquals("12345678-9abc-def0-1234-56789abcdef0", uuid.toString())
    }

    @Test
    fun `test randomUUID()`() {
        val uuid1 = randomUUID<Any>()
        val uuid2 = randomUUID<Any>()
        assertNotEquals(uuid1, uuid2)
        assertEquals(2, uuid1.value.variant())
        assertEquals(4, uuid1.value.version())
    }
}
