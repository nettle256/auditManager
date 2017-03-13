package auditManager.Model.DTO;

import auditManager.Model.Article;
import auditManager.Model.News;
import auditManager.Model.User;

/**
 * Created by Nettle on 2017/2/27.
 */
public class NewsDTO {

    private Long id;
    private String title;
    private Long theme;
    private Long updated;
    private String images;
    private String attachments;
    private String content;
    private String userName;
    private Boolean deleted = false;
    private Boolean published = false;
    private Boolean useOuter = false;
    private String outerUrl;

    NewsDTO() {};

    public NewsDTO(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.theme = news.getTheme();
        this.updated = news.getUpdated();
        this.images = news.getImages();
        this.attachments = news.getAttachments();
        this.deleted = news.getDeleted();
        this.published = news.getPublished();
        this.userName = news.getUserName();
        this.useOuter = news.getUseOuter();
        this.outerUrl = news.getOuterUrl();
        this.content = null;
    }

    public NewsDTO(News news, Article article) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.theme = news.getTheme();
        this.updated = news.getUpdated();
        this.images = news.getImages();
        this.attachments = news.getAttachments();
        this.deleted = news.getDeleted();
        this.published = news.getPublished();
        this.userName = news.getUserName();
        this.useOuter = news.getUseOuter();
        this.outerUrl = news.getOuterUrl();
        this.content = article.getContent();
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Boolean getUseOuter() {
        return useOuter;
    }

    public void setUseOuter(Boolean useOuter) {
        this.useOuter = useOuter;
    }

    public String getOuterUrl() {
        return outerUrl;
    }

    public void setOuterUrl(String outerUrl) {
        this.outerUrl = outerUrl;
    }
}
