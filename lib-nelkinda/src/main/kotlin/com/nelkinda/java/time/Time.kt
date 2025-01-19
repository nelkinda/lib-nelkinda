package com.nelkinda.java.time

import java.time.Duration
import java.time.LocalTime

fun Duration.toLocalTime(): LocalTime = LocalTime.MIDNIGHT.plus(this)
fun LocalTime.toDuration(): Duration = Duration.between(LocalTime.MIDNIGHT, this)
