package ro.gss.database.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDTO {
	private String enabled;
	private String username;
	private List<AuthorityDTO> authorities;
	private String userId;
	private String accountNonExpired;
	private String credentialsNonExpired;
	private String accountNonLocked;
}
