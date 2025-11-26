package org.n27.ktstonks.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Stock(
    val symbol: String,
    val companyName: String,
    val logoUrl: String?,
    val price: Double?,
    val dividendYield: Double?,
    val eps: Double?,
    val pe: Double?,
    val earningsQuarterlyGrowth: Double?,
    val expectedEpsGrowth: Double?,
    val currentIntrinsicValue: Double?,
    val forwardIntrinsicValue: Double?,
    val currency: String?,
    val lastUpdated: Long,
    val isWatchlisted: Boolean = false,
)
