package hellojpa.section5_relation_mapping.entity;

import javax.persistence.*;

/**
 * 연관관계 매핑 기초
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class MemberRelation {

    @Id // DB PK 와 매핑
//    @GeneratedValue(strategy = GenerationType.IDENTITY)// 속성 지정시 team과 member 시퀀스를 따로 사용
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME") //컬럼명 명시 가능
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private TeamRelation team;

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

    public TeamRelation getTeam() {
        return team;
    }
    public void setTeam(TeamRelation team){
        this.team = team;
    }
//=========== 연관관계 편의 메서드
//    public void changeTeam(TeamRelation team) {
//        this.team = team;
//        team.getMembers().add(this);
//    }


    @Override
    public String toString() {
        return "MemberRelation{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", team=" + team +
                '}';
    }
}
