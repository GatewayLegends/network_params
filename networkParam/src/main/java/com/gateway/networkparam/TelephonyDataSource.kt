package com.gateway.networkparam

import com.gateway.networkparam.entity2.CellLte
import com.gateway.networkparam.entity2.SignalStrengthLte

interface TelephonyDataSource {

    suspend fun getAllCellLte(): List<List<CellLte>>

    fun getAllSignalStrengthLte(): List<SignalStrengthLte>
}
