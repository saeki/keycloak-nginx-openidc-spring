package com.example.oauth.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class KeycloakRealmRolesGrantedAuthoritiesConverter(private val authorityPrefix: String? = "") :
    Converter<Jwt, Collection<GrantedAuthority>> {

    override fun convert(source: Jwt): Collection<GrantedAuthority> {
        val realmAccess = source.getClaim("realm_access") as? Map<String, Any?> ?: return emptySet()
        val roles = realmAccess["roles"] as? Collection<*> ?: return emptySet()
        return roles
            .filterIsInstance<String>()
            .map { SimpleGrantedAuthority(authorityPrefix + it) }
            .toSet()
    }
}
