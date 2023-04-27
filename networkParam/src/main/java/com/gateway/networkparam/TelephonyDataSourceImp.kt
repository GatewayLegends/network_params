package com.gateway.networkparam

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.gateway.networkparam.entity2.CellLte
import com.gateway.networkparam.entity2.SignalStrengthLte
import com.gateway.networkparam.util.*

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
    override suspend fun getAllCellLte(): List<List<CellLte>> =
        allTelephonyManager.map {
            it.getCellLte(context.getExecutor())
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
