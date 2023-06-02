package hellojpa.section6_various_relation_mapping.ono_to_ono.main;

import hellojpa.section6_various_relation_mapping.ono_to_ono.entity.MemberVariousRelationOneToOne;
import hellojpa.section6_various_relation_mapping.ono_to_ono.entity.TeamVariousRelationOneToOne;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 다양한 연관관계 매핑 - OneToOne 연관관계
 *
 * 주 테이블이나 대상 테이블 중에 외래 키 선택 가능
 * • 주 테이블에 외래 키
 * • 대상 테이블에 외래 키
 * • 외래 키에 데이터베이스 유니크(UNI) 제약조건 추가
 *
 * -- 주 테이블 외래키 주인
 * 다대일(@ManyToOne) 단방향 매핑과 유사
 *
 * 다대일 양방향 매핑 처럼 외래 키가 있는 곳이 연관관계의 주인
 * • 반대편은 mappedBy 적용
 * -------------------
 * 일대일: 대상 테이블에 외래 키 단방향 - Locker테이블 에서 관리
 *
 * member 객체에서 locker 의member 외래 키를 매핑할수 없다
 * locker 객체 에서 locker에 있는 member_id 외래 키를 매핑할수 있을뿐
 * -> 자신의 테이블만 매핑 가능하다
 *
 * 단방향 관계는 JPA 지원X
 * • 양방향 관계는 지원
 *
 *
 * -----
 * -> 일대일: 대상 테이블에 외래 키 양방향 가능
 * locker 에서 locker 테이블 외래 키 관리
 *
 * 사실 일대일 주 테이블에 외래 키 양방향과 매핑 방법은 같음
 * 대상테이블에 외래 키가 들어가기만 하면 된다
 *
 *
 * --------------------------
 * 문제점
 *
 * member와 locker 가 있을때 db설계상 어떤 테이블을 주 테이블로 써도 가능하다
 *
 * - Locker가 주인인 일대일 단방향
 * DBA 입장에서 비즈니스 로직이 변경되어 하나의회원이 여러개의 locker를 가질수 있을때
 * 변경이 쉽다 - locker에 있는 외래 키의 제약조건만 제거하면 추가 가능
 *
 * - Member 테이블에서 locker 외래 키를 갖고 있다면
 * 변경 포인트가 많아진다 - 외래 키 를 여러개 들고 있어야 하는데 member가 중복된다
 * member 테이블 외래키 제거
 * locker 쪽에 다시 외래키 생성
 *
 * -> 다만 여러 회원이 하나의 locker를 가질수 있다면
 * member에 외래 키를 넣는것이 맞다
 *
 * ------- 성능
 * member 에 locker 외래 키가 있는것이 성능상 좋다
 * 한번에 locker 의 값을 가져올 수 있음
 *
 *---------------
 * -> 결론
 * 명확한 1:1 관계라면
 * Member 에서 외래 키를 갖고 간다
 *
 * -> 양방향 이라면 locker를 주인으로 지정하고
 * 양방향으로 매핑한다
 * ( = 대상 테이블에서 외래 키 양방향)
 *
 *================
 * 일대일 정리
 *
 * 주 테이블에 외래 키 - member
 * • 주 객체가 대상 객체의 참조를 가지는 것 처럼 주 테이블에 외래 키를 두고 대상 테이블을 찾음
 * • 객체지향 개발자 선호
 * • JPA 매핑 편리
 * • 장점: 주 테이블만 조회해도 대상 테이블에 데이터가 있는지 확인 가능
 * • 단점: 값이 없으면 외래 키에 null 허용 -> DBA 입장에서 좋지 않다
 *
 * 대상 테이블에 외래 키 - locker
 * • 대상 테이블에 외래 키가 존재
 * • 전통적인 데이터베이스 개발자 선호
 * • 장점: 주 테이블과 대상 테이블을 일대일에서 일대다 관계로 변경할 때 테이블 구조 유지
 * • 단점: 프록시 기능의 한계로 지연 로딩으로 설정해도 항상 즉시 로딩됨(프록시는 뒤에서 설명)
 * - fetch 조인 등으로 해결해야한다
 *
 *  *** 대상 테이블 외래키 단점 설명 ***
 * jpa 입장에서는 locker의 값이 있는지 확인해야한다
 * member 테이블에 외래 키가 있다면 프록시 상태에서 외래 키 값만 확인 가능
 *
 * - 하지만 locker 테이블에 member_id 외래키를 관리한다면
 * member 에서 access 할때 회원에 해당하는 locker의 값이 있는지
 * locker의 테이블을 확인해야 하기 때문에 프록시 상태여도 쿼리가 나간다
 * */


public class VariousRelationOneToOneMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {


            MemberVariousRelationOneToOne member = new MemberVariousRelationOneToOne();
            member.setUsername("member1");
            em.persist(member);

            TeamVariousRelationOneToOne team = new TeamVariousRelationOneToOne();
            team.setName("TeamA");
            //팀테이블에 넣는것이 아니라 회원 테이블에 들어가야한다
            em.persist(team);

            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다


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
