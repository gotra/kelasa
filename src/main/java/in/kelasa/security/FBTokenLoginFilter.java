package in.kelasa.security;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FBTokenLoginFilter extends AbstractLoginFilter {




    public FBTokenLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService,
                              AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(urlMapping),tokenAuthenticationService);


        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        final String  idToken = request.getParameter("idtoken");
        final FBAuthenticationToken loginToken = new FBAuthenticationToken(
                idToken);


        return getAuthenticationManager().authenticate(loginToken);
    }


}