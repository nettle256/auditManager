package auditManager.Model;

import org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.*;

/**
 * Created by Nettle on 2017/2/8.
 */
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String content;

    private Long updated;

    private Long created;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return StringEscapeUtils.unescapeHtml(content);
    }

    public void setContent(String content) {
        this.content = StringEscapeUtils.escapeHtml(content);
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
