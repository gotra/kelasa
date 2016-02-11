package in.kelasa.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import in.kelasa.model.User;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GoogleTokenLoginFilter extends AbstractLoginFilter {




    public GoogleTokenLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
                                  AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(urlMapping),tokenAuthenticationService);


        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        Authentication alreadyAuthenticated = SecurityContextHolder.getContext().getAuthentication();

        if (alreadyAuthenticated != null && alreadyAuthenticated.isAuthenticated()) {
            return null;
        }



        final String  idToken = request.getParameter("idtoken");
        final GoogleAuthenticationToken loginToken = new GoogleAuthenticationToken(
                idToken);


        return getAuthenticationManager().authenticate(loginToken);
    }


}