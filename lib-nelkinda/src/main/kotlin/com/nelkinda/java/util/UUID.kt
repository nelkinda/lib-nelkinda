package com.nelkinda.java.util

import com.nelkinda.jakarta.annotation.Generated

/** This UUID type is the same as {@link java.util.UUID} but parameterized to provide type safety.
 * Note that value classes are name-mangled by the Kotlin compiler.
 * At present, there is no way to suppress name-mangling, and Hibernate can't deal with it in all places.
 * Hibernate can deal with it at the entity field level.
 * But Hibernate can't deal with it at the level of the {@link JPARepository} type parameter.
 *
 * The correct way to use this class is to use it at the entity field level and everywhere except at the JPARepository.
 *
 * @param TargetType The type of the entity that this UUID is for.
 */
@Suppress("unused") // Used for type-checking
@Generated
@JvmInline value class UUID<TargetType>(val value: java.util.UUID): Id<TargetType> {
    constructor(mostSigBits: Long, leastSigBits: Long): this(java.util.UUID(mostSigBits, leastSigBits))

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        /** See {@link java.util.UUID#fromString(String)}. */
        @JvmStatic fun <TargetType> fromString(name: String) = UUID<TargetType>(java.util.UUID.fromString(name))

        /** See {@link java.util.UUID#randomUUID()}. */
        @JvmStatic fun <TargetType> randomUUID() = UUID<TargetType>(java.util.UUID.randomUUID())
    }
}
