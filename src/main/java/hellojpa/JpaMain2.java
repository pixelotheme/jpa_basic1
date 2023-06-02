package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 영속성 관리 - 영속성 컨텍스트
 *
 *  1차 캐시 : @ID - 키 , Entity - 값
 *  -> ID : PK 값, Entity : Member 객체
 *
 * 조회 -> 영속성 컨텍스트 안의 1차 캐시 조회 -> DB 조회 -> 1차 캐시에 저장 -> 반환
 *
 * 영속성 컨텍스트의 1차 캐시 는 하나의 트랜잭션 단위에서만 생명주기가 지속된다
 * -> 성능의 이점을 크게 느끼는 상황이 없을수도 있다 - 비즈니스 로직이 커질수록 이점이 있다
 *
 * 전체 공유는 2차 캐시에서 한다
 *
 * 영속 엔티티 동일성 보장 -> java 컬렉션과 같다
 * -> 같은 객체를 조회했을때 == 참조값 비교가 같다
 * -> 1차 캐시로 반복 가능한 읽기(REPEATABLE READ)등급의
 * 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공
 *
 * ====================
 * 트랜잭션을 지원하는 쓰기지연 기능
 * commit 직전에 SQL 쓰기지연 저장소에 저장된 쿼리 flush 한다
 * -> 버퍼링 기능을 사용할수 있다 -> 최적화할 수 있는 여지가 생긴다
 * hibernate.jdbc.batch_size - 10개만큼 모아서 쿼리를 날린다
 *
 * =============
 * 변경감지 - java 컬렉션과 같이 List 에서 값을 꺼내 변경한경우 set으로 넣지 않는다
 * - 주소값으로 같이 변경되기 떄문 - 오히려 다시 호출하는것이 문제
 *
 * em.persist로 다시 저장시키지 않는다
 *
 * tx.commit 시점에 flush 가 호출된다
 * 영속성 컨텍스트의 1차 캐시의 ID, Entity, 스냅샷 중에
 * ->위에서 설명하지 않았지만 처음 데이터를 가져올때 최초의 상태를 스냅샷에 저장시켜둔다
 * 변경된 Id 값의 Entity 를 스냅샷과 비교한다
 * 변경이 있는경우 update 쿼리를 sql 저장소에 저장시켜 commit 시 쿼리를 날린다
 *
 * */

public class JpaMain2 {

    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        //하나의 트랜잭션마다 하나를 생성한다
        // 하나의 쓰레드에만 써야하고 쓰레드간의 공유 x
        EntityManager em = emf.createEntityManager();
        //RDB 내부적으로 트랜잭션 안에서 실행되도록 설계되어있다
        //데이터 변경 작업은 항상 트랜잭션 단위 안에서 해야한다
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        //코드작성 구간
        try {

//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");

            //영속성 컨텍스트 - db(테이블) 와 애플리케이션(java) 사이에 계층이 존재한다
            System.out.println("===== BEFORE =========");
            //트랜잭션을 지원하는 쓰기 지연 기능
//            em.persist(member1);
//            em.persist(member2);
//            em.detach(member); 영속성 컨텍스트에서 삭제 - 준영속 상태
//            em.remove(member); db 테이블에서 삭제
            System.out.println("===== AFTER =========");

//            Member findMember = em.find(Member.class, 101L);
            // 101 ID 를 가져와 1차캐시에 저장시켜 쿼리가 1번만 나간다
//            Member findMember2 = em.find(Member.class, 101L);
//            System.out.println("findMember = " + (findMember == findMember2) );

            //변경감지
            Member findMember = em.find(Member.class, 150L);
            findMember.setName("ZZZZZ수정");

            //수정사항이 있을경우만 update 하게 해도
            // 수정사항이 있다면 당연히 update 쿼리가 날라간다
//            if(findMember.getName().equals("ZZZZZ수정")){
//                em.update(member); - update 메서드는 없다
//            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        //코드가 모두 끝나면 닫기기
        emf.close();
    }
}
