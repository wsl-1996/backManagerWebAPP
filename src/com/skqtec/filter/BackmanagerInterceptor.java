package com.skqtec.filter;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BackmanagerInterceptor extends HandlerInterceptorAdapter {
    static Logger logger = Logger.getLogger(BackmanagerInterceptor.class.getName());
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        logger.info("preHandle url:"+request.getRequestURI());
        logger.info("request queryString:"+ request.getQueryString());
        logger.info("request parameterMap:"+ JSON.toJSONString(request.getParameterMap()));
        logger.info("request remoteUser:"+ request.getRemoteUser());
        logger.info("request remoteHost:"+ request.getRemoteHost());
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        Object authoridObject = session.getAttribute("authorid");
        if (authoridObject==null||(String)authoridObject=="") {
            logger.info("用户未登录请先登录");
            return false;
        }else {
            logger.info("管理员："+(String)authoridObject+" 正在登录，sessionId："+sessionId);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        logger.info("postHandle url:"+request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("afterCompletion url:"+request.getRequestURI());
    }
}
