package hellojpa.section8_proxy.proxy.main;


import hellojpa.section8_proxy.proxy.entity.MemberProxy;
import hellojpa.section8_proxy.proxy.entity.TeamProxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 프록시, 연관관계 정리
 *
 * 프록시 기초
 * • em.find() vs em.getReference()
 * • em.find(): 데이터베이스를 통해서 실제 엔티티 객체 조회
 * • em.getReference(): 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체 조회
 * -> 프록시로 나온다
 * class hellojpa.section8_proxy.proxy.entity.MemberProxy$HibernateProxy$jadr3Qiy
 *
 * --------------
 * 프록시 특징
 * • 실제 클래스를 상속 받아서 만들어짐
 * • 실제 클래스와 겉 모양이 같다.
 * • 사용하는 입장에서는 진짜 객체인지 프록시 객체인지 구분하지 않고 사용하면 됨(이론상)
 * • 프록시 객체는 실제 객체의 참조(target)를 보관
 * • 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메소드 호출
 *
 * ----------------
 * 프록시 객체의 초기화
 * Member member = em.getReference(Member.class, “id1”);
 * member.getName();
 * - 실제 가져와 쓰는 시점에 영속성 컨텍스트에 요청해 초기화한다
 *
 * ----------------------
 *
 * 프록시의 특징
 * • 프록시 객체는 처음 사용할 때 한 번만 초기화
 * • 프록시 객체를 초기화 할 때, 프록시 객체가 실제 엔티티로 바뀌는 것은 아님,
 *   초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
 *
 * • 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야함
 *   (== 비교 실패, 대신 instance of 사용)
 *   reference.getClass() == reference2.getClass(); - false - 각각 엔티티로 조회, getReference로 조회
 *   System.out.println("타입비교 : " + ( reference instanceof MemberProxy) ); true
 *   System.out.println("타입비교 : " + ( reference2 instanceof MemberProxy) ); true
 *
 *
 * • 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
 * -> 같은 트랜잭션 범위 안에서는 영속성 컨텍스트는 동일성이 보장되어야 한다
 * -> 반복 가능한 읽기 수준
 * -> m1 getReference로 먼저 가져온후 / m1m 을 find 로 가져올 경우
 * -> 동일성 보장 둘다 프록시로 반환된다
 * -> 한번 프록시로 반환되면 초기화 되어도 프록시로 나와 동일성을 보장한다
 *
 * ---> 결론 : 프록시, 엔티티 이던 동일성을 보장한다다
 *
 *
 * * • 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
 * (하이버네이트는 org.hibernate.LazyInitializationException 예외를 터트림)
 *
 * em.detach(reference2);
 * em.close(); // 영속성 컨텍스트를 강제로 닫을때도 같다
 *
 * 트랜잭션, 영속성컨텍스트의 시작과 끝을 대부분 맞추는데
 * 트랜잭션이 끝난후 프록시 초기화 할경우에 준영속 상태로 인한 에러 발생 가능성이 높다
 *
 * ====================================
 * 프록시 확인
 * • 프록시 인스턴스의 초기화 여부 확인
 *   emf.PersistenceUnitUtil.isLoaded(Object entity) - 팩토리 메니저에서 사용
 *
 * • 프록시 클래스 확인 방법
 *   entity.getClass().getName() 출력(..javasist.. or HibernateProxy…)
 *
 * • 프록시 강제 초기화
 *   org.hibernate.Hibernate.initialize(entity);
 *
 * • 참고: JPA 표준은 강제 초기화 없음
 *   강제 호출: member.getName()
 *
 * ======================
 *
 *
 * */


public class ProxyMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            TeamProxy teamProxy = new TeamProxy();
            teamProxy.setName("팀이름");
            em.persist(teamProxy);

            MemberProxy memberProxy = new MemberProxy();
            memberProxy.setUsername("회원이름");
            memberProxy.setTeamProxy(teamProxy);
            em.persist(memberProxy);

            MemberProxy memberProxy2 = new MemberProxy();
            memberProxy2.setUsername("회원이름");
            memberProxy2.setTeamProxy(teamProxy);
            em.persist(memberProxy2);

            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다

            MemberProxy reference = em.find(MemberProxy.class, memberProxy.getId());
            System.out.println("reference = " + reference.getClass());
            System.out.println("reference.getId() = " + reference.getId());
            System.out.println("reference.getUsername() = " + reference.getUsername());

            //== 비교 : true, instanceof 비교 : true
//            MemberProxy reference2 = em.find(MemberProxy.class, memberProxy2.getId());
            //== 비교 : false, instanceof 비교 : true
            MemberProxy reference2 = em.getReference(MemberProxy.class, memberProxy2.getId());
            System.out.println("reference2 = " + reference2.getClass());
            System.out.println("타입비교 : " + ( reference.getClass() == reference2.getClass()) );
            System.out.println("타입비교 : " + ( reference instanceof MemberProxy) );
            System.out.println("타입비교 : " + ( reference2 instanceof MemberProxy) );


            //준영속 상태로 만들어 초기화 불가
//            em.detach(reference2);
//            em.close(); // 영속성 컨텍스트를 강제로 닫을때도 같다
//            System.out.println("reference2.getUsername() = " + reference2.getUsername());

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        //코드가 모두 끝나면 닫기기
        emf.close();
    }

}
