package auditManager.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Nettle on 2017/2/7.
 */

@Component
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    static private String photoPath;
    static private String documentPath;

    @Value("${auditManager.photo.path}")
    public void setPhotoPath(String path) {
        photoPath = "file:///" + path;
    }

    @Value("${auditManager.document.path}")
    public void setDocumentPath(String path) {
        documentPath = "file:///" + path;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photo/**").addResourceLocations(photoPath);
        registry.addResourceHandler("/document/**").addResourceLocations(documentPath);
        super.addResourceHandlers(registry);
    }

}
