package com.everkeep.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain? {
        return httpSecurity
            .csrf().disable()
            .authorizeExchange()
            .anyExchange().permitAll()
            .and()
            .build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
