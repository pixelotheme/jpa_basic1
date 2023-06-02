package hellojpa.section8_proxy.proxy.main;

import hellojpa.section8_proxy.proxy.entity.MemberProxy;
import hellojpa.section8_proxy.proxy.entity.TeamProxy;

import javax.persistence.*;
import java.util.List;

/**
 * 프록시, 연관관계 정리
 *
 * ---- 즉시 로딩, 지연 로딩
 *
 *
 * =======지연 로딩 =======
 *
 *  member Entity 에서
 *     @ManyToOne(fetch = FetchType.LAZY)
 *     @JoinColumn(name = "TP_ID")
 *     private TeamProxy teamProxy;
 *
 * 팀은 프록시로 조회된다
 *  System.out.println("m.getTeamProxy().getClass() = " + m.getTeamProxy().getClass());
 *  class hellojpa.section8_proxy.proxy.entity.TeamProxy$HibernateProxy$SDkCVbad
 *----------
 * m.getTeamProxy().getName() 실제 사용할때 초기화 된다
 *
 * ===========즉시 로딩 ==========
 *  @ManyToOne(fetch = FetchType.EAGER)
 *
 * class 로 조회해봐도 진짜 엔티티가 나온다
 *
 *
 * ====================
 * 프록시와 즉시로딩 주의
 * • 가급적 지연 로딩만 사용(특히 실무에서)
 * • 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생
 * • 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.
 * List<MemberProxy> members = em.createQuery("select m from MemberProxy m", MemberProxy.class)
 *                      .getResultList();
 *
 * --> member 조회후 eager 설정된 team 을 추가로 조회한다
 * --> LAZY 엿다면 프록시로 들어간다
 * -> 처음 쿼리 = member -> 1개
 * -> 각 member 마다의 team -> 1개씩 총 2개
 * -> 3번의 쿼리가 나간다
 *
 * --> 해결 fetch join 사용하여 상황에 따라 동적으로 같이 가져올 엔티티 지정
 * List<MemberProxy> members = em.createQuery
 *              ("select m from MemberProxy m join fetch m.teamProxy", MemberProxy.class)
 *              .getResultList();
 *
 *
 * • @ManyToOne, @OneToOne은 기본이 즉시 로딩
 *   -> LAZY로 설정
 * • @OneToMany, @ManyToMany는 기본이 지연 로딩
 *
 *==================
 * 지연 로딩 활용 - 실무
 * • 모든 연관관계에 지연 로딩을 사용해라!
 * • 실무에서 즉시 로딩을 사용하지 마라!
 * • JPQL fetch 조인이나, 엔티티 그래프 기능을 사용해라!
 * (뒤에서 설명)
 * • 즉시 로딩은 상상하지 못한 쿼리가 나간다
 * */


public class LazyEagerLoadingMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            TeamProxy teamProxy = new TeamProxy();
            teamProxy.setName("팀이름");
            em.persist(teamProxy);

            TeamProxy teamProxy2 = new TeamProxy();
            teamProxy2.setName("팀이름2");
            em.persist(teamProxy2);

            MemberProxy memberProxy = new MemberProxy();
            memberProxy.setUsername("회원이름");
            memberProxy.setTeamProxy(teamProxy);
            em.persist(memberProxy);

            MemberProxy memberProxy2 = new MemberProxy();
            memberProxy2.setUsername("회원이름2");
            memberProxy2.setTeamProxy(teamProxy2);
            em.persist(memberProxy2);

            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다

//            MemberProxy m = em.find(MemberProxy.class, memberProxy.getId());
//            System.out.println("m.getTeamProxy().getClass() = " + m.getTeamProxy().getClass());
//
//            System.out.println("====================");
//            System.out.println("m.getTeamProxy().getName() = " + m.getTeamProxy().getName());
//            System.out.println("====================");
            List<MemberProxy> members = em.createQuery("select m from MemberProxy m join fetch m.teamProxy", MemberProxy.class)
                    .getResultList();


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

    private static void printMember(MemberProxy memberProxy) {
        System.out.println("memberProxy.getUsername() = " + memberProxy.getUsername());
    }

    private static void printMemberAndTeam(MemberProxy memberProxy){
        String username = memberProxy.getUsername();
        System.out.println("username = " + username);

        TeamProxy teamProxy = memberProxy.getTeamProxy();
        System.out.println("teamProxy.getName() = " + teamProxy.getName());

    }
}
