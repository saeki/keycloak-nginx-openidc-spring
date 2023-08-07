package com.example.web

import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController() {

    @GetMapping("/", produces = ["text/plain"])
    fun index(authentication: Authentication, @AuthenticationPrincipal jwt: Jwt): String? {
        val string = StringBuilder()

        string.append("authentication:\n")
        string.append("  principal: " + authentication.principal + "\n")
        string.append("  credentials: " + authentication.credentials + "\n")
        string.append("  details: " + authentication.details + "\n")
        string.append("  authenticated: " + authentication.isAuthenticated + "\n")
        string.append("  name: " + authentication.name + "\n")
        string.append("  authorities: " + authentication.authorities + "\n")
        string.append("\n")

        string.append("jwt claims:\n")
        jwt.claims.forEach { (k, v) -> string.append("  $k: $v\n") }

        return string.toString()
    }
}
