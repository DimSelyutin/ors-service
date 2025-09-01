package com.innowise.swimdom.filter;

import com.innowise.swimdom.exception.ForbiddenException;
import com.innowise.swimdom.exception.UnauthorizedException;
import com.innowise.swimdom.filter.RoleFilter.Config;
import java.util.List;
import java.util.stream.Stream;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * Query filter. Correlates the role in headers with the role in application.yml
 */
@Component
public class RoleFilter extends AbstractGatewayFilterFactory<Config> {

    public RoleFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String roleFromToken = exchange.getRequest().getHeaders().getFirst("X-User-Roles");
            if (roleFromToken == null) {
                throw new UnauthorizedException("Role not found");
            }

            config.getRoles().lines()
                .flatMap(line -> Stream.of(line.split(";")))
                .filter(role -> role.equals(roleFromToken))
                .findAny()
                .orElseThrow(() -> new ForbiddenException("Access is denied"));

            return chain.filter(exchange);
        };
    }

    /**
     * An auxiliary configuration object that pulls the role from application.
     * yml using the shortcut fieldorder() method.
     */
    @Data
    public static class Config {

        private String roles;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("roles");
    }
}
