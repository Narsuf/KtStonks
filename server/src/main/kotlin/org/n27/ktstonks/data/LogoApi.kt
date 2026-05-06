package org.n27.ktstonks.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.slf4j.LoggerFactory
import java.util.*

class LogoApi(private val httpClient: HttpClient) {

    private val logger = LoggerFactory.getLogger(LogoApi::class.java)

    suspend fun downloadLogo(url: String): String {
        logger.info("downloadLogo request triggered")
        val byteArray: ByteArray = httpClient.get(url).body()
        return Base64.getEncoder().encodeToString(byteArray)
    }
}
