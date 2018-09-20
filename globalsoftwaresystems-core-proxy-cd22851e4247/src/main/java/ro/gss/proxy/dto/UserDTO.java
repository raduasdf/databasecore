package ro.gss.proxy.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class UserDTO {
	private Long id;
	private Date startDate;
	private Date endDate;
	private String login;
	private String newPassword;
	private String confirmPassword;
	private String currentPassword;
	private List<String> roles;
}
