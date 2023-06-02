package hellojpa.section5_relation_mapping.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TeamRelation {

    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    //주인에 의해 매핑이 되었다는 뜻
    @OneToMany(mappedBy = "team")
    private List<MemberRelation> members = new ArrayList<>(); // new 초기화는 add null exception 안뜨게 한다

    //===========연관관계 편의 메서드===========//
    public void addMember(MemberRelation member){
        member.setTeam(this);
        members.add(member);
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

    public List<MemberRelation> getMembers() {
        return members;
    }

    public void setMembers(List<MemberRelation> members) {
        this.members = members;
    }

}
