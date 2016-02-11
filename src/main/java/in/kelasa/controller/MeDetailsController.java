package in.kelasa.controller;

import in.kelasa.model.User;
import in.kelasa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rajeevguru on 19/11/15.
 */
@RestController
@RequestMapping(value = "/api/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeDetailsController extends AbstractController{

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUserDetails() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username

        User user = userService.getUserByEmail(name);
        if (user == null) {
            ErrorMessage errorMessage = new ErrorMessage("user not found");
            return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);


    }

}
