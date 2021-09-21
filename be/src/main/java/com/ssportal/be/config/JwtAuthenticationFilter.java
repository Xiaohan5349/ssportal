package com.ssportal.be.config;

import com.ssportal.be.model.User;
import com.ssportal.be.model.security.UserRole;
import com.ssportal.be.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import com.ssportal.be.utilility.Constants;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;
    //xiaohan
    //this function check the jwt token in the request, retrieve rhe username from jwt token
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        LOG.debug("Request URI: {} , from: {}", req.getRequestURI(), req.getRemoteAddr());
        String header = req.getHeader(Constants.HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
            authToken = header.replace(Constants.TOKEN_PREFIX, "");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                LOG.error("an error occurred during getting username from token", e);
            } catch (ExpiredJwtException e) {
//                logger.warn("the token is expired and not valid anymore", e);
                LOG.warn("the token is expired and not valid anymore. {}", e.getMessage());
            } catch (SignatureException e) {
                LOG.error("Authentication Failed. Username or Password not valid.");
            }
        } else {
            LOG.info("couldn't find bearer string, will ignore the header of URI: {}", req.getRequestURI());
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findByUsername(username);
            if (user != null ) {
                Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
                for (UserRole role : user.getUserRoles()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole().getName()));
                }

                if (jwtTokenUtil.validateToken(authToken, user)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(req, res);
    }
}
