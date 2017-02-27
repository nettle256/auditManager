package auditManager.Model.DTO;

import auditManager.Model.Upload;

/**
 * Created by Nettle on 2017/2/8.
 */
public class UploadDTO {
    private Long id;
    private String uri;
    private String name;
    private String type;
    private Long userId;
    private Long created;

    public UploadDTO() {

    }

    public UploadDTO(Upload upload) {
        this.id = upload.getId();
        this.uri = upload.getUri();
        this.name = upload.getName();
        this.type = upload.getType();
        this.userId = upload.getUserId();
        this.created = upload.getCreated();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }
}
