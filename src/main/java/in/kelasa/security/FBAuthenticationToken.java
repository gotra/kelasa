package in.kelasa.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by rajeevguru on 13/11/15.
 */
public class FBAuthenticationToken extends AbstractAuthenticationToken {


    private String token;
    private static final long serialVersionUID = 320L;
    private final Object principal;



    public FBAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities, String token) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public FBAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    public FBAuthenticationToken(String token) {
        super(null);
        this.principal = null;
        this.token = token;
        super.setAuthenticated(false);
    }





    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if(isAuthenticated) {
            throw new IllegalArgumentException("Once created you cannot set this token to authenticated. Create a new instance using the constructor which takes a GrantedAuthority list will mark this as authenticated.");
        } else {
            super.setAuthenticated(false);
        }
    }



}
