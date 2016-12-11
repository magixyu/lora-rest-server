package lorapp.rest.Interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/12
 */
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    /**
     * need to confirm with front side developer about the resources related to login page
     */
    public final static String LOGIN_URL = "/login.html";


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UrlSecurityInterceptor()).addPathPatterns("/**").excludePathPatterns(LOGIN_URL);
    }
}
