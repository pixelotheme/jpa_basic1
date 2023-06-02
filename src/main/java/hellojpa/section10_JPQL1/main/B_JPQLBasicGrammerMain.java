package hellojpa.section10_JPQL1.main;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * JPQL - 기본 문법
 *
 * JPQL 소개
 * • JPQL은 객체지향 쿼리 언어다.따라서
 * 테이블을 대상으로 쿼리하는 것이 아니라 엔티티 객체를 대상으로 쿼리한다.
 * • JPQL은 SQL을 추상화해서 특정데이터베이스 SQL에 의존하지 않는다.
 * • JPQL은 결국 SQL로 변환된다.
 *
 * =======================
 *
 *
 * */


public class B_JPQLBasicGrammerMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {


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
