package com.zv.brokerageservice.domain

interface Notifier {
    suspend fun sendSimpleMessage(to: String, subject: String, text: String)
}