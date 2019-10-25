package nl.codestar.interchange

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v0/route").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
