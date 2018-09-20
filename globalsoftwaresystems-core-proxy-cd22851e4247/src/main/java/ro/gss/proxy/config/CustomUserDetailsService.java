package ro.gss.proxy.config;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import ro.gss.proxy.dto.UserDTO;

@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private RestTemplate restTempalte;

	@Value("${zuul.routes.database-api.url}")
	private String contentStoreUrl;

	private static String USER_ENDPOINT = "/users/get/{lowercaseLogin}";

	@Override
	public UserDetails loadUserByUsername(final String login) {
		final String username = login.trim();
		final StringBuilder requestStr = new StringBuilder();

		log.info("Trying to login with username: " + username);
		try {
			final ResponseEntity<UserDTO> UserDTO = restTempalte.getForEntity(
					requestStr.append(contentStoreUrl).append(USER_ENDPOINT).toString(), UserDTO.class, username);
			log.info("User information request status: " + UserDTO.getStatusCode());
			final UserDTO body = UserDTO.getBody();
			final Optional<UserDTO> userFromDatabase = UserDTO.getStatusCode().equals(HttpStatus.OK) && body != null
					? Optional.of(body)
					: Optional.empty();

			log.info("User information found: " + body);

			return userFromDatabase.map(user -> {
				return new UserInfo(username, user.getCurrentPassword(), user.getRoles().stream().map(role -> {
					return new SimpleGrantedAuthority(role);
				}).collect(Collectors.toList()), user.getId());
			}).orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		throw new UsernameNotFoundException("User " + username + " not found!");
	}
}