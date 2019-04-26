package com.liuhll.hl.auth.client.interceptor;

import com.liuhll.hl.auth.client.conf.ServiceAuthConfig;
import com.liuhll.hl.auth.client.feign.ServiceAuthClient;
import com.liuhll.hl.common.enums.ResultCode;
import com.liuhll.hl.common.exception.HlException;
import com.liuhll.hl.common.exception.UnAuthorizedException;
import com.liuhll.hl.common.runtime.session.HlContextSession;
import com.liuhll.hl.common.security.ISecurityWhitelistHandler;
import com.liuhll.hl.auth.client.annotation.IgnoreUserToken;
import com.liuhll.hl.auth.common.jwt.IJwtTokenProvider;
import com.liuhll.hl.auth.common.jwt.JwtUserClaims;
import com.liuhll.hl.common.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IJwtTokenProvider jwtTokenProvider;

    @Autowired
    private ServiceAuthConfig serviceAuthConfig;

    @Autowired
    private ISecurityWhitelistHandler securityWhitelistHandler;

    @Autowired
    private ServiceAuthClient serviceAuthClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 配置该注解，说明不进行用户拦截
        IgnoreUserToken annotation = handlerMethod.getBeanType().getAnnotation(IgnoreUserToken.class);
        if (annotation == null) {
            annotation = handlerMethod.getMethodAnnotation(IgnoreUserToken.class);
        }
        if (annotation != null) {
            return super.preHandle(request, response, handler);
        }
        String webapi = request.getRequestURI();
        if (securityWhitelistHandler.isPermitAuth(webapi)){
            return super.preHandle(request,response,handler);
        }
        String token = jwtTokenProvider.resolveToken(request,serviceAuthConfig.getTokenHeader());
        if (StringUtils.isEmpty(token)) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(serviceAuthConfig.getTokenHeader())) {
                        token = cookie.getValue();
                    }
                }
            }
        }
        if (StringUtils.isEmpty(token)){
            throw new UnAuthorizedException("您还没有登录系统");
        }
        ResponseResult<String> getJwtSecretResult = serviceAuthClient.getJwtSecret(serviceAuthConfig.getClientId(),serviceAuthConfig.getClientSecret());
        if (getJwtSecretResult.getCode() != ResultCode.Ok){
            throw new HlException(getJwtSecretResult.getMessage(),getJwtSecretResult.getCode());
        }

        JwtUserClaims userClaims = jwtTokenProvider.getJwtUserClaims(token,getJwtSecretResult.getData());
        HlContextSession.setUserId(userClaims.getUserid());
        HlContextSession.setUserName(userClaims.getUsername());
        HlContextSession.setAuthToken(token);
        return super.preHandle(request,response,handler);
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HlContextSession.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
