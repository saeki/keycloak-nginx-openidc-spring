package com.example.oauth.converters

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

class KeycloakJwtAuthenticationConverter : JwtAuthenticationConverter() {
    init {
        setJwtGrantedAuthoritiesConverter(KeycloakRealmRolesGrantedAuthoritiesConverter("ROLE_"))
        setPrincipalClaimName("preferred_username")
    }
}
