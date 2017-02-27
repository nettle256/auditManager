package auditManager.Controller.Api;

import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.*;
import auditManager.Model.DTO.MessageDTO;
import auditManager.Model.DTO.ModuleDTO;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nettle on 2017/2/8.
 */
@Controller
@RequestMapping("/api/module")
public class ModuleApiController {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private @ResponseBody
    List<ModuleDTO> getModule(
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        List<ModuleDTO> moduleDTOS = new ArrayList<ModuleDTO>();
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            List<Module> modules = moduleRepository.findAllByDeleted(false);
            for (Module module:modules) {
                moduleDTOS.add(new ModuleDTO(module));
            }
        }
        return moduleDTOS;
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.GET)
    private ResponseEntity<ModuleDTO> getSingleModule(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Module module = moduleRepository.findById(id);
            Article article = articleRepository.findById(module.getArticleId());
            return new ResponseEntity<ModuleDTO>(new ModuleDTO(module, article), HttpStatus.OK);
        }
        return new ResponseEntity<ModuleDTO>((ModuleDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private ResponseEntity<ModuleDTO> createModule(
            @RequestBody ModuleDTO moduleDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Article article = new Article();
            article.setCreated(new Date().getTime());
            article.setUpdated(article.getCreated());
            article.setContent(moduleDTO.getContent());
            article.setUserId(currentUser.getId());

            try {
                articleRepository.save(article);
            }   catch (Exception e) {
                return new ResponseEntity<ModuleDTO>((ModuleDTO) null, HttpStatus.BAD_REQUEST);
            }

            Module module = new Module();
            module.setArticleId(article.getId());
            module.setName(moduleDTO.getName());
            module.setUpdated(article.getUpdated());

            try {
                moduleRepository.save(module);
            }   catch (Exception e) {
                return new ResponseEntity<ModuleDTO>((ModuleDTO) null, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<ModuleDTO>(new ModuleDTO(module, article), HttpStatus.OK);
        }
        return new ResponseEntity<ModuleDTO>((ModuleDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.PUT)
    private ResponseEntity<ModuleDTO> updateModule(
            @PathVariable(value="id") Long id,
            @RequestBody ModuleDTO moduleDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Module module = moduleRepository.findById(id);
            Article article = articleRepository.findById(module.getArticleId());

            article.setContent(moduleDTO.getContent());
            article.setUpdated(new Date().getTime());

            try {
                articleRepository.save(article);
            }   catch (Exception e) {
                return new ResponseEntity<ModuleDTO>((ModuleDTO) null, HttpStatus.BAD_REQUEST);
            }

            module.setName(moduleDTO.getName());
            module.setUpdated(article.getUpdated());

            try {
                moduleRepository.save(module);
            }   catch (Exception e) {
                return new ResponseEntity<ModuleDTO>((ModuleDTO) null, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<ModuleDTO>(new ModuleDTO(module, article), HttpStatus.OK);
        }
        return new ResponseEntity<ModuleDTO>((ModuleDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    private ResponseEntity<MessageDTO> deleteModule(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Module module = moduleRepository.findById(id);
            module.setDeleted(true);
            try {
                moduleRepository.save(module);
            }   catch (Exception e) {
                return new ResponseEntity<MessageDTO>(new MessageDTO("模块删除失败"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<MessageDTO>(new MessageDTO("成功删除模块"), HttpStatus.OK);
        }
        return new ResponseEntity<MessageDTO>(new MessageDTO("模块删除失败"), HttpStatus.BAD_REQUEST);
    }

}
