package shinhan.server_common.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/api/**").setViewName("forward:/");
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("http://localhost:5173",
                        "http://ec2-13-124-164-1.ap-northeast-2.compute.amazonaws.com",
                        "http://ec2-43-203-199-109.ap-northeast-2.compute.amazonaws.com")
                    .exposedHeaders("Authorization", "Refresh-Token")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }
        };
    }
}
