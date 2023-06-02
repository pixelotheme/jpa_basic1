package hellojpa.section6_various_relation_mapping.many_to_many.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ProductVariousRelationManyToMany {

    @Id
    @GeneratedValue
    @Column(name = "PVRMTM_ID")
    private Long id;

    private String name;

    //N:M 양방향 테이블 전략
//    @ManyToMany(mappedBy = "productVariousRelationManyToManies")
//    private List<MemberVariousRelationManyToMany> memberVariousRelationManyToManies = new ArrayList<>();

    @OneToMany(mappedBy = "productVariousRelationManyToMany")
    private List<MemberProductVariousRelationManyToMany> memberProductVariousRelationManyToManies = new ArrayList<>();

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
