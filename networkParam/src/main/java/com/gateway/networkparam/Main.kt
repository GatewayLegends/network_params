package com.gateway.networkparam

import android.content.Context

class Main(private val context: Context) {

    fun telephonyDataSource(): TelephonyDataSource {
        return TelephonyDataSourceImp(context)
    }
}
