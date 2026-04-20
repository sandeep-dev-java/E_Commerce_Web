package Com.E_Commerce.Project.Security.SecurityConfig;

import Com.E_Commerce.Project.Security.Services.UserDeatilsServiceImpl;
import Com.E_Commerce.Project.Security.Services.UserDetailsImpl;
import Com.E_Commerce.Project.Security.jwt.AuthEntryPointJwt;
import Com.E_Commerce.Project.Security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDeatilsServiceImpl userDeatilsService;
    @Autowired
    private AuthEntryPointJwt unautorizedHandler;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
         return  authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthTokenFilter authontocationTokenFilter(){
        return  new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
          .exceptionHandling(exception -> exception.authenticationEntryPoint(unautorizedHandler))
           .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
        );

        httpSecurity.authenticationProvider(authenticationProvider());

        httpSecurity.addFilterBefore(authontocationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
     DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
     authenticationProvider.setUserDetailsService(userDeatilsService);
     authenticationProvider.setPasswordEncoder(passwordEncoder());
     return  authenticationProvider;
}

}
