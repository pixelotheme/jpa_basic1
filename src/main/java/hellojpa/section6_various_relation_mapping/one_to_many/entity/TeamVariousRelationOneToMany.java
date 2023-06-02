package hellojpa.section6_various_relation_mapping.one_to_many.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 일대다 연관관계 - 주인일경우
 *
 * 테이블에서 외래키 관리는 Member 에서 하지만
 * 객체에서 team 엔티티가 관리한다
 *
 * */

@Entity
public class TeamVariousRelationOneToMany {

    @Id
    @GeneratedValue
    @Column(name = "TVROTM_ID")
    private Long id;

    private String name;

    //연관관계 주인 - JoinColumn을 뺄경우 중간 테이블이 생겨 연관관계를 세팅한다
    @OneToMany
//    @JoinColumn(name = "TVROTM_ID") // --test
    private List<MemberVariousRelationOneToMany> memberVariousRelationOneToManies = new ArrayList<>(); // new 초기화는 add null exception 안뜨게 한다


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

    public List<MemberVariousRelationOneToMany> getMemberVariousRelations() {
        return memberVariousRelationOneToManies;
    }

    public void setMemberVariousRelations(List<MemberVariousRelationOneToMany> memberVariousRelationOneToManies) {
        this.memberVariousRelationOneToManies = memberVariousRelationOneToManies;
    }
}
