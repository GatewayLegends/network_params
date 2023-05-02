package com.gateway.networkparam

import android.content.Context
import com.gateway.networkparam.repository.TelephonyDataSource
import com.gateway.networkparam.framework.data.TelephonyDataSourceImp

class NetworkParams(private val context: Context) {

    fun telephonyDataSource(): TelephonyDataSource {
        return TelephonyDataSourceImp(context)
    }
}
