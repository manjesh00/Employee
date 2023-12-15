package com.employee.task.security;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	@Autowired
	private JwtTokenHelper jwtTokenHepler;
	
    @Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestToken=request.getHeader("Authorization");
		String username=null;
		String token=null;
		
		if(requestToken!=null && requestToken.startsWith("Bearer")) {
			token=requestToken.substring(7);
			try {
			username=this.jwtTokenHepler.getUsernameFromToken(token);
			}catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT token");
			}catch (ExpiredJwtException e) {
				System.out.println("JWT token has expired");
			}catch (MalformedJwtException e) {
				System.out.println("Invalid JWT");
			}
		}else {
			System.out.println("Jwt doesnot start with Bearer");
		}
		//validate Token
		
		if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
			if(this.jwtTokenHepler.validateToken(token, userDetails)) {
				//Authentication here
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities()); 
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}else {
				System.out.println("Invalid jwt token");
			}
		}else {
			System.out.println("username is null or context is not null");
		}
		filterChain.doFilter(request, response);
		
	}
		
}

