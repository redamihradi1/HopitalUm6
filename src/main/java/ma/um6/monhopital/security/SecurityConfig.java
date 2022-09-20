package ma.um6.monhopital.security;

import ma.um6.monhopital.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*

        System.out.println(passwordEncoder.encode("admin")+"  ****  "+passwordEncoder.encode("user"));
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder.encode("admin")).roles("ADMIN","USER")
                .and()
                .withUser("user").password(passwordEncoder.encode("user")).roles("USER");
                */
        /* ************************************************************************************************************************** */
            /*
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .usersByUsernameQuery("select username as principal, password as credentials, active from users where username=?")
                    .authoritiesByUsernameQuery("select username as principal , role as role from users_roles where username=?")
                    .rolePrefix("ROLE_")
                    .passwordEncoder(passwordEncoder);
            */
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .permitAll();
        http.logout().logoutUrl("/logout")
                .permitAll();

        http.authorizeRequests().antMatchers("/register").permitAll();
        http.authorizeRequests().antMatchers("/register/newUser").permitAll();
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN"); // hasrole
        http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER");
        http.authorizeRequests().antMatchers("/webjars/**").permitAll(); //  ressource on pp y accceder meme si on est pas auth
        http.authorizeRequests()
                .antMatchers("/resources/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/403");

    }



}
