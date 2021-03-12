package com.everkeep.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Configuration
class MongoConfig {

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions =
        MongoCustomConversions(
            listOf(
                OffsetDateTimeReadConverter,
                OffsetDateTimeWriteConverter
            )
        )

    object OffsetDateTimeWriteConverter : Converter<OffsetDateTime, String> {
        override fun convert(source: OffsetDateTime): String =
            source.toInstant().atZone(ZoneOffset.UTC).toString()
    }

    object OffsetDateTimeReadConverter : Converter<String, OffsetDateTime> {
        override fun convert(source: String): OffsetDateTime =
            OffsetDateTime.parse(source)
    }
}
