package hellojpa.section5_relation_mapping.main;

import hellojpa.section5_relation_mapping.entity.MemberRelation;
import hellojpa.section5_relation_mapping.entity.TeamRelation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 기본키 매핑 Main
 * 
 * 양방향 매핑시 문제점
 *
 * 순수한 객체상태에서 사용하기위해 주인과 반대편도 값을 넣어주어야 한다
 * 순수한 java 단 test 시에도 쓸수있게
 *
 * 까먹을수 있기때문에 연관관계 편의 메서드 사용
 *
 * 연관관계 편의 메서드는 둘중 한곳만 넣는다
 * -> 상황에 따라 정해준다
 *
 * ---------
 * toString, JSON 생성 라이브러리 에서 서로 연관관계 걸린 객체가 들어가
 * 무한 루프를 돌린다
 * -> 컨트롤러에서 직접 엔티티를 반환해버리면
 * JSON 으로 바꿀때 무한 루프가 돌아간다
 * -> 객체 서로를 호출하면서....
 *
 * lombok toString 자동생성은 쓰지 말거나 연관관계를 되어 있는 엔티티를 제거
 * --> 또는 엔티티를 절대 반환하지 말고 각 화면마다의 dto 를 만들어 주자
 *
 * 단방향 매핑으로 끝내자
 * -> 양방향으로 바꿀때는 단순히 속성들만 추가해주면 끝난다다
* * */
public class RelationMain3 {

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

            MemberRelation member = new MemberRelation();
            member.setUsername("member1");
            member.setTeam(team); // 연관관계 편의 메서드 적용
            em.persist(member);

            team.addMember(member); //주인이 아니라 insert가 되지 않는다
//            em.flush();
//            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다

            //flush, clear 안할경우 team.members 로 꺼낼때 1차캐시에 있는 값만 나온다
            //현재 team 에서 add 를 주석처리해서 team 컬렉션에 아무것도 없다

            TeamRelation findTeam = em.find(TeamRelation.class, team.getId());
            System.out.println("====================");
            System.out.println(findTeam);
            System.out.println("====================");
//            List<MemberRelation> members = findTeam.getMembers();
//
//            for (MemberRelation m : members) {
//                System.out.println("m.getUsername() = " + m.getUsername());
//            }

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
