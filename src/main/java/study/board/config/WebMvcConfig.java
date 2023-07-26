package study.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.board.common.argumentresolver.LoginArgumentResolver;
import study.board.common.interceptor.LonginInterceptor;
import study.board.common.interceptor.PostEditInterceptor;
import study.board.common.interceptor.UserEditInterceptor;
import study.board.service.PostService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final PostService postService;

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

        registry.addInterceptor(new UserEditInterceptor())
                .order(2)
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users", "/users/sign-in", "/users/sign-out", "/users/sign-up");

        registry.addInterceptor(new PostEditInterceptor(postService))
                .order(3)
                .addPathPatterns("/boards/**/edit");
    }
}
