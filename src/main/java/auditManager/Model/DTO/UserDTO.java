package auditManager.Model.DTO;

import auditManager.Controller.Service.UserAuthority;
import auditManager.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Nettle on 2017/2/6.
 */
public class UserDTO {
    private Long id;
    private String username;
    private Boolean userSystem = false;
    private Boolean articleSystem = false;
    private Boolean posterSystem = false;
    private Boolean deleted = false;
    private Long lastVisit;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userSystem = UserAuthority.checkCurrentUserAuthority(UserAuthority.USER_SYSTEM, user);
        this.articleSystem = UserAuthority.checkCurrentUserAuthority(UserAuthority.ARTICLE_SYSTEM, user);
        this.posterSystem = UserAuthority.checkCurrentUserAuthority(UserAuthority.POSTER_SYSTEM, user);
        this.deleted = user.getDeleted();
        this.lastVisit = user.getLastVisit();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getUserSystem() {
        return userSystem;
    }

    public void setUserSystem(Boolean userSystem) {
        this.userSystem = userSystem;
    }

    public Boolean getArticleSystem() {
        return articleSystem;
    }

    public void setArticleSystem(Boolean articleSystem) {
        this.articleSystem = articleSystem;
    }

    public Boolean getPosterSystem() {
        return posterSystem;
    }

    public void setPosterSystem(Boolean posterSystem) {
        this.posterSystem = posterSystem;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Long lastVisit) {
        this.lastVisit = lastVisit;
    }
}
