package com.nelkinda.java.util

interface UUIDGenerator {
    fun <T> generate(): UUID<T>
}

class RandomUUIDGenerator : UUIDGenerator {
    override fun <T> generate(): UUID<T> = UUID.randomUUID()
}
