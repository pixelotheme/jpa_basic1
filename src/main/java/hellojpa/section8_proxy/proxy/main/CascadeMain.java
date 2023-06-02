package hellojpa.section8_proxy.proxy.main;

import hellojpa.section8_proxy.proxy.entity.ChildCascade;
import hellojpa.section8_proxy.proxy.entity.MemberProxy;
import hellojpa.section8_proxy.proxy.entity.ParentCascade;
import hellojpa.section8_proxy.proxy.entity.TeamProxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 프록시, 연관관계 정리
 *
 * ---- 영속성 전이 : CASCADE
 *
 * ** 연관관계, 즉시 로딩, 지연 로딩과 전혀 관계 없다~
 *
 * ** 하나의 부모가 자식들을 관리할 떄
 * 게시판과 첨부파일 - 첨부파일의 경로는 한개의 게시판이 관리한다
 * -> 이럴때도 첨부파일을 다른 게시판에서도 관리한다면 쓰면 안된다
 * -> 즉 단일 엔티티에 종속적일때 가능하다
 *
 *
 * ===================
 * 영속성 전이: CASCADE
 * • 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 만들도 싶을 때
 * • 예: 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장.
 *
 * parent 엔티티
 *     @OneToMany(mappedBy = "parentCascade", cascade = CascadeType.ALL)
 *     private List<ChildCascade> childCascades = new ArrayList<>();
 *
 *  em.persist(parentCascade);
 *
 *  연관관계 주인은 child 이지만 모두 들어갔다
 *  parent 와 연관관계가 맺어진 child 에 insert 모두 시켜준다
 *
 *  ==================
 *  영속성 전이: CASCADE - 주의!
 * • 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
 * • 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐
 *
 * ==============
 * CASCADE의 종류
 * • ALL: 모두 적용
 * • PERSIST: 영속
 * • REMOVE: 삭제
 * • MERGE: 병합
 * • REFRESH: REFRESH
 * • DETACH: DETACH
 *
 * ======================================
 * 고아 객체
 * =======================================
 * 고아 객체
 * • 고아 객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
 * • orphanRemoval = true
 *
 * • Parent parent1 = em.find(Parent.class, id);
 *   parent1.getChildren().remove(0); //자식 엔티티를 컬렉션에서 제거
 *
 * • DELETE FROM CHILD WHERE ID=?
 *
 * ----------------------
 *
 * 고아 객체 - 주의
 * • 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제하는 기능
 *
 * • 참조하는 곳이 하나일 때 사용해야함!
 * • 특정 엔티티가 개인 소유할 때 사용
 *
 * • @OneToOne, @OneToMany만 가능
 * • 참고: 개념적으로 부모를 제거하면 자식은 고아가 된다.
 *   따라서 고아 객체 제거 기능을 활성화 하면, 부모를 제거할 때 자식도 함께 제거된다.
 *   이것은 CascadeType.REMOVE처럼 동작한다.
 *   - CascadeYpe.ALL 로 모두 적용시킬수도 있다
 *
 *   orphanRemoval는 자식 객체의 연관관계를 끊었을때 child 의 객체도 삭제된다
 *
 *
 * =========================
 * 영속성 전이 + 고아 객체, 생명주기
 * • CascadeType.ALL + orphanRemoval=true
 * • 스스로 생명주기를 관리하는 엔티티는 em.persist()로 영속화, em.remove()로 제거
 * • 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
 * • 도메인 주도 설계(DDD)의 Aggregate Root개념을 구현할 때 유용
 *
 * Aggregate Root : repository는 Aggregate Root 만 접근하고 하위 자식들은 접근하지 않는것이 낫다
 * */


public class CascadeMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            ChildCascade childCascade1 = new ChildCascade();
            ChildCascade childCascade2 = new ChildCascade();

            ParentCascade parentCascade = new ParentCascade();
            parentCascade.addChild(childCascade1);
            parentCascade.addChild(childCascade2);

//            em.persist(childCascade1);
//            em.persist(childCascade2);
            em.persist(parentCascade);

            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다

            ParentCascade parentCascade1 = em.find(ParentCascade.class, parentCascade.getId());
            parentCascade1.getChildCascades().remove(0);

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
