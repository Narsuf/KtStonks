package org.n27.ktstonks.data.alpha_vantage

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.ktstonks.data.alpha_vantage.model.AlphaVantageStock
import org.n27.ktstonks.data.alpha_vantage.model.EarningsEstimates
import org.n27.ktstonks.data.alpha_vantage.model.GlobalQuoteResponse

class AlphaVantageApi(
    private val client: HttpClient,
    private val apiKey: String,
    private val baseUrl: String,
) {

    private suspend inline fun <reified T> fetch(function: String, symbol: String): T {
        val url = "$baseUrl?function=$function&symbol=$symbol&apikey=$apiKey"
        return client.get(url).body()
    }

    suspend fun getStock(symbol: String): AlphaVantageStock = fetch("OVERVIEW", symbol)

    suspend fun getGlobalQuote(symbol: String): GlobalQuoteResponse = fetch("GLOBAL_QUOTE", symbol)

    suspend fun getEpsEstimate(symbol: String): EarningsEstimates = fetch("EARNINGS_ESTIMATES", symbol)
}
