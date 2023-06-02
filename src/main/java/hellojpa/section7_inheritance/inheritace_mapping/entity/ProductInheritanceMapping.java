package hellojpa.section7_inheritance.inheritace_mapping.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductInheritanceMapping {

    @Id
    @GeneratedValue
    @Column(name = "PIM_ID")
    private Long id;

    private String name;

    //N:M 양방향 테이블 전략
//    @ManyToMany(mappedBy = "productVariousRelationManyToManies")
//    private List<MemberVariousRelationManyToMany> memberVariousRelationManyToManies = new ArrayList<>();

    @OneToMany(mappedBy = "productInheritanceMapping")
    private List<MemberProductInheritanceMapping> memberProductInheritanceMappings = new ArrayList<>();

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
}
