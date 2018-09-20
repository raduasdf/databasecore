package ro.gss.proxy.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UrlFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(UrlFilter.class);

	@Override
	@SuppressWarnings("unused")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse res = (HttpServletResponse) response;
		final String path = req.getRequestURI();
		logger.debug("Request path: " + path);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.debug("Initiating " + UrlFilter.class + "!");
	}

	@Override
	public void destroy() {
		logger.debug("Destroying " + UrlFilter.class + "!");
	}

}