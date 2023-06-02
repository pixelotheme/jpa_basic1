package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
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

//            Member member = new Member();
//            member.setId(1L);
//            member.setName("helloA");
//            em.persist(member);
            //조회
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember = " + findMember.getName());
            System.out.println("findMember = " + findMember.getId());

            //JPQL -테이블이 아닌 객체를 대상으로 검색해온다
            // - 검색조건을 객체를 대상으로 지정하여 sql 에 의존적이지 않게 해준다
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(1) //1번부터
                    .setMaxResults(10) // 10개 가져와
                    .getResultList()
                    ;
            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }
            //삭제
//            em.remove(findMember);
            //수정 - persist 로 저장하지 않아도 된다
//            findMember.setName("helloJPA");

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
