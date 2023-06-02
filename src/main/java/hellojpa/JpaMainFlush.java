package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 플러시(flush)
 * 영속성 컨텍스트의 SQL 들을 db에 전송하는 역할
 * 플러시 발생시
 * 1. 변경감지
 * 2. 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
 * 3. 쿼리를 데이터베이스에 전송
 *
 * 영속성 컨텍스트를 플러시하는 방법
 * 1.em.flush - 직접호출
 * 2.트랜잭션 커밋 - 플러시 자동 호출
 * 3.JPQL 쿼리 실행 - 플러시 자동 호출
 * -> 이전에 persist 만 해둔 엔티티들을 강제로 flush 시켜야 JPQL로 검색했을때
 * 영속성 컨텍스트에만 있는 엔티티들이 db에 반영되어 검색된다
 * 그래서 JPA에서 기본 전략이 flush를 강제로 호출시키는것
 *
 * 거의 쓰지 않는다 - 필요없는 커밋을 줄이기위해 사용할수 있다
 * em.setFlushMode(FlushModeType.COMMIT) - 커밋할 때만 플러시
 * • FlushModeType.AUTO -> 커밋이나 쿼리를 실행할 떄 플러시
 *
 * 플러시 발생해도 1차캐시가 지워지는것이 아니다
 * 쓰기지연 sql 저장소에 있는 쿼리들이 db에 동기화 되는 과정일 뿐
 *
 * 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화 하면 됨
 * -> 영속성 컨텍스트의 시작과 끝을 트랜잭션 범위와 같이 써야한다
 * -> 커밋 전에
 * */

public class JpaMainFlush {

    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member(200L, "member200");

            em.persist(member);
            //미리 db 에 반영하고 싶을때
            em.flush();
            System.out.println("===================");

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
