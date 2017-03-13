package auditManager.Model;

import javax.persistence.*;

/**
 * Created by Nettle on 2017/2/8.
 */

@Entity
public class Carousel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long idx;

    @Column(nullable=false)
    private Long uploadId;

    @Column(nullable=false)
    private String imageUrl;

    private String text;

    private Long connectId;

    private Long connectTheme;

    private Boolean deleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public Long getUploadId() {
        return uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getConnectId() {
        return connectId;
    }

    public void setConnectId(Long connectId) {
        this.connectId = connectId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getConnectTheme() {
        return connectTheme;
    }

    public void setConnectTheme(Long connectTheme) {
        this.connectTheme = connectTheme;
    }
}
