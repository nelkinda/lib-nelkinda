package com.nelkinda.java.lang

inline fun <reified T> T.binaryResource(name: String): ByteArray =
    T::class.java.getResourceAsStream(name)?.use { it.readBytes() } ?:
    throw AssertionError("Resource not found: ${T::class.java.`package`.name.replace('.', '/')}/$name")
