package hellojpa.section7_inheritance.inheritace_mapping.entity;

import javax.persistence.*;

@Entity
public class LockerInheritanceMapping {

    @Id @GeneratedValue
    @Column(name = "LIM_ID")
    private Long id;

    private String name;

    //주인이 아닐때
    @OneToOne(mappedBy = "lockerInheritanceMapping")
    private MemberInheritanceMapping memberInheritanceMapping;
}
