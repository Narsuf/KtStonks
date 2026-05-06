package org.n27.ktstonks.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.n27.ktstonks.domain.Repository

class RemoveFromWatchlistUseCaseTest {

    private lateinit var useCase: RemoveFromWatchlistUseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = RemoveFromWatchlistUseCase(repository)
    }

    @Test
    fun `removeFromWatchlist should call repository`(): Unit = runBlocking {
        useCase("AAPL")

        verify(repository).removeFromWatchlist("AAPL")
    }
}
