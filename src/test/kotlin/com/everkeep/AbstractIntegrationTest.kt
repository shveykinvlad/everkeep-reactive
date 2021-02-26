package com.everkeep

import com.everkeep.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [EverkeepKtApplication::class])
@ContextConfiguration(classes = [TestConfig::class])
abstract class AbstractIntegrationTest {

    @Autowired
    protected lateinit var webClient: WebTestClient

    @LocalServerPort
    private var port: Int = 0

    protected abstract fun getControllerPath(): String

    protected fun getPath(): String = "http://localhost:" + port + getControllerPath()
}
