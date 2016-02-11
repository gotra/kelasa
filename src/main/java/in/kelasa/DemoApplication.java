package in.kelasa;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.UnknownHostException;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public Jongo jongo() {
        DB db;
        try {
            db = new MongoClient("localhost", 27017).getDB("kelasa");
            db.setWriteConcern(WriteConcern.SAFE );
        } catch (UnknownHostException e) {
            throw new MongoException("Connection error : ", e);
        }
        return new Jongo(db);
    }

    @Bean
    public MongoCollection users() {
        return jongo().getCollection("users");
    }

    @Bean
    public MongoCollection agencies() {
        return jongo().getCollection("agencies");
    }

    @Bean
    public MongoCollection reviews() {
        return jongo().getCollection("reviews");
    }
}
