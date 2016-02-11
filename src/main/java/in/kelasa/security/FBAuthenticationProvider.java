package in.kelasa.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import in.kelasa.model.User;
import in.kelasa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * Created by rajeevguru on 13/11/15.
 */
@Service
public class FBAuthenticationProvider implements AuthenticationProvider {


    private HttpRequestFactory requestFactory;
    private URL url;

    @Autowired
    private UserService userService;


    @PostConstruct
    public void init() {

        try {
            HttpTransport httpTransport = new NetHttpTransport();
            requestFactory = httpTransport.createRequestFactory();
            url = new URL("https://graph.facebook.com/v2.5/me");




        } catch (Exception e) {
            //post construct cannot throw exception so throwing a runtime exception
            throw new IllegalStateException(e);
        }




    }

    private User process(String idTokenString) {

        Map<String, Object> map = new HashMap<String, Object>();

        try {
            GenericUrl genericUrl = new GenericUrl(url);
            genericUrl.put("fields","id,first_name,last_name,name,email,picture{url}");
            genericUrl.put("format","json");
            genericUrl.put("access_token",idTokenString);

            HttpRequest httpRequest = requestFactory.buildGetRequest(genericUrl);
            HttpResponse response = httpRequest.execute();

            ObjectMapper objectMapper = new ObjectMapper();

            map = objectMapper.readValue(response.getContent(),new TypeReference<Map<String, Object>>(){});



        } catch (Exception e) {
            e.printStackTrace();
            //todo log in error log
        }
        if (!map.isEmpty()) {


                User user = userService.getUserByEmail((String) map.get("email"));
                if (user == null ) {
                    // We need to update the user
                    user = new User();
                    user.setEmail((String) map.get("email"));
                    GrantedAuthority authority = new SimpleGrantedAuthority("normal");
                    List authorities = new ArrayList<GrantedAuthority>();
                    authorities.add(authority);
                    user.setRoles(authorities);
                    user.setFacebookAccount(true);
                    user.setFirstName((String) map.get("first_name"));
                    user.setLastName((String) map.get("last_name"));
                    userService.create(user);
                 }
                 else {
                    user.setFacebookAccount(true);
                    user.setFirstName((String) map.get("first_name"));
                    user.setLastName((String) map.get("last_name"));
                    userService.update(user);
                }
                return user;

        }

            return null;

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        FBAuthenticationToken fbtoken = (FBAuthenticationToken) authentication;
        User user = process(fbtoken.getToken());
        if (user != null) {
            Authentication auth = new FBAuthenticationToken(user.getEmail(), user.getRoles());
            return auth;

        }

        throw new BadCredentialsException("Could not authenticate based on token");

    }

    @Override
    public boolean supports(Class<?> aClass) {

        return aClass ==null ? false :aClass.equals(FBAuthenticationToken.class);
    }
}
