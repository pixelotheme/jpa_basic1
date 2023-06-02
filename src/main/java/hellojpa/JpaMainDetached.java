package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 준영속 상태 (detached)
 * -dirty check 같은 영속성 컨텍스트의 기능 사용 x
 *
 * em.detach(entity)
 * 특정 엔티티만 준영속 상태로 전환
 * • em.clear()
 * 영속성 컨텍스트를 완전히 초기화
 * • em.close()
 * 영속성 컨텍스트를 종료
 *
 * */

public class JpaMainDetached {

    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = em.find(Member.class, 150L);
            member.setName("detached member");

            //변경감지기능 작동 x
//            em.detach(member);
            //영속성 컨텍스트를 모두 지운다
            em.clear();
            //쿼리가 2번 나간다
            Member member2 = em.find(Member.class, 150L);

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
