package org.n27.ktstonks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Stocks(val items: List<Stock>)
