package hellojpa.section6_various_relation_mapping.ono_to_ono.entity;

import hellojpa.section6_various_relation_mapping.one_to_many.entity.MemberVariousRelationOneToMany;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 일대일
 * */

@Entity
public class TeamVariousRelationOneToOne {

    @Id
    @GeneratedValue
    @Column(name = "TVROTO_ID")
    private Long id;

    private String name;

    //연관관계 주인
    @OneToMany
    @JoinColumn(name = "TVROTO_ID")
    private List<MemberVariousRelationOneToOne> memberVariousRelationOneToOnes = new ArrayList<>(); // new 초기화는 add null exception 안뜨게 한다



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

}
