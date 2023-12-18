package com.employee.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.employee.task.security.CustomUserDetailService;
import com.employee.task.security.JwtAuthenticationFilter;
import com.employee.task.security.jwtAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	private jwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private CustomUserDetailService customerUserDetailService;
	
	 @Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
		   http
	       .csrf().disable()
	       .authorizeHttpRequests().requestMatchers("/").permitAll()
	       .requestMatchers(HttpMethod.POST).permitAll()
	       .requestMatchers(HttpMethod.GET).permitAll()
	       .anyRequest()
	       .authenticated()
	       .and()
	       .exceptionHandling()
	       .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
	       .and()
	       .sessionManagement()
	       .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			
			http.authenticationProvider(daoAuthenticationProvider());
			DefaultSecurityFilterChain defaultSecurityFilterChain=http.build();
			return defaultSecurityFilterChain;
		}
	   
	   
	   public void configure(AuthenticationManagerBuilder auth)throws Exception{
		   auth.userDetailsService(this.customerUserDetailService).passwordEncoder(passwordEncoder());
	   }
	   
	   @Bean
	    public AuthenticationManager aunthenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
	    	return configuration.getAuthenticationManager();
	    }
	   @Bean
	   public DaoAuthenticationProvider daoAuthenticationProvider() {
		   DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		   provider.setUserDetailsService(this.customerUserDetailService);
		   provider.setPasswordEncoder(passwordEncoder());
		   return provider;
		   
	   }
	   @Bean
	   public PasswordEncoder passwordEncoder() {
		   return new BCryptPasswordEncoder();
	   }
	

}
