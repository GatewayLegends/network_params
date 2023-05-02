package com.gateway.networkparam.framework.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.entity.SignalStrengthLte
import com.gateway.networkparam.framework.util.getAllSubTelephonyManagers
import com.gateway.networkparam.framework.util.getCellLte
import com.gateway.networkparam.framework.util.getExecutor
import com.gateway.networkparam.framework.util.getSignalStrengthLte
import com.gateway.networkparam.framework.util.isFineLocationGranted
import com.gateway.networkparam.repository.TelephonyDataSource

internal class TelephonyDataSourceImp(
    private val context: Context
) : TelephonyDataSource {

    private val allTelephonyManager: List<TelephonyManager>
        @SuppressLint("MissingPermission")
        get() = if (context.isFineLocationGranted())
            context.getAllSubTelephonyManagers()
        else
            emptyList()

    @SuppressLint("MissingPermission")
    override suspend fun getAllCellLte(): List<CellLte> {
        val list = mutableListOf<CellLte>()
        allTelephonyManager.forEach {
            it.getCellLte(context.getExecutor()).map(list::add)
        }
        return list
    }

    override fun getAllSignalStrengthLte(): List<SignalStrengthLte> {
        val list = mutableListOf<SignalStrengthLte>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            allTelephonyManager.map {
                it.getSignalStrengthLte()?.let(list::add)
            }

        return list
    }
}
