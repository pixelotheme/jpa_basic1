package hellojpa.section5_relation_mapping.main;

import hellojpa.section4_entitymapping.entity.MemberId;
import hellojpa.section5_relation_mapping.entity.MemberRelation;
import hellojpa.section5_relation_mapping.entity.TeamRelation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 기본키 매핑 Main - main3 에 설명
 * */
public class RelationMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            TeamRelation team = new TeamRelation();
            team.setName("TeamA");
            em.persist(team);

            TeamRelation team2 = new TeamRelation();
            team2.setName("TeamB");
            em.persist(team2);

            MemberRelation member = new MemberRelation();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다

            MemberRelation findMember = em.find(MemberRelation.class, member.getId());

            TeamRelation findTeam = findMember.getTeam();
            System.out.println("findTeam = " + findTeam.getName());

            //회원팀 변경
            TeamRelation newTeam = em.find(TeamRelation.class, team2.getId());
            findMember.setTeam(newTeam);

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
