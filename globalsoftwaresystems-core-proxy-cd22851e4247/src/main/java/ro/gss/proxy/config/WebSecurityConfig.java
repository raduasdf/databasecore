package ro.gss.proxy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private CustomUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
        	.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
				.and()
			.csrf().disable()
			.headers().frameOptions().sameOrigin()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.deleteCookies("JSESSIONID").invalidateHttpSession(true)
				.logoutSuccessUrl("/")
				.and()
			.authorizeRequests()
				.anyRequest().authenticated();
	}
}