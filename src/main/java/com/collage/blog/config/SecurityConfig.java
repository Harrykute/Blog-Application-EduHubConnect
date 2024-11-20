package com.collage.blog.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {
	
	@Autowired
	private UserDetailsService userDetailService;

	@Autowired
	private JWTFilter jwtFilter;
	/*
	 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
	 * throws Exception{ //this will give the security objectchain
	 * 
	 * //disable csrf http.csrf(customizer -> customizer.disable());
	 * 
	 * //from this implementation we can not access the page withou auth bealow Code
	 * http.authorizeHttpRequests(request -> request.anyRequest().authenticated());
	 * 
	 * //it will use defualt form login page
	 * //http.formLogin(Customizer.withDefaults());
	 * 
	 * //for enabling rest client ex. postmanApi
	 * http.httpBasic(Customizer.withDefaults());
	 * 
	 * //without csrf another way to create manage security or session
	 * http.sessionManagement(session->session.sessionCreationPolicy(
	 * SessionCreationPolicy.STATELESS));
	 * 
	 * 
	 * return http.build(); }
	 */
	
	//this are another way without using csrf token both are the defualt one 
	/*
	 * @Bean public UserDetailsService userDeatilsService() {
	 * 
	 * UserDetails user1 = User.withDefaultPasswordEncoder() .username("Harish")
	 * .password("hk@123") .build();
	 * 
	 * return new InMemoryUserDetailsManager(user1);
	 * 
	 * }
	 */
	
	   @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable() // Disable CSRF for testing with Postman
	            .authorizeHttpRequests(authorize -> authorize
	            		.requestMatchers("/api/v1/auth/**")
	            		.permitAll()
	                    .anyRequest().authenticated()
	            )
	            .httpBasic(Customizer.withDefaults()) // Or use formLogin() if needed
                .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
	        return http.build();
	    }
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		//provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); for no becrypt password
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailService);
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
}
