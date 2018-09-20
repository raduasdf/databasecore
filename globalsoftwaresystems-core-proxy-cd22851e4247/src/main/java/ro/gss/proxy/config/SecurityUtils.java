package ro.gss.proxy.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public final class SecurityUtils {

	public static String getCurrentUserLogin() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		String userName = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserInfo) authentication.getPrincipal();
				userName = springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				userName = (String) authentication.getPrincipal();
			}
		}
		return userName;
	}

	public static Long getCurrentUserId() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		Long userId = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserInfo springSecurityUser = (UserInfo) authentication.getPrincipal();
				userId = springSecurityUser.getUserId();
			}
		}
		return userId;
	}

	public static boolean isAuthenticated() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext == null || securityContext.getAuthentication() == null) {
			return false;
		}
		final Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
		if (authorities != null) {
			for (GrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)) {
					return false;
				}
			}
		}
		return true;
	}

	public static UserInfo getCurrentUser() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof User) {
				return (UserInfo) authentication.getPrincipal();
			}
		}
		return null;
	}

	public static boolean isCurrentUserInRole(String authority) {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
			}
		}
		return false;
	}
}
