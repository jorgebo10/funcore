package com.jbo.core

import arrow.core.Either
import arrow.core.left
import arrow.core.right


@JvmInline
value class ReservationId(val value: String) {
    override fun toString(): String {
        return value
    }
}

enum class ReservationStatus {
    BOOKED, CONFIRMED, PAID, CANCELLED, ISSUED
}

data class Reservation(
    var reservationId: ReservationId,
    val status: ReservationStatus,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reservation

        if (reservationId != other.reservationId) return false

        return true
    }

    override fun hashCode(): Int {
        return reservationId.hashCode()
    }

    override fun toString(): String {
        return "Reservation(reservationId=$reservationId)"
    }
}


sealed interface ReservationDomainError

@JvmInline
value class ReservationNotFoundDomainError private constructor(val reservationId: ReservationId) :
    ReservationDomainError {
    companion object {

        fun fromId(id: String) = ReservationNotFoundDomainError(ReservationId(id))

        fun fromReservationId(reservationId: ReservationId) = ReservationNotFoundDomainError(reservationId)
    }
}

object NotEnoughFundsDomainError : ReservationDomainError

object InvalidStatusDomainError : ReservationDomainError

object NoDomainError : ReservationDomainError

fun payReservation(reservation: Reservation): Either<ReservationDomainError, Reservation> =
    if (reservation.reservationId.value == "invalidStatusError") {
        InvalidStatusDomainError.left()
    } else {
        reservation.copy(status = ReservationStatus.PAID).right()
    }

fun createReservation(reservationId: ReservationId) =
    Reservation(
        reservationId,
        ReservationStatus.BOOKED
    )