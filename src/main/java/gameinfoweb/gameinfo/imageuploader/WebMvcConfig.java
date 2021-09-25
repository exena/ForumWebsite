package gameinfoweb.gameinfo.imageuploader;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequestWrapper;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/summernoteImage/**")
                .addResourceLocations("file:///home/osusml2135/summernote_image/");
        //file:///home/osusml2135/gameinfo/src/main/resources/static
        //file:///C:/summernote_image/
    }
}
