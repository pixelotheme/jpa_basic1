package hellojpa.section8_proxy.proxy.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 일대일
 * */

@Entity
public class TeamProxy extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "TP_ID")
    private Long id;

    private String name;

    //연관관계 주인
    @OneToMany(mappedBy = "teamProxy")
//    @JoinColumn(name = "MP_ID")
    private List<MemberProxy> memberProxies = new ArrayList<>(); // new 초기화는 add null exception 안뜨게 한다



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

    public List<MemberProxy> getMemberProxies() {
        return memberProxies;
    }

    public void setMemberProxies(List<MemberProxy> memberProxies) {
        this.memberProxies = memberProxies;
    }
}
