package auditManager.Model.DTO;

import auditManager.Model.Theme;

/**
 * Created by Nettle on 2017/2/6.
 */
public class ThemeDTO {
    private Long id;
    private String name;
    private Long father;
    private Long module;
    private Boolean hide;
    private Boolean unmodifiable;

    public ThemeDTO() {}

    public ThemeDTO(Theme theme) {
        this.id = theme.getId();
        this.name = theme.getName();
        this.father = theme.getFather();
        this.hide = theme.getHide();
        this.unmodifiable = theme.getUnmodifiable();
        this.module = theme.getModule();
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

    public Long getFather() {
        return father;
    }

    public void setFather(Long father) {
        this.father = father;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public Boolean getUnmodifiable() {
        return unmodifiable;
    }

    public void setUnmodifiable(Boolean unmodifiable) {
        this.unmodifiable = unmodifiable;
    }

    public Long getModule() {
        return module;
    }

    public void setModule(Long module) {
        this.module = module;
    }
}
