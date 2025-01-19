package com.nelkinda.java.util

/** Super type of type-safe identifiers.
 * The typical use case would be value classes that wrap existing types to provide identifiers.
 *
 * @param TargetType The type of the entity that this {@code Id} is for.
 */
interface Id<TargetType>
