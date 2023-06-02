package hellojpa.section6_various_relation_mapping.ono_to_ono.entity;

import hellojpa.section6_various_relation_mapping.one_to_many.entity.TeamVariousRelationOneToMany;

import javax.persistence.*;

/**
 * 일대일
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class MemberVariousRelationOneToOne {

    @Id // DB PK 와 매핑
    @GeneratedValue
    @Column(name = "MVROTO_ID")
    private Long id;

    @Column(name = "USERNAME") //컬럼명 명시 가능
    private String username;

    //조회용 양방향 설정 - insert, update 를 하지 않게 처리한다
    @ManyToOne
    @JoinColumn(name = "TVROTO_ID",insertable = false, updatable = false)
    private TeamVariousRelationOneToOne teamVariousRelationOneToOne;

    //N:1 관계와 비슷하다 - 회원에서 외래키 관리할때
    @OneToOne
    @JoinColumn(name = "LVROTO_ID")
    private LockerVariousRelationOneToOne lockerVariousRelationOneToOne;

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
