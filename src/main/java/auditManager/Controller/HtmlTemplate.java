package auditManager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Nettle on 2017/1/4.
 */
@Controller
@RequestMapping(path="/html")
public class HtmlTemplate {
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public String getHtmlTemplate(HttpServletRequest request) {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return "pages/" + path;
    }
}
