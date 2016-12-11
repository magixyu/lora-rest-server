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

    public static List<String> adminUserUrls = new ArrayList<String>();

    static{
        adminUserUrls.add("//TODO 用户新增页面");
        adminUserUrls.add("//TODO 用户删除页面");
        adminUserUrls.add("//TODO 用户编辑页面");
        adminUserUrls.add("//TODO 用户查询页面");
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return false;
        }else{
            String userRole = user.getRole();
            if(!User.USER_ADMIN.equals(userRole)){
                String requestUrl = request.getRequestURI();
                for(String adminUserUrl : adminUserUrls){
                    if(adminUserUrl.endsWith(requestUrl)){
                        return false;
                    }
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