package auditManager.Model.DTO;

import auditManager.Model.Photo;

/**
 * Created by Nettle on 2017/2/10.
 */
public class PhotoDTO {
    private Long id;
    private Long index;
    private String imageUrl;

    public PhotoDTO() {}

    public PhotoDTO(Photo photo) {
        this.id = photo.getId();
        this.index = photo.getIdx();
        this.imageUrl = "/photo/" + photo.getImageUrl();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
