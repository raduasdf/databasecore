package ro.gss.proxy.config.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginAttemptsLogger {

	private final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptsLogger.class);

	@EventListener
	public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {
		final AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
		final WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");
		LOGGER.info("Principal {}; Remote IP address: {}; Session Id: {}; Request URL: {}",
				auditEvent.getPrincipal() + " - " + auditEvent.getType(), details.getRemoteAddress(),
				details.getSessionId(), auditEvent.getData().get("requestUrl"));
	}
}
