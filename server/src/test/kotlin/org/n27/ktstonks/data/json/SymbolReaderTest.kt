package org.n27.ktstonks.data.json

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SymbolReaderTest {

    private val reader = SymbolReader(JsonReader())

    @Test
    fun `getSymbols should return a list of symbols from test resources`() = runTest {
        val symbols = reader.getSymbols(null)
        assertEquals(5, symbols.size)
        assertTrue(symbols.contains("MSFT"))
        assertTrue(symbols.contains("NESN.SW"))
        assertTrue(symbols.contains("7203.T"))
        assertTrue(symbols.contains("ACS.MC"))
        assertTrue(symbols.contains("TERRF"))
    }

    @Test
    fun `getSymbols with symbol should return a filtered list of symbols from test resources`() = runTest {
        val symbols = reader.getSymbols("M")
        assertEquals(2, symbols.size)
        assertTrue(symbols.contains("MSFT"))
        assertTrue(symbols.contains("ACS.MC"))
    }
}
