package in.kelasa;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.junit.Test;

import java.security.Key;

/**
 * Created by rajeevguru on 15/01/16.
 */
public class MyTest {

    @Test
    public void doStuff() {

        // We need a signing key, so we'll create one just for this example. Usually
// the key would be read from your application configuration instead.
        Key key = MacProvider.generateKey();

        String s = Jwts.builder().setSubject("Joe").signWith(SignatureAlgorithm.RS512, key).compact();

        System.out.println(s +" - "+ key);

    }
}
