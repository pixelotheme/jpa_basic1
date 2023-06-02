package hellojpa.section6_various_relation_mapping.many_to_many.entity;

import javax.persistence.*;

@Entity
public class LockerVariousRelationManyToMany {

    @Id @GeneratedValue
    @Column(name = "LVRMTM_ID")
    private Long id;

    private String name;

    //주인이 아닐때
    @OneToOne(mappedBy = "lockerVariousRelationManyToMany")
    private MemberVariousRelationManyToMany memberVariousRelationManyToMany;
}
