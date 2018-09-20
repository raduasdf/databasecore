package ro.gss.proxy.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserInfo extends User {

	private static final long serialVersionUID = -2734328378839341455L;

	private final Long userId;
	
	public UserInfo(final String username, final String password,
			final Collection<? extends GrantedAuthority> authorities, final Long userId) {
		super(username, password, authorities);
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

}