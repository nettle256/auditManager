package auditManager.Model;

import org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.*;

/**
 * Created by Nettle on 2017/2/27.
 */
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private Long theme;

    private Long updated;

    private String outerUrl;

    @Lob
    private String images;

    @Lob
    private String attachments;

    private Long articleId;

    private String userName;

    private Boolean useOuter = false;

    private Boolean deleted = false;

    private Boolean published = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTheme() {
        return theme;
    }

    public void setTheme(Long theme) {
        this.theme = theme;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOuterUrl() {
        return StringEscapeUtils.unescapeHtml(outerUrl);
    }

    public void setOuterUrl(String outerUrl) {
        this.outerUrl = StringEscapeUtils.escapeHtml(outerUrl);
    }


    public Boolean getUseOuter() {
        return useOuter;
    }

    public void setUseOuter(Boolean useOuter) {
        this.useOuter = useOuter;
    }
}
