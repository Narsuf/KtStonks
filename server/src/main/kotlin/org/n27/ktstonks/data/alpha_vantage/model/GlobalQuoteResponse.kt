package org.n27.ktstonks.data.alpha_vantage.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GlobalQuoteResponse(
    @SerialName("Global Quote") val globalQuote: GlobalQuote
) {
    @Serializable
    data class GlobalQuote(
        @SerialName("01. symbol") val symbol: String,
        @SerialName("05. price") val price: String
    )
}
