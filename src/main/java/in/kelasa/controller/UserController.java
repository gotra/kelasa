package in.kelasa.controller;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoException;
import com.mongodb.MongoSocketException;
import in.kelasa.model.User;
import in.kelasa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by rajeevguru on 04/11/15.
 */
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends AbstractController{

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser( @Valid @RequestBody  User user) {



        try {
            userService.create(user);
        } catch (DuplicateKeyException e) {
            // cannot create user with the same username!
            ErrorMessage errorMessage = new ErrorMessage("username " + user.getEmail() + " already in use");
            return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserById(@PathVariable String id) {

        User user = userService.getUser(id);
        if (user == null) {
            ErrorMessage errorMessage = new ErrorMessage("user not found");
            return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);


    }




}
