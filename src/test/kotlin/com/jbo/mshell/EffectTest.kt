@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jbo.mshell

import arrow.core.*
import com.jbo.core.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class EffectTest {

    @Test
    fun router() = runTest {
        /*
            This creates a description of a program that ONLY when executed will trigger a side effect.
            e.g.: an exception or access to a database
            So in a functional architecture it is part of the functional core, however the point where it is
            executed belongs to the imperative shell where exceptions will be handled
            How to use: iterate over these reservationIds for testing different cases
            storeException notFoundError ok storeException getByIdException invalidStatusError notEnoughFundsError invalidStatusError
         */

        val description = PayReservationUseCase(ReservationRepositoryImpl(), PaymentApi())
            .invoke(ReservationId("storeException"))
        println(description) //this prints the effect description object, but does not execute anything

        PayReservationUseCase(ReservationRepositoryImpl(), PaymentApi())
            .invoke(ReservationId("storeException"))
            .fold(
                {
                    throw IllegalArgumentException("Problem details status=500 title=reservations/500", it)
                }, {
                    when (it) {
                        is ReservationNotFoundDomainError -> throw IllegalArgumentException("Problem details status=404 title=reservations/404")
                        NoDomainError -> TODO("this might never happen")
                        InvalidStatusDomainError -> throw IllegalArgumentException("Problem details status=400 title=reservations/400 detail=invalidStatus")
                        NotEnoughFundsDomainError -> throw IllegalArgumentException("Problem details status=400 title=reservations/400 detail=notEnoughFunds")
                    }
                },
                {
                    println("reservation updated")
                })
    }
}
