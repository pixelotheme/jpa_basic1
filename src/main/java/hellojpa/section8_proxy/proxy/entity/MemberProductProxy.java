package hellojpa.section8_proxy.proxy.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberProductProxy {

    @Id @GeneratedValue
    @Column(name = "MPP_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MP_ID")
    private MemberProxy memberProxy;

    @ManyToOne
    @JoinColumn(name = "PP_ID")
    private ProductProxy productProxy;

    private int count;
    private int price;

    private LocalDateTime orderDateTime;
}
