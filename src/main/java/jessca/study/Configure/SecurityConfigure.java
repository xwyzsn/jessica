package jessca.study.Configure;

import jessca.study.security.FailHandler;
import jessca.study.security.JwtAuthentication;
import jessca.study.security.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigure extends WebSecurityConfigurerAdapter {
    private static final String [] URL_WHITELIST = {
        "login",
        "logout"
    };
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private FailHandler failHandler;
    @Autowired
    private SuccessHandler successHandler;
    @Bean
    JwtAuthentication jwtAuthenticationFilter() throws Exception {
        JwtAuthentication jwtAuthenticationFilter = new JwtAuthentication(authenticationManager());
        return jwtAuthenticationFilter;
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // 内存中得用户名
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String password = passwordEncoder.encode("123");
//        authenticationManagerBuilder.inMemoryAuthentication().withUser("zzh").password(password).roles("admin");
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(password());

    }
    @Bean
    PasswordEncoder password(){

        return new BCryptPasswordEncoder();

    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .formLogin()
                .successHandler(successHandler)
                .failureHandler(failHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthenticationFilter());
    }




}
