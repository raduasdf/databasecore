package ro.gss.database.rest;

import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.gss.database.dto.FlatPage;
import ro.gss.database.dto.GridRequestDTO;
import ro.gss.database.dto.MyUserDTO;
import ro.gss.database.service.MyUserService;
import ro.gss.database.utils.CustomHeaders;
import ro.gss.database.utils.UserInfoDecoder;

@Slf4j
@RestController
@RequestMapping("/myusers")
@AllArgsConstructor
public class MyUserRestController {

	private final MyUserService myUserService;
	private final UserInfoDecoder userInfoDecoder;
	
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public FlatPage<MyUserDTO> getUsersList(@RequestBody GridRequestDTO gridRequest,
			@RequestHeader(value = CustomHeaders.USER_INFO_HEADER, required = false) final String encodedUser) {
		log.info("User: {}", userInfoDecoder.decodeUserFromBase64Value(encodedUser));
		FlatPage<MyUserDTO> flatPage = new FlatPage<>(myUserService.getUsersWithPagination(gridRequest));
		return flatPage;
	}

	@GetMapping("/users")
	public Collection<MyUserDTO> getAllUsers() {
		return myUserService.getAllUsers();
	}

	@DeleteMapping("/users")
	public ResponseEntity<String> deleteUserById(@RequestParam Long id) {
		myUserService.deleteUser(id);;
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	
	@PostMapping("/users")
	public ResponseEntity<String> addUser(@RequestBody MyUserDTO user) {
		myUserService.addUser(user);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PutMapping("/users")
	public ResponseEntity<String> updateUser(@RequestBody MyUserDTO user) {
		myUserService.updateUser(user);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
