package hellojpa.section6_various_relation_mapping.many_to_many.main;

import hellojpa.section6_various_relation_mapping.many_to_many.entity.MemberVariousRelationManyToMany;
import hellojpa.section6_various_relation_mapping.many_to_many.entity.TeamVariousRelationManyToMany;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 다양한 연관관계 매핑 - ManyToMany 연관관계
 *
 * ----결론은 일대다, 다대일 관계로 풀어내야한다
 * 연결 테이블을 만들어 활용한다
 *
 * 객체는 컬렉션을 사용해서 객체 2개로 다대다 관계 가능
 *
 * member 엔티티
 *    //N:M 중간 테이블 생성
 *     @ManyToMany
 *     @JoinTable(name = "Member_Product_Many_To_Many")
 *     private List<ProductVariousRelationManyToMany> productVariousRelationManyToManies = new ArrayList<>();
 *
 * product 거울 양방향 추가
 *     //양방향 만들기
 *     @ManyToMany(mappedBy = "productVariousRelationManyToManies")
 *     private List<MemberVariousRelationManyToMany> memberVariousRelationManyToManies = new ArrayList<>();
 *
 * ----------------------
 *
 * • 편리해 보이지만 실무에서 사용X
 * • 연결 테이블이 단순히 연결만 하고 끝나지 않음
 * • 주문시간, 수량 같은 데이터가 들어올 수 있음 - 추가하기 어렵다
 *
 * 생각하지 못한 쿼리들이 나간다
 *
 * ======해결
 *
 * 다대다 한계 극복
 * • 연결 테이블용 엔티티 추가(연결 테이블을 엔티티로 승격)
 * • @ManyToMany -> @OneToMany, @ManyToOne
 *
 * @Entity
 * public class MemberProductVariousRelationManyToMany {
 *
 *     @Id @GeneratedValue
 *     @Column(name = "MPVRMTM_ID")
 *     private Long id;
 *
 *     @ManyToOne
 *     @JoinColumn(name = "MVRMTM_ID")
 *     private MemberVariousRelationManyToMany memberVariousRelationManyToMany;
 *
 *     @ManyToOne
 *     @JoinColumn(name = "PVRMTM_ID")
 *     private ProductVariousRelationManyToMany productVariousRelationManyToMany;
 *
 *     private int count;
 *     private int price;
 *
 *     private LocalDateTime orderDateTime;
 * }
 *
 * --------또는 전통적인 방식
 *
 * member_product 테이블
 * member_id - PK, FK
 * product_id - PK, FK
 *
 * 각각의 FK 를 PK 로도 사용
 *
 * -> 하지만 각 테이블에 일관성있게
 * 의미없는 구분자 PK를 지정하여
 * 어딘가에 종속되지 않게 설정하는것이
 * 확장성에도 좋다
 *
 * */


public class VariousRelationManyToManyMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {


//            MemberVariousRelationManyToMany member = new MemberVariousRelationManyToMany();
//            member.setUsername("member1");
//            em.persist(member);
//
//            TeamVariousRelationManyToMany team = new TeamVariousRelationManyToMany();
//            team.setName("TeamA");
//            //팀테이블에 넣는것이 아니라 회원 테이블에 들어가야한다
//            em.persist(team);

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
