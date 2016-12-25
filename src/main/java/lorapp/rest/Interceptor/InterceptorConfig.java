package lorapp.rest.Interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/12
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    /**
     * need to confirm with front side developer about the resources related to login page
     */
    public final static String LOGIN_URL = "/login/login.html";
    public final static String LOGIN_API = "/login";
    public final static String ADMIN_DIR = "/admin";
    public final static String SWAGGER_UI = "/swagger-ui.html";
    public final static String DEV_API_EXCLUDE = "/simpleSpv/creation";

    @Bean
    public UrlSecurityInterceptor urlSecurityInterceptor(){
        return new UrlSecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
            for the url patterns:
              '/*' can intercept '/rawdata'
                                '/demo.html'
              '/**' can intercept '/rawdata', '/rawdata/appEUI1/devEUI1/all'
                                  '/demo.html', '/demoDir/demo.html'
         */
        registry.addInterceptor(urlSecurityInterceptor()).addPathPatterns("/**").excludePathPatterns(LOGIN_API, LOGIN_URL, SWAGGER_UI, DEV_API_EXCLUDE);
    }
}
