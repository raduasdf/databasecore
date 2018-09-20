package ro.gss.proxy.zuul.filter;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;
import ro.gss.proxy.config.SecurityUtils;

@Slf4j
@Component
public class UserInfoZuulFilter extends ZuulFilter {
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        try {
			final String userInfoAsJsonValue = objectMapper.writeValueAsString(SecurityUtils.getCurrentUser());
			final String userInfoInBase64 = Base64.getEncoder().encodeToString(userInfoAsJsonValue.getBytes());
			log.info("User \n {} \n with base64 {}", userInfoAsJsonValue, userInfoInBase64);
			ctx.addZuulRequestHeader(CustomHeaders.USER_INFO_HEADER, userInfoInBase64);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 1110;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
