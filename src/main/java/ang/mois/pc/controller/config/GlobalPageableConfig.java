package ang.mois.pc.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class GlobalPageableConfig {
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 1000;
    private static final String DEFAULT_SORT_BY = "id";

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePaginationDefaults() {
        return resolver -> {
            resolver.setFallbackPageable(
                    PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_BY).ascending())
            );
        };
    }
}

