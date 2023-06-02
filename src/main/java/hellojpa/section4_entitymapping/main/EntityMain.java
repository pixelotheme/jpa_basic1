package hellojpa.section4_entitymapping.main;

import hellojpa.section4_entitymapping.entity.Member;
import hellojpa.section4_entitymapping.entity.RoleType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * hibernate.hbm2ddl.auto
 * 옵션 설명
 * create : 기존테이블 삭제 후 다시 생성 (DROP + CREATE)
 * create-drop : create와 같으나 종료시점에 테이블 DROP
 * update : 변경분만 반영(운영DB에는 사용하면 안됨)
 * validate : 엔티티와 테이블이 정상 매핑되었는지만 확인
 * none : 사용하지 않음
 *
 * 테스트 서버에서도 직접 테이블 넣어주는 방법을 쓰자
 * update 도 쓰지말자 - db 락걸릴수도 있다
 *
 * */
public class EntityMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = new Member();
            member.setId(1L);
            member.setUsername("A");
            member.setRoleType(RoleType.USER);

            Member member2 = new Member();
            member2.setId(2L);
            member2.setUsername("B");
            member2.setRoleType(RoleType.ADMIN);

            em.persist(member);
            em.persist(member2);
            em.flush();


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
