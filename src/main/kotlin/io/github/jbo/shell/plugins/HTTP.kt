package io.github.jbo.shell.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.deflate
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.compression.minimumSize
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.routing.routing

private const val MIN_SIZE = 1024
private const val DEFLATE_PRIORITY = 10.0

private const val GZIP_PRIORITY = 1.0

fun Application.configureHTTP() {
    routing {
        openAPI(path = "openapi")
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(Compression) {
        gzip {
            priority = GZIP_PRIORITY
        }
        deflate {
            priority = DEFLATE_PRIORITY
            minimumSize(MIN_SIZE.toLong()) // condition
        }
    }
}
