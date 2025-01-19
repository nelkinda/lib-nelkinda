package com.nelkinda.test.java.util

import com.nelkinda.java.util.UUID
import com.nelkinda.java.util.UUIDGenerator

class TestUUIDGenerator : UUIDGenerator {
    private var counter = 0L
    override fun <T> generate(): UUID<T> = UUID(0, ++counter)
}
