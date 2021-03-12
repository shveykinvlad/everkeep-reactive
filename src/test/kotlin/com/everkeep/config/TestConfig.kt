package com.everkeep.config

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.util.GreenMail
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class TestConfig {

    @Bean
    fun mongoContainer(): MongoDBContainer {
        val image = DockerImageName
            .parse(MONGO_IMAGE)
            .asCompatibleSubstituteFor("mongo")

        return MongoDBContainer(image)
            .withCommand(BIND_IP)
            .apply { start() }
    }

    @Bean
    fun mongoClient(mongoContainer: MongoDBContainer): MongoClient =
        MongoClients.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(mongoContainer.replicaSetUrl))
                .build()
        )

    @Bean
    fun greenMail(): GreenMail =
        GreenMail()
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("username", "password"))
            .apply { start() }

    companion object {
        const val MONGO_IMAGE: String = "mongo:4.0.10"
        const val BIND_IP: String = "--bind_ip_all"
    }
}
