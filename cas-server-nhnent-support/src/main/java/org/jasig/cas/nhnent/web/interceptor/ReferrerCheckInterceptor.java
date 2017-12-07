package org.jasig.cas.nhnent.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Please describe the role of the ReferrerCheckInterceptor
 * <B>History:</B>
 * Created by taekyeom.oh on 2017. 12. 7.
 *
 * @author taekyeom.oh
 * @version 0.1
 * @since 2017. 12. 7.
 */
public class ReferrerCheckInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String referrer = request.getHeader("referrer");
//        if (referrer == null) {
//            response.sendRedirect("http://www.daum.net");
//            return false;
//        }

        return super.preHandle(request, response, handler);
    }

}
