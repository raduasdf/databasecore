package ro.gss.database.dto;



import com.mysema.query.annotations.QueryProjection;

import lombok.Data;
import ro.gss.database.entity.MyUser;


@Data
public class MyUserDTO {
	
	private Long id;
	private String userName;
	private String password;
	private String email;
	private String address;
	private String gender;
	
	@QueryProjection
	public MyUserDTO(Long id, String userName, String password, String email, String address, String gender) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.address = address;
		this.gender = gender;
	}
	
	public MyUserDTO(MyUser user) {
		super();
		this.id = user.getId();
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.email = user.getEmail();
		this.address = user.getAddress();
		this.gender = user.getNomItem().getValue();
	}
	
}
