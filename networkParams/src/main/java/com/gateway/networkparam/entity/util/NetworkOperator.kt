package com.gateway.networkparam.entity.util

sealed class NetworkOperator(val value: String) {
    object Fastlink : NetworkOperator("41866")
    object Korek : NetworkOperator("41840")
    object Asiacell : NetworkOperator("41805")
    object Zain : NetworkOperator("41820")
}
