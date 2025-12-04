package org.n27.ktstonks.data.alpha_vantage

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.ktstonks.data.alpha_vantage.model.AlphaVantageStock
import org.n27.ktstonks.data.alpha_vantage.model.EarningsEstimates
import org.n27.ktstonks.data.alpha_vantage.model.GlobalQuoteResponse
import org.n27.ktstonks.data.db.api_usage.ApiUsageDao
import org.n27.ktstonks.domain.exceptions.ApiLimitExceededException
import java.time.LocalDate

private const val API_LIMIT = 25

class AlphaVantageApi(
    private val client: HttpClient,
    private val apiKey: String,
    private val baseUrl: String,
    private val apiUsageDao: ApiUsageDao
) {

    private suspend inline fun <reified T> fetch(function: String, symbol: String): T {
        val today = LocalDate.now()
        val usage = apiUsageDao.getUsage(today)

        if (usage >= API_LIMIT) throw ApiLimitExceededException("API limit of $API_LIMIT reached for today.")

        apiUsageDao.incrementUsage(today)

        val url = "$baseUrl?function=$function&symbol=$symbol&apikey=$apiKey"
        return client.get(url).body()
    }

    suspend fun getStock(symbol: String): AlphaVantageStock = fetch("OVERVIEW", symbol)

    suspend fun getGlobalQuote(symbol: String): GlobalQuoteResponse = fetch("GLOBAL_QUOTE", symbol)

    suspend fun getEpsEstimate(symbol: String): EarningsEstimates = fetch("EARNINGS_ESTIMATES", symbol)
}
