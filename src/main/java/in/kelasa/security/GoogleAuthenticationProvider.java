package in.kelasa.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import in.kelasa.model.User;
import in.kelasa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rajeevguru on 13/11/15.
 */
@Service
public class GoogleAuthenticationProvider implements AuthenticationProvider {

    private static String CLIENT_ID = "716551550308-ms5akcfccln6c6k4n2esk7bf2binkge9.apps.googleusercontent.com";
    private static String APPS_DOMAIN_NAME = "Kelasa Web Client";
    private static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private HttpTransport httpTransport;
    private GoogleIdTokenVerifier verifier;
    @Autowired
    private UserService userService;


    @PostConstruct
    public void init() {

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            //post construct cannot throw exception so throwing a runtime exception
            throw new IllegalStateException(e);
        }

        // Build the verifier for later use, we do it only once and reuse this object
        verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Arrays.asList(CLIENT_ID))
                .build();


    }

    private User process(String idTokenString) {


        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            e.printStackTrace();
            //todo log in error log
        }
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

                User user = userService.getUserByEmail(payload.getEmail());
                if (user == null ) {
                    // We need to update the user
                    user = new User();
                    user.setEmail(payload.getEmail());
                    GrantedAuthority authority = new SimpleGrantedAuthority("normal");
                    List authorities = new ArrayList<GrantedAuthority>();
                    authorities.add(authority);
                    user.setRoles(authorities);
                    user.setGoogleAccount(true);
                    user.setFirstName((String) payload.get("given_name"));
                    user.setLastName((String) payload.get("family_name"));
                    userService.create(user);
                 }
                    else {
                    user.setGoogleAccount(true);
                    user.setFirstName((String) payload.get("given_name"));
                    user.setLastName((String) payload.get("family_name"));
                    userService.update(user);
                }
                return user;

        }

            return null;

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        GoogleAuthenticationToken gtoken = (GoogleAuthenticationToken) authentication;
        User user = process(gtoken.getToken());
        if (user != null) {
            Authentication auth = new GoogleAuthenticationToken(user.getEmail(), user.getRoles());
            return auth;

        }

        throw new BadCredentialsException("Could not authenticate based on token");

    }

    @Override
    public boolean supports(Class<?> aClass) {

        return aClass ==null ? false :aClass.equals(GoogleAuthenticationToken.class);
    }
}
