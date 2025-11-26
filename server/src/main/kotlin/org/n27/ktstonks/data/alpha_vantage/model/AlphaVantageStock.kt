package org.n27.ktstonks.data.alpha_vantage.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlphaVantageStock(
    @SerialName("Symbol") val symbol: String,
    @SerialName("Name") val name: String,
    @SerialName("OfficialSite") val officialSite: String?,
    @SerialName("DividendYield") val dividendYield: String?,
    @SerialName("EPS") val eps: String?,
    @SerialName("QuarterlyEarningsGrowthYOY") val earningsGrowthYOY: String?,
    @SerialName("Currency") val currency: String?,
)
