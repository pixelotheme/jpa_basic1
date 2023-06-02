package hellojpa.section6_various_relation_mapping.many_to_many.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 일대일
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class MemberVariousRelationManyToMany {

    @Id // DB PK 와 매핑
    @GeneratedValue
    @Column(name = "MVRMTM_ID")
    private Long id;

    @Column(name = "USERNAME") //컬럼명 명시 가능
    private String username;

    //조회용 양방향 설정 - insert, update 를 하지 않게 처리한다
    @ManyToOne
    @JoinColumn(name = "TVRMTM_ID",insertable = false, updatable = false)
    private TeamVariousRelationManyToMany teamVariousRelationManyToMany;

    //N:1 관계와 비슷하다 - 회원에서 외래키 관리할때
    @OneToOne
    @JoinColumn(name = "LVRMTM_ID")
    private LockerVariousRelationManyToMany lockerVariousRelationManyToMany;

    //N:M 중간 테이블 생성
//    @ManyToMany
//    @JoinTable(name = "Member_Product_Many_To_Many")
//    private List<ProductVariousRelationManyToMany> productVariousRelationManyToManies = new ArrayList<>();

    //연결 엔티티를 통한 일대다 로 해결
    @OneToMany(mappedBy = "memberVariousRelationManyToMany")
    private List<MemberProductVariousRelationManyToMany> memberProductVariousRelationManyToMany = new ArrayList<>();


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
