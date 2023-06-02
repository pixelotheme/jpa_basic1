package hellojpa.section6_various_relation_mapping.many_to_many.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProductVariousRelationManyToMany {

    @Id @GeneratedValue
    @Column(name = "MPVRMTM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MVRMTM_ID")
    private MemberVariousRelationManyToMany memberVariousRelationManyToMany;

    @ManyToOne
    @JoinColumn(name = "PVRMTM_ID")
    private ProductVariousRelationManyToMany productVariousRelationManyToMany;

    private int count;
    private int price;

    private LocalDateTime orderDateTime;
}
