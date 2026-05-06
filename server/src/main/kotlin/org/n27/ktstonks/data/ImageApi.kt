package org.n27.ktstonks.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.slf4j.LoggerFactory
import java.util.*

class ImageApi(private val httpClient: HttpClient) {

    private val logger = LoggerFactory.getLogger(ImageApi::class.java)

    suspend fun downloadImage(url: String): String {
        logger.info("downloadImage request triggered")
        val byteArray: ByteArray = httpClient.get(url).body()
        return Base64.getEncoder().encodeToString(byteArray)
    }
}
