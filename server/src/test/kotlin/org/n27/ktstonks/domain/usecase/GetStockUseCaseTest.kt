package org.n27.ktstonks.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.test_data.getStock
import kotlin.Result.Companion.success

class GetStockUseCaseTest {

    private lateinit var useCase: GetStockUseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = GetStockUseCase(repository)
    }

    @Test
    fun `getStock should call repository`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock()))

        val actual = useCase("AAPL")

        assertEquals(getStock(), actual.getOrNull())
    }
}
