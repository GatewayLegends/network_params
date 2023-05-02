package com.gateway.networkparam.repository

import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.entity.SignalStrengthLte

interface TelephonyDataSource {

    suspend fun getAllCellLte(): List<CellLte>

    fun getAllSignalStrengthLte(): List<SignalStrengthLte>
}
