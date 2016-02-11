package in.kelasa.service;

import com.mongodb.WriteResult;
import in.kelasa.model.Review;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by rajeevguru on 14/12/15.
 */
public class ReviewService {

    @Autowired
    private MongoCollection reviews;

    public Review create(Review review) {


        reviews.save(review);
        return review;
    }

    public Review update(Review review) {
        WriteResult result =  reviews.save(review);
        return review;
    }


}
