package hellojpa.section6_various_relation_mapping.ono_to_ono.entity;

import javax.persistence.*;

@Entity
public class LockerVariousRelationOneToOne {

    @Id @GeneratedValue
    @Column(name = "LVROTO_ID")
    private Long id;

    private String name;

    //주인이 아닐때
    @OneToOne(mappedBy = "lockerVariousRelationOneToOne")
    private MemberVariousRelationOneToOne memberVariousRelationOneToOne;
}
