package org.n27.ktstonks.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.n27.ktstonks.test_data.getStock
import org.n27.ktstonks.test_data.getStocks
import kotlin.Result.Companion.success

class UseCaseTest {

    private lateinit var useCase: UseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = UseCase(repository)
    }

    @Test
    fun `getStock should call repository`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock()))

        val actual = useCase.getStock("AAPL")

        assertEquals(getStock(), actual.getOrNull())
    }

    @Test
    fun `getStocks should call repository`(): Unit = runBlocking {
        `when`(repository.getStocks(anyInt(), anyInt(),anyBoolean(), anyString())).thenReturn(success(getStocks()))

        val actual = useCase.getStocks(0, 1, false, "AAPL")

        assertEquals(getStocks(), actual.getOrNull())
    }

    @Test
    fun `getWatchlist should call repository`(): Unit = runBlocking {
        `when`(repository.getWatchlist(anyInt(), anyInt())).thenReturn(success(getStocks()))

        val actual = useCase.getWatchlist(0, 1)

        assertEquals(getStocks(), actual.getOrNull())
    }

    @Test
    fun `addToWatchlist should call repository`(): Unit = runBlocking {
        val symbol = "AAPL"

        useCase.addToWatchlist(symbol)

        verify(repository).addToWatchlist(symbol)
    }

    @Test
    fun `removeFromWatchlist should call repository`(): Unit = runBlocking {
        val symbol = "AAPL"

        useCase.removeFromWatchlist(symbol)

        verify(repository).removeFromWatchlist(symbol)
    }

    @Test
    fun `addCustomValuation should update stock with new values`(): Unit = runBlocking {
        val stock = getStock()
        val expectedStock = getStock(
            expectedEpsGrowth = 7.72,
            valuationFloor = 12.5,
            currentIntrinsicValue = 93.375,
            forwardIntrinsicValue = 100.58355,
        )
        `when`(repository.getStock(anyString())).thenReturn(success(stock))
        `when`(repository.updateStock(getStock())).thenReturn(success(Unit))

        useCase.addCustomValuation("AAPL", 12.5, 7.72)

        verify(repository).updateStock(expectedStock)
    }

    @Test
    fun `addCustomValuation should update stock with new values when valuation floor is null`(): Unit = runBlocking {
        val stock = getStock()
        val expectedStock = getStock(
            expectedEpsGrowth = 7.72,
            valuationFloor = null,
            forwardIntrinsicValue = 128.74694399999998,
        )
        `when`(repository.getStock(anyString())).thenReturn(success(stock))
        `when`(repository.updateStock(getStock())).thenReturn(success(Unit))

        useCase.addCustomValuation("AAPL", null, 7.72)

        verify(repository).updateStock(expectedStock)
    }
}
