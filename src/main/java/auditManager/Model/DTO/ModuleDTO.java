package auditManager.Model.DTO;

import auditManager.Model.Article;
import auditManager.Model.Module;
import auditManager.Model.User;

/**
 * Created by Nettle on 2017/2/8.
 */
public class ModuleDTO {
    private Long id;
    private String name;
    private String content;
    private Long updated;

    public ModuleDTO() {}

    public ModuleDTO(Module module) {
        this.id = module.getId();
        this.name = module.getName();
        this.updated = module.getUpdated();
    }

    public ModuleDTO(Module module, Article article) {
        this.id = module.getId();
        this.name = module.getName();
        this.content = article.getContent();
        this.updated = article.getUpdated();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

}
