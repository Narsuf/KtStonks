package org.n27.ktstonks.data.json

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertTrue

class JsonReaderTest {

    private val reader = JsonReader()

    @Test
    fun `getSymbols should return a list of symbols from test resources`() = runTest {
        val result = reader.readJson<List<String>>("/sp.json")
        assertTrue(result.isNotEmpty())
        assertTrue(result.contains("MSFT"))
    }
}
