package org.n27.ktstonks.data.alpha_vantage

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.n27.ktstonks.data.alpha_vantage.model.AlphaVantageStock
import org.n27.ktstonks.data.alpha_vantage.model.EarningsEstimates
import org.n27.ktstonks.data.alpha_vantage.model.GlobalQuoteResponse

class AlphaVantageApi(private val client: HttpClient, private val apiKey: String) {

    private suspend inline fun <reified T> fetch(function: String, symbol: String): T {
        val url = "$BASE_URL?function=$function&symbol=$symbol&apikey=$apiKey"
        return client.get(url).body()
    }

    suspend fun getStock(symbol: String): AlphaVantageStock = fetch("OVERVIEW", symbol)

    suspend fun getGlobalQuote(symbol: String): GlobalQuoteResponse = fetch("GLOBAL_QUOTE", symbol)

    suspend fun getEpsEstimate(symbol: String): EarningsEstimates = fetch("EARNINGS_ESTIMATES", symbol)

    companion object {
        private const val BASE_URL = "https://www.alphavantage.co/query"

        fun create(apiKey: String): AlphaVantageApi {
            val client = HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }
            return AlphaVantageApi(client, apiKey)
        }
    }
}
