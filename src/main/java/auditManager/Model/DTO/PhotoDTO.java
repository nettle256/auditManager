package auditManager.Model.DTO;

import auditManager.Model.Photo;

/**
 * Created by Nettle on 2017/2/10.
 */
public class PhotoDTO {
    private Long id;
    private Long connect;
    private Long theme;
    private String imageUrl;

    public PhotoDTO() {}

    public PhotoDTO(Photo photo) {
        this.id = photo.getId();
        this.connect = photo.getConnect();
        this.theme = photo.getTheme();
        this.imageUrl = "/photo/" + photo.getImageUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConnect() {
        return connect;
    }

    public void setConnect(Long connect) {
        this.connect = connect;
    }

    public Long getTheme() {
        return theme;
    }

    public void setTheme(Long theme) {
        this.theme = theme;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
