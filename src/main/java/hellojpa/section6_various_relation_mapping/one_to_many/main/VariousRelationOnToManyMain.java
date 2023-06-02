package hellojpa.section6_various_relation_mapping.one_to_many.main;

import hellojpa.section6_various_relation_mapping.one_to_many.entity.MemberVariousRelationOneToMany;
import hellojpa.section6_various_relation_mapping.one_to_many.entity.TeamVariousRelationOneToMany;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 다양한 연관관계 매핑 - OneToMany 단방향 연관관계 주인일때
 *
 * 테이블에서 외래키 관리는 Member 에서 하지만
 * 객체에서 team 엔티티가 관리한다
 *
 * 1. member 테이블 insert
 * 2. team 테이블 에 insert 이후
 * 3. member 테이블에 해당하는 회원의 teaem_id 외래키를 update 한다
 *  update
 *         MemberVariousRelationOneToMany
 *     set
 *         TVROTM_ID=?
 *     where
 *         MVROTM_ID=?
 *
 * --------문제점
 * 1. 엔티티가 관리하는 외래 키가 다른 테이블에 있다
 * 2. 비즈니스 로직을 짜다보면 소스만으로 정확하게 구분할수 없다
 * 3. 연관관계 관리를 위해 추가로 UPDATE SQL 실행
 *
 * 일대다 단방향이더라도 이런 문제점으로인해
 * 다대일 양방향으로 설계하는것이 좋다
 * 즉 주인을 1 쪽이 아닌 N 쪽에 되게끔 사용하자
 * -> 객체적으로 약간 손해가 있더라도(필요없는 참조를 넣더라도) 다대일 양방향으로 가자
 *
 * -------------
 *
 * @JoinColumn을 꼭 사용해야 함. 그렇지 않으면 조인 테이블
 * 방식을 사용함(중간에 테이블을 하나 추가함)
 *
 * 각 테이블의 id 만 들어있는 테이블이 생겨
 * 중간테이블에서 관리하는 테이블이 있다
 *
 * --단점 테이블이 늘어나 성능상 문제, 관리의 난이도가 올라간다
 *
 * ======================
 * 일대다 양방향 - 스팩상 지원하는것은 아니고 편법을 사용
 *
 * //조회용 양방향 설정 - insert, update 를 하지 않게 처리한다
 * @ManyToOne
 * @JoinColumn(name = "TVROTM_ID",insertable = false, updatable = false)
 *
 * 다대일 주인 설정과 기본적으로 비슷하다
 * 대신 읽기 전용 매핑으로만 만들어 준다
 *
 *
 * ====================
 *
 *  * @ManyToOne - 주요 속성
 *  * • 다대일 관계 매핑
 *  *
 *  * mappedBy 연관관계의 주인 필드를 선택한다.
 *  * fetch : 글로벌 페치 전략을 설정한다. / 각 기본값 : - @ManyToOne=FetchType.EAGER - @OneToMany=FetchType.LAZY
 *  * cascade : 영속성 전이 기능을 사용한다.
 *  * targetEntity :
 *  * 연관된 엔티티의 타입 정보를 설정한다. 이 기능은 거의 사용하지 않는다.
 *  * 컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있다.
 *  *
 * */


public class VariousRelationOnToManyMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {


            MemberVariousRelationOneToMany member = new MemberVariousRelationOneToMany();
            member.setUsername("member1");
            em.persist(member);

            TeamVariousRelationOneToMany team = new TeamVariousRelationOneToMany();
            team.setName("TeamA");
            //팀테이블에 넣는것이 아니라 회원 테이블에 들어가야한다
            team.getMemberVariousRelations().add(member);
            em.persist(team);

            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다

            TeamVariousRelationOneToMany findTeam = em.find(TeamVariousRelationOneToMany.class, team.getId());
            for (MemberVariousRelationOneToMany member1 : findTeam.getMemberVariousRelations()) {
                System.out.println("member1.getUsername() = " + member1.getUsername());
            }


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        //코드가 모두 끝나면 닫기기
        emf.close();
    }
}
