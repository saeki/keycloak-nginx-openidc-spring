package com.example.config

import com.example.oauth.converters.KeycloakJwtAuthenticationConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    lateinit var jwkSetUri: String

    @Bean
    fun securityFilterChains(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors { cors ->
                cors.disable()
            }
            .csrf { csrf ->
                csrf.disable()
            }
            .formLogin { formLogin ->
                formLogin.disable()
            }
            .httpBasic { httpBasic ->
                httpBasic.disable()
            }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2ResourceServer ->
                oauth2ResourceServer.jwt { jwtConfigurer ->
                    jwtConfigurer.jwkSetUri(this.jwkSetUri)
                    jwtConfigurer.jwtAuthenticationConverter(KeycloakJwtAuthenticationConverter())
                }
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }
}
