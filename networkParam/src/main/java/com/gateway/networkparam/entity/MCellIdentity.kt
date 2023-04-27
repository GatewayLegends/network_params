package com.gateway.networkparam.entity

import android.telephony.CellInfo

abstract class MCellIdentity {

    protected fun Int.orNull() = if (this == CellInfo.UNAVAILABLE) null else this

    protected fun Long.orNull() = if (this == CellInfo.UNAVAILABLE_LONG) null else this
}
