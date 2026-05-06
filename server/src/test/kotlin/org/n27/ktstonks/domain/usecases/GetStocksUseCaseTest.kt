package org.n27.ktstonks.domain.usecases

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.test_data.getStocks
import kotlin.Result.Companion.success

class GetStocksUseCaseTest {

    private lateinit var useCase: GetStocksUseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = GetStocksUseCase(repository)
    }

    @Test
    fun `getStocks should call repository`(): Unit = runBlocking {
        `when`(repository.getStocks(anyInt(), anyInt(), anyBoolean(), anyString())).thenReturn(success(getStocks()))

        val actual = useCase(0, 1, false, "AAPL")

        assertEquals(getStocks(), actual.getOrNull())
    }
}
