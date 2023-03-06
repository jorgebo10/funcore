package io.github.jbo.shell.dto

import kotlinx.serialization.Serializable

@Serializable
data class Order(val number: String, val contents: List<OrderItem>)

@Serializable
data class OrderItem(val item: String, val amount: Int, val price: Double)

private const val PRICE_1 = 2.35

private const val PRICE_2 = 1.76

private const val ONE = 1

private const val TWO = 2

val orderStorage = listOf(
    Order(
        "2020-04-03-01",
        listOf(
            OrderItem("Cheeseburger", ONE, PRICE_1),
            OrderItem("Water", TWO, PRICE_1),
            OrderItem("Coke", TWO, PRICE_2),
            OrderItem("Ice Cream", ONE, PRICE_1),
        ),
    ),
)
