package in.kelasa.service;

import com.mongodb.WriteResult;
import in.kelasa.model.User;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by rajeevguru on 04/11/15.
 */
@Service
public class UserService {

    @Autowired
    private MongoCollection users;

    public User create(User user) {

        user.setEmail(user.getEmail().toLowerCase());
        if (!StringUtils.isEmpty(user.getPassword()))
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        users.save(user);
        return user;
    }

    public User getUser(String id) {
        User user = users.findOne(new ObjectId(id)).as(User.class);
        return user;
    }

    public User getUserByEmail(String username) {
        User user = users.findOne("{email: #}",username).as(User.class);
        return user;
    }

    public User update(User user) {

       WriteResult result =  users.save(user);
       return user;

    }








}
