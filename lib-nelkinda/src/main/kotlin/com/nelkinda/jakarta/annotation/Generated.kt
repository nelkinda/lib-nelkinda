package com.nelkinda.jakarta.annotation

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.TYPEALIAS

@Retention(RUNTIME)
@Target(CLASS, FUNCTION, PROPERTY, CONSTRUCTOR, ANNOTATION_CLASS, PROPERTY_GETTER, PROPERTY_SETTER, TYPEALIAS)
/** The {@code Generated} annotation is used to mark source code that has been generated.
 * It serves as a replacement or addition to the {@link jakarta.annotation.Generated} annotation.
 * Unlike the original {@link jakarta.annotation.Generated} annotation, this annotation has runtime retention.
 * This is required to support tools like Jacoco that rely on runtime retention.
 * @see jakarta.annotation.Generated
 */
annotation class Generated
