package io.github.jbo.core

import arrow.core.continuations.Effect
import arrow.core.continuations.effect

class ReservationRepositoryImpl {

    fun getById(reservationId: ReservationId): Effect<ReservationDomainError, Reservation> = effect {
        when (reservationId.value) {
            "notFoundError" -> {
                shift(ReservationNotFoundDomainError.fromReservationId(reservationId))
            }

            "ok" -> {
                createReservation(reservationId)
            }

            "storeException" -> {
                createReservation(reservationId)
            }

            "getByIdException" -> {
                throw java.lang.IllegalArgumentException("getByIdException")
            }

            "invalidStatusError" -> {
                createReservation(reservationId)
            }

            "notEnoughFundsError" -> {
                createReservation(reservationId)
            }

            else -> {
                throw IllegalArgumentException("Exception")
            }
        }
    }

    fun store(reservation: Reservation): Effect<Unit, Unit> = effect {
        check(reservation.reservationId.value == "storeException")
    }
}

class PaymentApi {
    fun registerPaymentForReservation(
        reservation: Reservation,
    ): Effect<ReservationDomainError, String> = effect {
        if (reservation.reservationId.value == "notEnoughFundsError") {
            shift(NotEnoughFundsDomainError)
        } else {
            "Ok"
        }
    }
}

class PayReservationUseCase(
    private val repository: ReservationRepositoryImpl,
    private val paymentApi: PaymentApi,
) {

    fun invoke(reservationId: ReservationId): Effect<ReservationDomainError, Unit> = effect {
        val reservation = repository.getById(reservationId).bind()

        val paidReservation = payReservation(reservation).bind()

        paymentApi.registerPaymentForReservation(paidReservation).bind()

        repository.store(paidReservation).handleError { shift(NoDomainError) }.bind()
    }
}
