package hellojpa.section8_proxy.proxy.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ParentCascade {

    @Id // DB PK 와 매핑
    @GeneratedValue
    @Column(name = "PC_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parentCascade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildCascade> childCascades = new ArrayList<>();

    //========== 연관관계 편의 메서드 =============//
    public void addChild(ChildCascade childCascade){
        childCascades.add(childCascade);
        childCascade.setParentCascade(this);
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

    public List<ChildCascade> getChildCascades() {
        return childCascades;
    }

    public void setChildCascades(List<ChildCascade> childCascades) {
        this.childCascades = childCascades;
    }
}
