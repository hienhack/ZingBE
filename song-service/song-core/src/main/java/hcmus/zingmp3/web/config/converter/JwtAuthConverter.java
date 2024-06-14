package hcmus.zingmp3.web.config.converter;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${keycloak.resource-id}")
    private String resourceId;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(jwtGrantedAuthoritiesConverter
                                .convert(jwt)
                                .stream(),
                        extractResourceRoles(jwt)
                                .stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(
                        jwt,
                        authorities,
                        getPrincipleClaimName(jwt));
    }

    private String getPrincipleClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Map<String, Object> realmAccess;
        Collection<String> realmRoles ;
        Collection<String> resourceRoles;
        if (jwt.getClaim("realm_access") == null) {
            return Set.of();
        }

        realmAccess = jwt.getClaim("realm_access");
        realmRoles = (Collection<String>) realmAccess.get("roles");

        if (jwt.getClaim("resource_access") == null) {
            return realmRoles
                    .stream()
                    .map(String::toUpperCase)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        }

        resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess.get(resourceId) == null) {
            return realmRoles
                    .stream()
                    .map(String::toUpperCase)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet());
        }

        resource = (Map<String, Object>) resourceAccess.get(resourceId);

        resourceRoles = (Collection<String>) resource.get("roles");

        return Stream.concat(
                realmRoles
                        .stream()
                        .map(String::toUpperCase)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)),
                resourceRoles
                        .stream()
                        .map(SimpleGrantedAuthority::new)
        ).collect(Collectors.toSet());
    }

}
