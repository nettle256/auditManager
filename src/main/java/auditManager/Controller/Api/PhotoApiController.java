package auditManager.Controller.Api;

/**
 * Created by Nettle on 2017/2/10.
 */

import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.*;
import auditManager.Model.DTO.MessageDTO;
import auditManager.Model.DTO.PhotoDTO;
import auditManager.Model.DTO.UploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/photo")
public class PhotoApiController {

    @Autowired
    private  PhotoRepository photoRepository;

    @Autowired
    private NewsRepository newsRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private @ResponseBody
    List<PhotoDTO> getPhotos(
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        List<PhotoDTO> photoDTOS = new ArrayList<PhotoDTO>();
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            List<Photo> photos = photoRepository.findAllByDeleted(false);
            for (Photo photo:photos)
                photoDTOS.add(new PhotoDTO(photo));
        }
        return photoDTOS;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private ResponseEntity<PhotoDTO> createPhoto(
            @RequestBody UploadDTO uploadDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Photo photo = new Photo();
            photo.setUploadId(uploadDTO.getId());
            photo.setImageUrl(uploadDTO.getUri());
            try {
                photoRepository.save(photo);
            }   catch (Exception e) {
                return new ResponseEntity<PhotoDTO>((PhotoDTO) null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<PhotoDTO>(new PhotoDTO(photo), HttpStatus.OK);
        }
        return new ResponseEntity<PhotoDTO>((PhotoDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.PUT)
    private ResponseEntity<PhotoDTO> updatePhoto(
            @PathVariable(value="id") Long id,
            @RequestBody PhotoDTO photoDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Photo photo = photoRepository.findById(id);
            photo.setConnect(photoDTO.getConnect());
            if (photo.getConnect() != null && photo.getConnect() != 0L) {
                News news = newsRepository.findByIdAndDeleted(photo.getConnect(), false);
                if (news != null) photo.setTheme(news.getTheme());
                else {
                    return new ResponseEntity<PhotoDTO>((PhotoDTO) null, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
                }
            }
            photoRepository.save(photo);
            return new ResponseEntity<PhotoDTO>(new PhotoDTO(photo), HttpStatus.OK);
        }
        return new ResponseEntity<PhotoDTO>((PhotoDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    private ResponseEntity<MessageDTO> deletePhoto(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Photo photo = photoRepository.findById(id);
            photo.setDeleted(true);
            photoRepository.save(photo);
            return new ResponseEntity<MessageDTO>(new MessageDTO("成功删除该图片"), HttpStatus.OK);
        }
        return new ResponseEntity<MessageDTO>(new MessageDTO("删除该图片失败"), HttpStatus.BAD_REQUEST);
    }

}
