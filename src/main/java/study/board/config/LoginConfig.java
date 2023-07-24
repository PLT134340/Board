package study.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.board.common.argumentresolver.LoginArgumentResolver;
import study.board.common.interceptor.LonginInterceptor;

import java.util.List;

@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LonginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/users/sign-in", "/users/sign-out", "/users/sign-up",
                        "/css/**", "/*.ico", "/error"
                );
    }
}
