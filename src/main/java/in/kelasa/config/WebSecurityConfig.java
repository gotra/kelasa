package in.kelasa.config;

import in.kelasa.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by rajeevguru on 26/10/15.
 */

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private FBAuthenticationProvider fbAuthenticationProvider;

    @Autowired
    private DBAuthenticationProvider dbAuthenticationProvider;

    @Autowired
    private GoogleAuthenticationProvider googleAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/", "/**").permitAll()
                //allow anonymous POSTs to login
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/google/tokensignin").permitAll()
                .antMatchers(HttpMethod.POST,"/api/facebook/tokensignin").permitAll()
                .antMatchers(HttpMethod.POST, "/api/signup").permitAll()

                //allow anonymous GETs to API
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers("/api/**").authenticated()

                //defined Admin only API area
                .antMatchers("/admin/**").hasRole("ADMIN")

                //all other request need to be authenticated
                .anyRequest().hasAnyRole("admin","user")
                .and()

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
                .addFilterBefore(new StatelessLoginFilter("/api/login", tokenAuthenticationService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
                .addFilterBefore(new GoogleTokenLoginFilter("/api/google/tokensignin", tokenAuthenticationService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
                .addFilterBefore(new FBTokenLoginFilter("/api/facebook/tokensignin", tokenAuthenticationService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

                // custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);



        http.csrf().disable();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(dbAuthenticationProvider)
                .authenticationProvider(fbAuthenticationProvider)
                .authenticationProvider(googleAuthenticationProvider);

    }
}
