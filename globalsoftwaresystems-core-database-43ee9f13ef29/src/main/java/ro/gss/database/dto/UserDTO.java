package ro.gss.database.dto;

import java.util.Date;
import java.util.List;

import com.mysema.query.annotations.QueryProjection;

import lombok.Data;

@Data
public class UserDTO {
	private Long id;
	private Date startDate;
	private Date endDate;
	private String login;
	private String newPassword;
	private String confirmPassword;
	private String currentPassword;
	private List<String> roles;
	
	public UserDTO() {
		
	}

	@QueryProjection
	public UserDTO(Long id, Date startDate, Date endDate, String login) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.login = login;
	}

	public UserDTO(Long id, Date startDate, Date endDate, String login, List<String> roles) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.login = login;
		this.roles = roles;
	}

	public UserDTO(Long id, Date startDate, Date endDate, String login, List<String> roles, final String currentPassword) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.login = login;
		this.roles = roles;
		this.currentPassword = currentPassword;
	}
}
