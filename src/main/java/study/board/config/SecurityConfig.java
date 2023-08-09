package study.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import study.board.common.security.CustomSavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/sign-in"),
                                new AntPathRequestMatcher("/sign-up")).anonymous()
                        .requestMatchers(
                                new AntPathRequestMatcher("/users/**"),
                                new AntPathRequestMatcher("/**/create"),
                                new AntPathRequestMatcher("/**/write"),
                                new AntPathRequestMatcher("/**/edit"),
                                new AntPathRequestMatcher("/**/comment"),
                                new AntPathRequestMatcher("/**/recomment"),
                                new AntPathRequestMatcher("/**/like")).authenticated()
                        .requestMatchers("/**").permitAll())
                .csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter
                                .XFrameOptionsMode.SAMEORIGIN)))
                .formLogin((formLogin) -> formLogin
                        .loginPage("/sign-in")
                        .loginProcessingUrl("/sign-in")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(customSavedRequestAwareAuthenticationSuccessHandler())
                        .failureUrl("/sign-in?error=true"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/sign-out"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler customSavedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new CustomSavedRequestAwareAuthenticationSuccessHandler();
        return successHandler;
    }

}
