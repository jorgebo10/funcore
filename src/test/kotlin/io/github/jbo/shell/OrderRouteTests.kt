package io.github.jbo.shell

import io.github.jbo.shell.plugins.configureRouting
import io.github.jbo.shell.plugins.configureSerialization
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderRouteTests {
    @Test
    fun testGetOrder() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }
        val response = client.get("/order/2020-04-03-01")
        assertEquals(
            """
                {
                  "number" : "2020-04-03-01",
                  "contents" : [ {
                    "item" : "Cheeseburger",
                    "amount" : 1,
                    "price" : 2.35
                  }, {
                    "item" : "Water",
                    "amount" : 2,
                    "price" : 2.35
                  }, {
                    "item" : "Coke",
                    "amount" : 2,
                    "price" : 1.76
                  }, {
                    "item" : "Ice Cream",
                    "amount" : 1,
                    "price" : 2.35
                  } ]
                }
            """.trimIndent(),
            response.bodyAsText(),
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
