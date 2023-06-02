package hellojpa.section4_entitymapping.main;

import hellojpa.section4_entitymapping.entity.Member;
import hellojpa.section4_entitymapping.entity.MemberId;
import hellojpa.section4_entitymapping.entity.RoleType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 기본키 매핑 Main
 * */
public class IdMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            MemberId member1 = new MemberId();
            member1.setUsername("C");

            MemberId member2 = new MemberId();
            member2.setUsername("D");

            em.persist(member1);
            em.persist(member2);
            System.out.println("member1.getId() = " + member1.getId());
            System.out.println("member2.getId() = " + member2.getId());
            System.out.println("===================");

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
