package org.n27.ktstonks.data.yfinance

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.ktstonks.data.yfinance.model.StockRaw
import java.util.*

class YfinanceApi(private val url: String, private val httpClient: HttpClient) {

    suspend fun getStock(symbol: String): StockRaw {
        val url = "$url/stock/$symbol"
        println("getStock request triggered")
        val response: StockRaw = httpClient.get(url).body()
        return response
    }

    suspend fun getStocks(symbols: String): List<StockRaw> {
        val url = "$url/stocks"
        println("getStocks request triggered")
        val response: List<StockRaw> = httpClient.get(url) {
            parameter("symbols", symbols)
        }.body()

        return response
    }

    suspend fun downloadImage(url: String): String {
        val byteArray: ByteArray = httpClient.get(url).body()
        return Base64.getEncoder().encodeToString(byteArray)
    }
}
