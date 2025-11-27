package co.unicauca.anteproject.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String userEmail = request.getHeader("X-User-Email");
                    String userRole = request.getHeader("X-User-Role");
                    String isDepartmentHead = request.getHeader("X-User-IsDepartmentHead");

                    if (userEmail != null) {
                        template.header("X-User-Email", userEmail);
                    }
                    if (userRole != null) {
                        template.header("X-User-Role", userRole);
                    }
                    if (isDepartmentHead != null) {
                        template.header("X-User-IsDepartmentHead", isDepartmentHead);
                    }
                }
            }
        };
    }
}
