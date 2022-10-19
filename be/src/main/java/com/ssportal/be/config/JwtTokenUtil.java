package com.ssportal.be.config;

import com.ssportal.be.controller.LoginController;
import com.ssportal.be.model.User;
import com.ssportal.be.utilility.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger ( JwtTokenUtil.class );

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getStringFromToken(String token, String attrName) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get(attrName, String.class);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Constants.SIGNING_KEY_BASE64)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public String generateToken(Object principal) {
        if (principal instanceof User) {
            return generateToken((User) principal);
        } else {
            return "Unknown principal:" + principal;
        }
    }

    public String generateToken(User user) {
        return doGenerateToken(user);
    }

    public String generateToken(String subject) {
        return doGenerateToken(subject);
    }

    private String doGenerateToken(User user) {
        user.getUsername ();
        String admin = user.getAdmin ()!=null?user.getAdmin ():"false";
        String hardToken = user.getHardToken () != null? user.getHardToken ():"false";
        String softToken = user.getSoftToken () != null? user.getSoftToken ():"false";
        String otpToken = user.getOtpToken () != null? user.getOtpToken ():"false";
        String desktopToken = user.getDesktopToken () != null? user.getDesktopToken ():"false";
        String SMSToken = user.getSMSToken () != null? user.getSMSToken():"false";
        LOG.error ( "User SMS Token in JWT Class "+ SMSToken );
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        claims.put("scopes", authorities);
        LOG.info ( "Check User details from Saml assertion:" );
        if(user.getEmail () == null) {
            LOG.error ( "User doesn't have email" );
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("Self Service Portal")
                .claim ( "mail", user.getEmail () )
                .claim ( "admin", admin )
                .claim ( "hardToken", hardToken )
                .claim ("otpToken", otpToken )
                .claim ( "softToken", softToken )
                .claim ( "desktopToken", desktopToken)
                .claim ("SMSToken", SMSToken)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY_BASE64)
                .compact();
    }

    private String doGenerateToken(String subject) {

        Claims claims = Jwts.claims().setSubject(subject);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("Self Service Portal")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY_BASE64)
                .compact();
    }

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return (!isTokenExpired(token));
    }

}
