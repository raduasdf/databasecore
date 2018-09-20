package ro.gss.database.utils;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ro.gss.database.dto.UserInfoDTO;

@Slf4j
@Component
public class UserInfoDecoder {

	private final ObjectMapper objectMapper;
	private final Boolean defaultUserEnabled;
	private final UserInfoDTO defaultEncodedUser;

	public UserInfoDecoder(ObjectMapper objectMapper,
			@Value("${default.user.enabled}") final Boolean defaultUserEnabled,
			@Value("${default.user.encoded}") final String defaultEncodedUser)
			throws JsonParseException, JsonMappingException, IOException {
		this.objectMapper = objectMapper;
		this.defaultUserEnabled = defaultUserEnabled;
		if (defaultUserEnabled) {
			this.defaultEncodedUser = objectMapper.readValue(Base64.getDecoder().decode(defaultEncodedUser),
					UserInfoDTO.class);
		} else {
			this.defaultEncodedUser = null;
		}
	}

	public Optional<UserInfoDTO> decodeUserFromBase64Value(final String base64Value) {
		if (base64Value == null) {
			return getDefaultUser();
		}
		final String userInfoString = new String(Base64.getDecoder().decode(base64Value));
		try {
			return Optional.of(objectMapper.readValue(userInfoString, UserInfoDTO.class));
		} catch (Exception e) {
			return getDefaultUser();
		}
	}

	private Optional<UserInfoDTO> getDefaultUser() {
		if (defaultUserEnabled) {
			log.info("No user specified. Using default one: {}", this.defaultEncodedUser);
			try {
				return Optional.of(this.defaultEncodedUser);
			} catch (Exception e1) {
				throw new RuntimeException("Error! Could not parse default user!");
			}
		}
		return Optional.empty();
	}

}
