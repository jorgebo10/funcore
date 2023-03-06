package io.github.jbo

import io.github.jbo.shell.plugins.configureHTTP
import io.github.jbo.shell.plugins.configureMonitoring
import io.github.jbo.shell.plugins.configureRouting
import io.github.jbo.shell.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureHTTP()
    configureRouting()
}
