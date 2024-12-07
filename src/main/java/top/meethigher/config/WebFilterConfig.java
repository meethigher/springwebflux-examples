package top.meethigher.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class WebFilterConfig {


    @Bean
    @Order(0)
    @ConditionalOnProperty(name = "allowAllCors.learnFilter", havingValue = "true")
    public WebFilter aFilter() {
        /**
         * 在servlet中。请求的扭转是 aFilter-->bFilter-->servlet-->bFilter-->aFilter
         * 在webflux中同理。Filter对应WebFilter，Servlet对应WebHandler
         */
        return (exchange, chain) -> {
            log.info("aFilter start");
            return chain.filter(exchange).doOnSuccess(t -> log.info("aFilter end"));
        };
    }

    @Bean
    @Order(1)
    @ConditionalOnProperty(name = "allowAllCors.learnFilter", havingValue = "true")
    public WebFilter bFilter() {
        return (exchange, chain) -> {
            log.info("bFilter start");
            return chain.filter(exchange).doOnSuccess(t -> log.info("bFilter end"));
        };
    }

    /**
     * 内置的跨域逻辑
     */
    @Bean
    @Order(Integer.MIN_VALUE + 1)
    @ConditionalOnProperty(name = "allowAllCors.builtin", havingValue = "true")
    public WebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        CorsWebFilter corsWebFilter = new CorsWebFilter(source);
        log.info("allowAllCors.builtin is set to true");
        return corsWebFilter;
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    @ConditionalOnProperty(name = "allowAllCors.personal", havingValue = "true")
    public WebFilter personalCorsFilter(WebSocketHandlerAdapter webFluxWebSocketHandlerAdapter) {
        WebFilter webFilter = (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            //用*会导致范围过大，浏览器出于安全考虑，在allowCredentials为true时会不认*这个操作，因此可以使用如下代码，间接实现允许跨域
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeaders().getFirst("origin"));
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
            //允许跨域发送cookie
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            if ("OPTIONS".equalsIgnoreCase(request.getMethod().name())) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            } else {
                return chain.filter(exchange);
            }

        };

        log.info("allowAllCors.personal is set to true");
        return webFilter;

    }
}
