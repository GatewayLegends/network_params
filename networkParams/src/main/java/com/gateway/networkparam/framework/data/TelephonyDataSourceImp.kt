package com.gateway.networkparam.framework.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.entity.SignalStrengthLte
import com.gateway.networkparam.entity.util.NetworkParamsOperator
import com.gateway.networkparam.framework.util.getAllSubTelephonyManagers
import com.gateway.networkparam.framework.util.getCellLte
import com.gateway.networkparam.framework.util.getExecutor
import com.gateway.networkparam.framework.util.getSignalStrengthLte
import com.gateway.networkparam.framework.util.isFineLocationGranted
import com.gateway.networkparam.framework.util.removeCellLteUpdates
import com.gateway.networkparam.framework.util.requestCellLteUpdates
import com.gateway.networkparam.framework.util.telephonyManager
import com.gateway.networkparam.repository.TelephonyDataSource
import com.gateway.networkparam.repository.util.isSim4GSupported


internal class TelephonyDataSourceImp(
    private val context: Context,
) : TelephonyDataSource {

    override var lastCellsLte: List<CellLte> = emptyList()
        private set

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun requestCellLteUpdates(
        networkOperator: NetworkParamsOperator,
        updates: Int,
        updateIntervalMillis: Long,
        onUpdate: (List<CellLte>) -> Unit
    ) {
        allTelephonyManager.forEach { telephonyManager ->

            if (
                (networkOperator.value != telephonyManager.simOperator)
                || telephonyManager.isSim4GSupported.not()
            ) return@forEach

            telephonyManager.requestCellLteUpdates(
                updates = updates,
                executor = context.mainExecutor,
                updateIntervalMillis = updateIntervalMillis,
                onUpdate = {
                    lastCellsLte = it.also(onUpdate)
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun removeCellLteUpdates() = context.telephonyManager().removeCellLteUpdates()

    private val allTelephonyManager: List<TelephonyManager>
        @SuppressLint("MissingPermission")
        get() = if (context.isFineLocationGranted())
            context.getAllSubTelephonyManagers()
        else
            emptyList()

    @SuppressLint("MissingPermission")
    override suspend fun getCellLte(networkOperator: NetworkParamsOperator): List<CellLte> {
        val list = mutableListOf<CellLte>()
        allTelephonyManager.forEach { telephonyManager ->
            if (
                (networkOperator.value != telephonyManager.simOperator)
                || telephonyManager.isSim4GSupported.not()
            ) return@forEach

            telephonyManager.getCellLte(context.getExecutor()).map(list::add)
        }
        return list
    }

    @SuppressLint("MissingPermission")
    override suspend fun getAllCellLte(): List<CellLte> {
        val list = mutableListOf<CellLte>()
        allTelephonyManager.forEach { telephonyManager ->
            telephonyManager.getCellLte(context.getExecutor()).map(list::add)
        }
        return list
    }

    override fun getSignalStrengthLte(networkOperator: NetworkParamsOperator): SignalStrengthLte? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            allTelephonyManager.map { telephonyManager ->
                if (
                    (networkOperator.value != telephonyManager.simOperator)
                    || telephonyManager.isSim4GSupported.not()
                ) return@map

                return telephonyManager.getSignalStrengthLte()
            }

        return null
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
