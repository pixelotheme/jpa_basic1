package hellojpa.section6_various_relation_mapping.one_to_many.entity;

import javax.persistence.*;

/**
 * 일대다의 역방향
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class MemberVariousRelationOneToMany {

    @Id // DB PK 와 매핑
    @GeneratedValue
    @Column(name = "MVROTM_ID")
    private Long id;

    @Column(name = "USERNAME") //컬럼명 명시 가능
    private String username;

    //조회용 양방향 설정 - insert, update 를 하지 않게 처리한다
    // 연관관계를 끊을 경우 테스트
    @ManyToOne
    @JoinColumn(name = "TVROTM_ID",insertable = false, updatable = false)
    private TeamVariousRelationOneToMany teamVariousRelationOneToMany;

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

//=========== 연관관계 편의 메서드
//    public void changeTeam(TeamRelation team) {
//        this.team = team;
//        team.getMembers().add(this);
//    }

}
