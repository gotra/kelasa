package in.kelasa.security;

import in.kelasa.model.User;
import in.kelasa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * Created by rajeevguru on 07/11/15.
 */
@Service
public class DBAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userService.getUserByEmail(name);
        if (user != null) {

            if (BCrypt.checkpw(password,user.getPassword())) {

                Authentication auth = new UsernamePasswordAuthenticationToken(name, password, user.getRoles());
                return auth;

            }

        }

        throw new BadCredentialsException("Username/Password incorrect!");
    }

    @Override
    public boolean supports(Class<?> aClass) {

        return aClass ==null ? false :aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
