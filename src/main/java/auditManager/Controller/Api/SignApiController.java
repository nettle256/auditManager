package auditManager.Controller.Api;

import auditManager.Controller.Service.MD5;
import auditManager.Model.DTO.MessageDTO;
import auditManager.Model.DTO.UserDTO;
import auditManager.Model.User;
import auditManager.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Date;

/**
 * Created by Nettle on 2017/2/6.
 */
@Controller
@SessionAttributes("currentUser")
public class SignApiController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public ResponseEntity<MessageDTO> login(
            @RequestBody User loginUser,
            ModelMap model
    )   {
        if (model.containsAttribute("currentUser"))
            return new ResponseEntity<MessageDTO>(new MessageDTO("错误:用户已登录"), HttpStatus.BAD_REQUEST);

        User user = userRepository.findByUsername(loginUser.getUsername());
        if (user == null || user.getDeleted())
            return new ResponseEntity<MessageDTO>(new MessageDTO("错误:该用户不存在"), HttpStatus.BAD_REQUEST);

        try {
            if (!user.getPassword().equals(MD5.md5(loginUser.getPassword())))
                return new ResponseEntity<MessageDTO>(new MessageDTO("错误:密码错误"), HttpStatus.BAD_REQUEST);
        }   catch (Exception e) {
            return new ResponseEntity<MessageDTO>(new MessageDTO("错误:系统错误"), HttpStatus.BAD_REQUEST);
        }

        model.addAttribute("currentUser", user);
        user.setLastVisit(new Date().getTime());
        userRepository.save(user);
        return new ResponseEntity<MessageDTO>(new MessageDTO("成功:用户 " + user.getUsername() + " 登录"), HttpStatus.OK);
    }

    @RequestMapping(value = "api/logout", method = RequestMethod.GET)
    public @ResponseBody
    void logout(
            SessionStatus status
    )   {
        status.setComplete();
    }
}
