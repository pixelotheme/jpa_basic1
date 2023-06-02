package hellojpa.section7_inheritance.inheritace_mapping.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProductInheritanceMapping {

    @Id @GeneratedValue
    @Column(name = "MPIM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MIM_ID")
    private MemberInheritanceMapping memberInheritanceMapping;

    @ManyToOne
    @JoinColumn(name = "PIM_ID")
    private ProductInheritanceMapping productInheritanceMapping;

    private int count;
    private int price;

    private LocalDateTime orderDateTime;
}
