package com.zv.brokerageservice

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

fun <T> any(): T = Mockito.any<T>()

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> =
    ArgumentCaptor.forClass(T::class.java)

