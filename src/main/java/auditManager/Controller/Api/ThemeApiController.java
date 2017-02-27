package auditManager.Controller.Api;

import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.DTO.ThemeDTO;
import auditManager.Model.Theme;
import auditManager.Model.ThemeRepository;
import auditManager.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nettle on 2017/2/6.
 */

@Controller
@RequestMapping("/api/theme")
public class ThemeApiController {
    @Autowired
    ThemeRepository themeRepository;

    /*
     * 获取栏目列表
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    private @ResponseBody
    List<Theme> getThemes(
        @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        List<Theme> themes = new ArrayList<Theme>();
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser)) {
            return themeRepository.findByDeleted(false);
        }
        return themes;
    }

    /*
     * 增加新的栏目
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    private ResponseEntity<ThemeDTO> createTheme(
        @RequestBody ThemeDTO themeDTO,
        @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser)) {
            Theme theme = new Theme();
            theme.setName(themeDTO.getName());
            theme.setFather(themeDTO.getFather());
            if (themeDTO.getModule() != null)
                theme.setModule(themeDTO.getModule());
            else theme.setModule(0L);
            try {
                themeRepository.save(theme);
            }   catch (Exception e) {
                return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<ThemeDTO>(new ThemeDTO(theme), HttpStatus.OK);
        }
        return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
    }

    /*
     * 修改栏目信息
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    private ResponseEntity<ThemeDTO> updateTheme(
            @PathVariable(value="id") Long id,
            @RequestBody ThemeDTO themeDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser)) {
            Theme theme = themeRepository.findById(id);
            if (theme.getUnmodifiable()) {
                return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
            }

            if (themeDTO.getName() != null) theme.setName(themeDTO.getName());
            if (themeDTO.getFather() != null) theme.setFather(themeDTO.getFather());
            if (themeDTO.getHide() != null) theme.setHide(themeDTO.getHide());
            if (themeDTO.getModule() != null) theme.setModule(themeDTO.getModule());

            try {
                themeRepository.save(theme);
            }   catch (Exception e) {
                return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<ThemeDTO>(new ThemeDTO(theme), HttpStatus.OK);
        }
        return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
    }

    /*
     * 删除栏目
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    private ResponseEntity<ThemeDTO> deleteTheme(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser)) {
            Theme theme = themeRepository.findById(id);
            if (theme.getUnmodifiable()) {
                return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
            }
            theme.setDeleted(true);
            List<Theme> childes = themeRepository.findAllByFather(theme.getId());
            for (Theme subTheme:childes)
                subTheme.setDeleted(true);
            try {
                themeRepository.save(theme);
                themeRepository.save(childes);
            }   catch (Exception e) {
                return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.OK);
        }
        return new ResponseEntity<ThemeDTO>((ThemeDTO) null, HttpStatus.BAD_REQUEST);
    }
}
