package auditManager.Controller.Service;

import auditManager.Model.User;
import org.springframework.stereotype.Service;

/**
 * Created by Nettle on 2017/1/5.
 */
@Service
public class UserAuthority {
    public static Long USER_SYSTEM = 8L;
    public static Long ARTICLE_SYSTEM = 4L;
    public static Long POSTER_SYSTEM = 2L;

    public static Boolean checkCurrentUserAuthority(
            Long requestAuthority,
            User currentUser
    ) {
        return (currentUser != null && ((currentUser.getAuthority() & requestAuthority) == requestAuthority));
    }
}
