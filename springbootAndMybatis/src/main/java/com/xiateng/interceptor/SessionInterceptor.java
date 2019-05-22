package com.xiateng.interceptor;

import com.xiateng.entity.TUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，检验是否是登录状态
 */
public class SessionInterceptor implements HandlerInterceptor{
    private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录不做拦截
        logger.info("拦截页面："+request.getRequestURI());
        if (request.getRequestURI().equals("/user/login_view") || request.getRequestURI().equals("/user/login")){
            return true;
        }

        // 验证session是否存在
        Object object = request.getSession().getAttribute("_session_user");

            if (object == null){
            response.sendRedirect("/user/login_view");
            return false;
        }
        TUser tUser = (TUser) object;
        logger.info("session："+"userid= "+tUser.getUserId()+" userName= "+tUser.getUserName());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        logger.info("---postHandle被调用------");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        logger.info("---afterCompletion被调用------");
    }
}
