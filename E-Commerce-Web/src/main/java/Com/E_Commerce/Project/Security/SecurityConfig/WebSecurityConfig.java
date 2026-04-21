package Com.E_Commerce.Project.Security.SecurityConfig;

import Com.E_Commerce.Project.Repository.RoleRepository;
import Com.E_Commerce.Project.Repository.UserRepository;
import Com.E_Commerce.Project.Security.Services.UserDeatilsServiceImpl;
import Com.E_Commerce.Project.Security.Services.UserDetailsImpl;
import Com.E_Commerce.Project.Security.jwt.AuthEntryPointJwt;
import Com.E_Commerce.Project.Security.jwt.AuthTokenFilter;
import Com.E_Commerce.Project.model.Role;
import Com.E_Commerce.Project.model.User;
import Com.E_Commerce.Project.model.User_Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

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
                        .requestMatchers("/h2-console/**").permitAll()
                       // .requestMatchers("/api/admin/**").permitAll()
                       // .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()

                        /*        // ROLE BASED ACCESS 👇
                      .requestMatchers("/api/admin/**").hasRole("ADMIN")
                       .requestMatchers("/api/seller/**").hasRole("SELLER")
                        .requestMatchers("/api/user/**").hasRole("USER")
*/
                        .anyRequest().authenticated()
        );

        httpSecurity.authenticationProvider(authenticationProvider());
       httpSecurity.headers(headers -> headers.frameOptions(
               frameOptions -> frameOptions.sameOrigin()));
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

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }


   /*public CommandLineRunner initData(RoleRepository roleRepository,
                                     UserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRole(User_Role.Role_User)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(User_Role.Role_User);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRole(User_Role.Role_Seller)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(User_Role.Role_Seller);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRole(User_Role.Role_Adimn)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(User_Role.Role_Adimn);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUserName("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }*/

}
