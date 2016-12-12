package lorapp.rest.Interceptor;

import lorapp.db.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/12
 */
public class UrlSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            response.sendRedirect(InterceptorConfig.LOGIN_URL);
            return false;
        }else{
            String userRole = user.getRole();
            if(!User.USER_ADMIN.equals(userRole)){
                String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
                if(requestUrl.startsWith(InterceptorConfig.ADMIN_DIR)){
                    //ordin user can not access admin related pages
                    response.sendRedirect(InterceptorConfig.LOGIN_URL);
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}