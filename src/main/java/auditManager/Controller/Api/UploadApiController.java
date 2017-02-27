package auditManager.Controller.Api;

import auditManager.Controller.Service.ImageTransfer;
import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.DTO.UploadDTO;
import auditManager.Model.Upload;
import auditManager.Model.UploadRepository;
import auditManager.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nettle on 2017/2/7.
 */
@Controller
@RequestMapping("/api/upload")
public class UploadApiController {

    @Autowired
    private ImageTransfer imageTransfer;

    @Autowired
    private UploadRepository uploadRepository;

    @Value("${auditManager.photo.path}")
    private String photoPath;

    @Value("${auditManager.document.path}")
    private String documentPath;

    @Value("${auditManager.temp.path}")
    private String tempPath;

    private String getFilePath(Date nowTime) {
        return String.format("%d", 1900 + nowTime.getYear()) + "/" + String.format("%02d", nowTime.getMonth() + 1) + "/";
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    private ResponseEntity<UploadDTO> uploadImage(
            @RequestParam(value = "file") MultipartFile file,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && (UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser) ||
                UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser))) {
            File path = new File(tempPath);
            if (!path.exists()) {
                path.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            File targetFile = new File(path, fileName);

            try {
                file.transferTo(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Date nowTime = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd_HHmmss");

            String filePath = getFilePath(nowTime);
            String savePath = photoPath + filePath;
            String saveName = "Photo_" + time.format(nowTime) + ".jpg";
            path = new File(savePath);
            if (!path.exists()) {
                path.mkdirs();
            }

            try {
                imageTransfer.reduceImage(tempPath + fileName, savePath + saveName);
            } catch (Exception e) {
                return new ResponseEntity<UploadDTO>((UploadDTO) null, HttpStatus.BAD_REQUEST);
            }

            Upload upload = new Upload();
            upload.setName(saveName);
            upload.setUri(filePath + saveName);
            upload.setType("photo");
            upload.setUserId(currentUser.getId());
            try {
                uploadRepository.save(upload);
            }   catch (Exception e) {
                return new ResponseEntity<UploadDTO>((UploadDTO) null, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<UploadDTO>(new UploadDTO(upload), HttpStatus.OK);
        }
        return new ResponseEntity<UploadDTO>((UploadDTO) null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/document", method = RequestMethod.POST)
    private ResponseEntity<UploadDTO> uploadDocument(
            @RequestParam(value = "file") MultipartFile file,
            @SessionAttribute(value="currentUser", required = false) User currentUser
    ) {
        if (currentUser != null && (UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, currentUser) ||
                UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, currentUser))) {

            String filePath = getFilePath(new Date());
            String savePath = documentPath + filePath;

            File path = new File(savePath);
            if (!path.exists()) {
                path.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            File targetFile = new File(path, fileName);

            try {
                file.transferTo(targetFile);
            } catch (Exception e) {
                return new ResponseEntity<UploadDTO>((UploadDTO) null, HttpStatus.BAD_REQUEST);
            }

            Upload upload = new Upload();
            upload.setName(fileName);
            upload.setUri(filePath + fileName);
            upload.setType("document");
            upload.setUserId(currentUser.getId());
            try {
                uploadRepository.save(upload);
            }   catch (Exception e) {
                return new ResponseEntity<UploadDTO>((UploadDTO) null, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<UploadDTO>(new UploadDTO(upload), HttpStatus.OK);
        }
        return new ResponseEntity<UploadDTO>((UploadDTO) null, HttpStatus.BAD_REQUEST);
    }
}
