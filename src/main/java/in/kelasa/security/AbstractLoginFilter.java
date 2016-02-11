package in.kelasa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.kelasa.model.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by rajeevguru on 13/11/15.
 */
public abstract class AbstractLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenAuthenticationService tokenAuthenticationService;

    public AbstractLoginFilter(RequestMatcher requiresAuthenticationRequestMatcher,TokenAuthenticationService tokenAuthenticationService) {
        super(requiresAuthenticationRequestMatcher);
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {

        // Add the custom token as HTTP header to the response
        String token = tokenAuthenticationService.addAuthentication(response, authentication);

        User user = new User();
        user.setEmail(authentication.getName());
        user.setRoles((List<GrantedAuthority>) authentication.getAuthorities());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(),user);


        // Add the authentication to the Security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
