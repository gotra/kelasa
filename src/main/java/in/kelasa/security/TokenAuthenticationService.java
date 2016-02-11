package in.kelasa.security;

/**
 * Created by rajeevguru on 06/11/15.
 */


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenAuthenticationService {

    private static final String AUTH_COOKIE_NAME = "X-AUTH-TOKEN";
    private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

    private final String secret;

    @Autowired
    public TokenAuthenticationService(@Value("${token.secret}") String secret) {
        this.secret = secret;
    }

    public String addAuthentication(HttpServletResponse response, Authentication authentication) {

        Calendar cal = Calendar.getInstance();
        String token = Jwts.builder().setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities()
                        .stream().map(o -> o.getAuthority()).toArray()

                ).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secret).compact();

        Cookie cookie = new Cookie(AUTH_COOKIE_NAME,token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return token;


    }

    public Authentication getAuthentication(HttpServletRequest request) {
        Cookie[] requestCookies = request.getCookies();
        if (requestCookies != null && requestCookies.length > 0) {

            for (Cookie cookie : requestCookies) {

                if (cookie.getName().equals(AUTH_COOKIE_NAME) ){
                    try {

                        final Claims claims = Jwts.parser().setSigningKey(secret)
                                .parseClaimsJws(cookie.getValue()).getBody();
                        ArrayList<String> allroles = (ArrayList<String>) claims.get("roles");



                        // Please pay attention this is a trick, we need to ADD ROLE_ to all the granted authorities as
                        // this is used by spring security
                        List<GrantedAuthority> grantedAuthorities =allroles.stream().map( o ->
                                new SimpleGrantedAuthority("ROLE_" + o)).collect(Collectors.toList());


                        Authentication auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", grantedAuthorities);

                        return auth;


                    }
                   catch (Exception e) {
                       // do nothing, then user does not have any details over why his token was rejected
                   }




                }

            }

        }

        return null;


    }
}