package in.kelasa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajeevguru on 07/11/15.
 */
public class AbstractController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        List<String> messages = new ArrayList<String>();

        //WebServiceError webServiceError = WebServiceError.build(WebServiceError.Type.VALIDATION_ERROR, errors.get(0).getObjectName() + " " + errors.get(0).getDefaultMessage());

        for(ObjectError error:errors){


            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                messages.add(fieldError.getField() + " " + fieldError.getRejectedValue()+ " " + error.getDefaultMessage());
            }
            else {

                messages.add(error.toString());

            }


        }


        return new ResponseEntity<ErrorMessage>(new ErrorMessage(messages), HttpStatus.BAD_REQUEST);
    }

}
