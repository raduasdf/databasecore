package ro.gss.database.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.gss.database.dto.FlatPage;
import ro.gss.database.dto.GridRequestDTO;
import ro.gss.database.dto.UserDTO;
import ro.gss.database.service.UserService;
import ro.gss.database.utils.CustomHeaders;
import ro.gss.database.utils.UserInfoDecoder;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersRestController {

	private final UserService userService;
	private final UserInfoDecoder userInfoDecoder;

	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public FlatPage<UserDTO> getUsersList(@RequestBody GridRequestDTO gridRequest,
			@RequestHeader(value = CustomHeaders.USER_INFO_HEADER, required = false) final String encodedUser) {
		log.info("User: {}", userInfoDecoder.decodeUserFromBase64Value(encodedUser));
		FlatPage<UserDTO> flatPage = new FlatPage<>(userService.getUsersWithPagination(gridRequest));
		return flatPage;
	}

	@GetMapping("/roles")
	public List<String> getAllRoleCodes() {
		return userService.getAllRoleCodes();
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody UserDTO user) {
		userService.saveUser(user);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PutMapping("/deactivate/{id}")
	public ResponseEntity<String> deactivateUserById(@PathVariable final Long id) {
		userService.deactivateUserById(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PutMapping("/activate/{id}")
	public ResponseEntity<String> activateUserById(@PathVariable final Long id) {
		userService.activateUserById(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@GetMapping("/get/{login:.*}")
	public ResponseEntity<UserDTO> getUserByLogin(@PathVariable final String login) {
		return ResponseEntity.ok().body(userService.getUserByLogin(login));
	}
}
