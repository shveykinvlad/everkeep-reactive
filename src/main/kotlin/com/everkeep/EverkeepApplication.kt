package com.everkeep

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EverkeepApplication

fun main(args: Array<String>) {
    runApplication<EverkeepApplication>(*args)
}
