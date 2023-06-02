package hellojpa.section6_various_relation_mapping.many_to_one.main;

import hellojpa.section6_various_relation_mapping.many_to_one.entity.MemberVariousRelation;
import hellojpa.section6_various_relation_mapping.many_to_one.entity.TeamVariousRelation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 다양한 연관관계 매핑
 *
 * @ManyToOne - 주요 속성
 * • 다대일 관계 매핑
 *
 * optional : false로 설정하면 연관된 엔티티가 항상 있어야 한다. / 기본값 : TRUE
 * fetch : 글로벌 페치 전략을 설정한다. / 각 기본값 : - @ManyToOne=FetchType.EAGER - @OneToMany=FetchType.LAZY
 * cascade : 영속성 전이 기능을 사용한다.
 * targetEntity :
 * 연관된 엔티티의 타입 정보를 설정한다. 이 기능은 거의 사용하지 않는다.
 * 컬렉션을 사용해도 제네릭으로 타입 정보를 알 수 있다.
 *
 * */
public class VariousRelationMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            TeamVariousRelation team = new TeamVariousRelation();
            team.setName("TeamA");
            em.persist(team);

            MemberVariousRelation member = new MemberVariousRelation();
            member.setUsername("member1");
            member.setTeamVariousRelation(team);
            em.persist(member);

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
