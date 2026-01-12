package org.n27.ktstonks.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.test_data.data.getStockEntity
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
        val symbol = "AAPL"
        val stock = getStock()
        val expectedStock = getStock(
            expectedEpsGrowth = 15.0,
            forwardIntrinsicValue = 18.4,
            valuationFloor = 16.0,
            currentIntrinsicValue = 16.0
        )
        `when`(repository.getStock(symbol)).thenReturn(success(stock))
        `when`(repository.updateStock(any())).thenReturn(success(Unit))

        val result = useCase.addCustomValuation(symbol, 12.5, 7.72)

        assertEquals(success(Unit), result)
        verify(repository).updateStock(expectedStock)
    }

    @Test
    fun `addCustomValuation should update stock with new values when valuation floor is null`(): Unit = runBlocking {
        val symbol = "AAPL"
        val stock = getStock()
        val expectedStock = getStock(
            expectedEpsGrowth = 15.0,
            forwardIntrinsicValue = 18.4,
        )

        `when`(repository.getStock(symbol)).thenReturn(Result.success(stock))
        `when`(repository.updateStock(any())).thenReturn(Result.success(Unit))

        val result = useCase.addCustomValuation(symbol, null, 15.0)

        assertEquals(Result.success(Unit), result)
        verify(repository).updateStock(expectedStock)
    }
}
