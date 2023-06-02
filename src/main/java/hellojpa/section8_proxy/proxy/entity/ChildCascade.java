package hellojpa.section8_proxy.proxy.entity;

import javax.persistence.*;

@Entity
public class ChildCascade {

    @Id // DB PK 와 매핑
    @GeneratedValue
    @Column(name = "CC_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "PC_ID")
    private ParentCascade parentCascade;

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

    public ParentCascade getParentCascade() {
        return parentCascade;
    }

    public void setParentCascade(ParentCascade parentCascade) {
        this.parentCascade = parentCascade;
    }
}
