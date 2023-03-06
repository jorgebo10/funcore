@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.jbo

import io.github.jbo.core.InvalidStatusDomainError
import io.github.jbo.core.NoDomainError
import io.github.jbo.core.NotEnoughFundsDomainError
import io.github.jbo.core.PayReservationUseCase
import io.github.jbo.core.PaymentApi
import io.github.jbo.core.ReservationId
import io.github.jbo.core.ReservationNotFoundDomainError
import io.github.jbo.core.ReservationRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

private const val NOT_ENOUGH_FUNDS =
    "Problem details status=400 title=reservations/400 detail=notEnoughFunds"

private const val INVALID_STATUS =
    "Problem details status=400 title=reservations/400 detail=invalidStatus"

internal class EffectTest {

    @Test
    fun router() = runTest {
        /*
           This creates a description of a program that ONLY when executed will trigger a side effect.
           e.g.: an exception or access to a database
           So in a functional architecture it is part of the functional core, however the point where it is
           executed belongs to the imperative shell where exceptions will be handled
           How to use: iterate over these reservationIds for testing different cases
           storeException notFoundError ok storeException getByIdException invalidStatusError notEnoughFundsError
            invalidStatusError
        */

        val description =
            PayReservationUseCase(ReservationRepositoryImpl(), PaymentApi())
                .invoke(ReservationId("storeException"))
        println(description) // this prints the effect description object, but does not execute anything

        PayReservationUseCase(ReservationRepositoryImpl(), PaymentApi())
            .invoke(ReservationId("storeException"))
            .fold(
                { throw IllegalArgumentException("Problem details status=500 title=reservations/500", it) },
                {
                    when (it) {
                        is ReservationNotFoundDomainError ->
                            throw IllegalArgumentException("Problem details status=404 title=reservations/404")

                        NoDomainError -> TODO("this might never happen")
                        InvalidStatusDomainError -> throw IllegalArgumentException(INVALID_STATUS)
                        NotEnoughFundsDomainError -> throw IllegalArgumentException(NOT_ENOUGH_FUNDS)
                    }
                },
                { println("reservation updated") },
            )
    }
}
