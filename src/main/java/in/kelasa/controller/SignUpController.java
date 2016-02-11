package in.kelasa.controller;

import com.mongodb.DuplicateKeyException;
import in.kelasa.model.User;
import in.kelasa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeevguru on 07/11/15.
 */

@RestController
@RequestMapping("/api/signup")
public class SignUpController extends AbstractController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            ErrorMessage errorMessage = new ErrorMessage("user " + auth.getName() + " already logged in");
            return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
        }



        try {
            //ensure that the user always has normal role
            GrantedAuthority authority = new SimpleGrantedAuthority("normal");
            List authorities = new ArrayList<GrantedAuthority>();
            authorities.add(authority);
            user.setRoles(authorities);
            userService.create(user);
        } catch (DuplicateKeyException e) {
            // cannot create user with the same username!
            ErrorMessage errorMessage = new ErrorMessage("username " + user.getEmail() + " already in use");
            return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

}
