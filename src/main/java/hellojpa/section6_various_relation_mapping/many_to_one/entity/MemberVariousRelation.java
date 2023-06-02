package hellojpa.section6_various_relation_mapping.many_to_one.entity;

import javax.persistence.*;

/**
 * 다대일 연관관계
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class MemberVariousRelation {

    @Id // DB PK 와 매핑
    @GeneratedValue
    @Column(name = "MVR_ID")
    private Long id;

    @Column(name = "USERNAME") //컬럼명 명시 가능
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TVR_ID")
    private TeamVariousRelation teamVariousRelation;

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

    public TeamVariousRelation getTeamVariousRelation() {
        return teamVariousRelation;
    }

    public void setTeamVariousRelation(TeamVariousRelation teamVariousRelation) {
        this.teamVariousRelation = teamVariousRelation;
    }
//=========== 연관관계 편의 메서드
//    public void changeTeam(TeamRelation team) {
//        this.team = team;
//        team.getMembers().add(this);
//    }

}
