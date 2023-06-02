package hellojpa.section6_various_relation_mapping.many_to_one.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 다대일 연관관계의 역방향
 * */

@Entity
public class TeamVariousRelation {

    @Id
    @GeneratedValue
    @Column(name = "TVR_ID")
    private Long id;

    private String name;

    //주인에 의해 매핑이 되었다는 뜻
    @OneToMany(mappedBy = "teamVariousRelation")
    private List<MemberVariousRelation> memberVariousRelations = new ArrayList<>(); // new 초기화는 add null exception 안뜨게 한다

    //===========연관관계 편의 메서드===========//
    public void addMember(MemberVariousRelation memberVariousRelation){
        memberVariousRelation.setTeamVariousRelation(this);
        memberVariousRelations.add(memberVariousRelation);
    }

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

    public List<MemberVariousRelation> getMemberVariousRelations() {
        return memberVariousRelations;
    }

    public void setMemberVariousRelations(List<MemberVariousRelation> memberVariousRelations) {
        this.memberVariousRelations = memberVariousRelations;
    }
}
