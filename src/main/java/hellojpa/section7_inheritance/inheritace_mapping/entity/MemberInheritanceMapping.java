package hellojpa.section7_inheritance.inheritace_mapping.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 일대일
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class MemberInheritanceMapping extends BaseEntity{

    @Id // DB PK 와 매핑
    @GeneratedValue
    @Column(name = "MIM_ID")
    private Long id;

    @Column(name = "USERNAME") //컬럼명 명시 가능
    private String username;

    //조회용 양방향 설정 - insert, update 를 하지 않게 처리한다
    @ManyToOne
    @JoinColumn(name = "TIM_ID",insertable = false, updatable = false)
    private TeamInheritanceMapping teamInheritanceMapping;

    //N:1 관계와 비슷하다 - 회원에서 외래키 관리할때
    @OneToOne
    @JoinColumn(name = "LIM_ID")
    private LockerInheritanceMapping lockerInheritanceMapping;


    //연결 엔티티를 통한 일대다 로 해결
    @OneToMany(mappedBy = "memberInheritanceMapping")
    private List<MemberProductInheritanceMapping> memberProductInheritanceMapping = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
