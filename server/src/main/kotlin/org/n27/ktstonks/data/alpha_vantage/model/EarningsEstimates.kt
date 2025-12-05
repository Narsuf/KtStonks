package org.n27.ktstonks.data.alpha_vantage.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EarningsEstimates(
    @SerialName("symbol") val symbol: String,
    @SerialName("estimates") val annualEarnings: List<Estimate>,
) {
    @Serializable
    data class Estimate(
        @SerialName("eps_estimate_high") val expectedEpsGrowth: String?
    )
}
