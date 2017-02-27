package auditManager.Controller.Api;

import auditManager.Controller.Service.MD5;
import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.DTO.PasswordDTO;
import auditManager.Model.User;
import auditManager.Model.DTO.UserDTO;
import auditManager.Model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nettle on 2017/2/6.
 */
@Controller
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    UserRepository userRepository;

    /*
     * 查询当前用户
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    private ResponseEntity<UserDTO> getCurrentUser(
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser == null)
            return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<UserDTO>(new UserDTO(currentUser), HttpStatus.OK);
    }

    /*
     * 查询所有用户
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    private @ResponseBody
    List<UserDTO> getUsers(
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, currentUser)) {
            List<User> users = userRepository.findAll();
            for (User user:users) {
                if (!UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, user))
                    userDTOS.add(new UserDTO(user));
            }
        }
        return userDTOS;
    }

    /*
     * 创建新用户
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    private ResponseEntity<UserDTO> createUser(
       @RequestBody UserDTO userDTO,
       @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, currentUser)) {
            try {
                User user = new User();
                user.setUsername(userDTO.getUsername());
                user.setPassword(MD5.md5(user.getUsername()));
                Long authority = 0L;
                if (userDTO.getArticleSystem()) authority |= UserAuthority.ARTICLE_SYSTEM;
                if (userDTO.getPosterSystem()) authority |= UserAuthority.POSTER_SYSTEM;
                user.setAuthority(authority);
                userRepository.save(user);
                return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.ACCEPTED);
            }   catch (Exception e) {
                return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.BAD_REQUEST);
            }
        }   else {
            return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.FORBIDDEN);
        }
    }

    /*
     * 更新当前用户密码
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    private ResponseEntity<UserDTO> updateCurrentUserPassword(
            @RequestBody PasswordDTO passwordDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null) {
            try {
                User user = userRepository.findById(currentUser.getId());
                if (user.getPassword().equals(MD5.md5(passwordDTO.getOldPassword()))) {
                    user.setPassword(MD5.md5(passwordDTO.getNewPassword()));
                    userRepository.save(user);
                    return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.BAD_REQUEST);
    }

    /*
     * 重置指定用户密码
     */
    @RequestMapping(value = "/{id:[0-9]+}/reset", method = RequestMethod.PUT)
    private ResponseEntity<UserDTO> updateUserPassword(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, currentUser)) {
            User user = userRepository.findById(id);
            try {
                user.setPassword(MD5.md5(user.getUsername()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            userRepository.save(user);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.BAD_REQUEST);
    }

    /*
     * 更新指定用户权限
     */
    @RequestMapping(value = "/{id:[0-9]+}/authority", method = RequestMethod.PUT)
    private ResponseEntity<UserDTO> updateUserAuthority(
            @PathVariable(value="id") Long id,
            @RequestBody UserDTO userDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, currentUser)) {
            User user = userRepository.findById(id);
            Long authority = 0L;
            if (userDTO.getArticleSystem()) authority |= UserAuthority.ARTICLE_SYSTEM;
            if (userDTO.getPosterSystem()) authority |= UserAuthority.POSTER_SYSTEM;
            user.setAuthority(authority);
            userRepository.save(user);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.BAD_REQUEST);
    }

    /*
     * 恢复指定用户
     */
    @RequestMapping(value = "/{id:[0-9]+}/recover", method = RequestMethod.PUT)
    private ResponseEntity<UserDTO> recoverUserAuthority(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, currentUser)) {
            User user = userRepository.findById(id);
            user.setDeleted(false);
            userRepository.save(user);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.BAD_REQUEST);
    }

    /*
     * 删除指定用户
     */
    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    private ResponseEntity<UserDTO> deleteUserAuthority(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, currentUser)) {
            User user = userRepository.findById(id);
            user.setDeleted(true);
            userRepository.save(user);
            return new ResponseEntity<UserDTO>(new UserDTO(user), HttpStatus.OK);
        }
        return new ResponseEntity<UserDTO>((UserDTO) null, HttpStatus.BAD_REQUEST);
    }
}
