package auditManager.Controller;

import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Created by Nettle on 2017/2/6.
 */

@Controller
public class Kernel {
    @RequestMapping("/")
    public String index(
            @SessionAttribute(value="currentUser", required=false) User currentUser
    )   {
        if (currentUser != null)
            return "app";
        return "sign";
    }
}

