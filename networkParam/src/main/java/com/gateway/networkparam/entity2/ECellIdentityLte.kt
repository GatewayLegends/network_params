package com.gateway.networkparam.entity2

data class ECellIdentityLte(
    val ci: Int? = null,
    val earfcn: Int? = null,
    val pci: Int? = null,
    val tac: Int? = null,
    val networkOperator: String? = null,
    val additionalPlmns: List<String> = emptyList(),
    val bands: List<Int> = emptyList(),
    val bandwidth: Int? = null,
    val siteId: Int? = null,
    val cellId: Int? = null
)
