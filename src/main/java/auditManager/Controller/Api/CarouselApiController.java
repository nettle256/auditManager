package auditManager.Controller.Api;

import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.*;
import auditManager.Model.DTO.CarouselDTO;
import auditManager.Model.DTO.MessageDTO;
import auditManager.Model.DTO.UploadDTO;
import auditManager.Model.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nettle on 2017/2/8.
 */
@Controller
@RequestMapping("/api/carousel")
public class CarouselApiController {

    @Autowired
    private CarouselRepository carouselRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    private @ResponseBody
    List<CarouselDTO> getCarousels(
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        List<CarouselDTO> carouselDTOS = new ArrayList<CarouselDTO>();
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            List<Carousel> carousels = carouselRepository.findAllByDeleted(false);
            for (Carousel carousel:carousels)
                carouselDTOS.add(new CarouselDTO(carousel));
        }
        return carouselDTOS;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    private ResponseEntity<CarouselDTO> createCarousel(
            @RequestBody UploadDTO uploadDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Carousel carousel = new Carousel();
            carousel.setUploadId(uploadDTO.getId());
            carousel.setImageUrl(uploadDTO.getUri());
            try {
                carouselRepository.save(carousel);
            }   catch (Exception e) {
                return new ResponseEntity<CarouselDTO>((CarouselDTO) null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<CarouselDTO>(new CarouselDTO(carousel), HttpStatus.OK);
        }
        return new ResponseEntity<CarouselDTO>((CarouselDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.PUT)
    private ResponseEntity<CarouselDTO> updateCarousel(
            @PathVariable(value="id") Long id,
            @RequestBody CarouselDTO carouselDTO,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Carousel carousel = carouselRepository.findById(id);
            carousel.setIdx(carouselDTO.getIndex());
            carousel.setText(carouselDTO.getText());
            carousel.setConnectId(carouselDTO.getConnect());
            carouselRepository.save(carousel);
            return new ResponseEntity<CarouselDTO>(new CarouselDTO(carousel), HttpStatus.OK);
        }
        return new ResponseEntity<CarouselDTO>((CarouselDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.DELETE)
    private ResponseEntity<MessageDTO> deleteCarousel(
            @PathVariable(value="id") Long id,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    )   {
        if (currentUser != null && UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser)) {
            Carousel carousel = carouselRepository.findById(id);
            carousel.setDeleted(true);
            carouselRepository.save(carousel);
            return new ResponseEntity<MessageDTO>(new MessageDTO("成功删除该图片"), HttpStatus.OK);
        }
        return new ResponseEntity<MessageDTO>(new MessageDTO("删除该图片失败"), HttpStatus.BAD_REQUEST);
    }
}
