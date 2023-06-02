package hellojpa.section7_inheritance.inheritace_mapping.main;

import hellojpa.section7_inheritance.inheritace_mapping.entity.MovieInheritanceMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 고급 매핑 - 상속관계 매핑
 *
 * -----부모클래스는 추상 클래스로 선언
 *
 * -----전략 고려 순서
 * 1. 조인
 * 2. 단일 테이블
 *
 * 상속관계 매핑
 * • 관계형 데이터베이스는 상속 관계X
 * • 슈퍼타입 서브타입 관계라는 모델링 기법이 객체 상속과 유사
 * • 상속관계 매핑: 객체의 상속과 구조와 DB의 슈퍼타입 서브타입 관계를 매핑
 *
 * 슈퍼타입 서브타입 논리 모델을 실제 물리 모델로 구현하는 방법
 * • 각각 테이블로 변환 -> 조인 전략
 * • 통합 테이블로 변환 -> 단일 테이블 전략
 * • 서브타입 테이블로 변환 -> 구현 클래스마다 테이블 전략
 *
 *주요 어노테이션
 * • @Inheritance(strategy=InheritanceType.XXX)
 *  • JOINED: 조인 전략
 *  • SINGLE_TABLE: 단일 테이블 전략
 *  • TABLE_PER_CLASS: 구현 클래스마다 테이블 전략
 * • @DiscriminatorColumn(name=“DTYPE”)
 * • @DiscriminatorValue(“XXX”)
 *
 * =======================
 * **  기본 정석 **
 *
 * 조인 전략 - dtype 으로 구분, 하위 테이블 PK,FK 로 상위 테이블 PK 를 가져와 쓴다(iteam_id)
 * • 장점
 *  • 테이블 정규화 - 조회시 ITEM 테이블만 바라보면 정보를 알수 있다
 *  • 외래 키 참조 무결성 제약조건 활용가능
 *  • 저장공간 효율화
 * • 단점
 *  • 조회시 조인을 많이 사용, 성능 저하 - 잘 맞추면 극복 가능
 *  • 조회 쿼리가 복잡함
 *  • 데이터 저장시 INSERT SQL 2번 호출 - 생각보다 큰 문제는 아니다
 *  - 테이블이 많아 복잡하다
 *
 * -----DTYPE 없어도 외래키로 구분할수도 있다 - 다만 있는것이 구분하기 훨씬 좋다
 *
 * 부모 엔티티
 * @Inheritance(strategy = InheritanceType.JOINED)
 * @DiscriminatorColumn // 기본값 : 들어온 자식 엔티티명 들어간다
 * 자식 엔티티
 * @DiscriminatorValue("A") -명칭 정할수 있다
 *
 *  =================
 *
 *  **    **
 *  단일 테이블 전략
 * • 장점
 *  • 조인이 필요 없으므로 일반적으로 조회 성능이 빠름
 *  • 조회 쿼리가 단순함
 * • 단점
 *  • 자식 엔티티가 매핑한 컬럼은 모두 null 허용
 *  • 단일 테이블에 모든 것을 저장하므로 테이블이 커질 수 있다. 상황에 따라서 조회 성능이 오히려 느려질 수 있다.
 *  -> 대부분 그런 상황이 없다다

 *  ------------DTYPE 필수 - 단일테이블 에서 각 데이터를 구분해야 한다
 *  -> 어노테이션 안써도 자동으로 들어간다
 *  * 부모 엔티티
 *  * @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
 *  * @DiscriminatorColumn // 기본값 : 들어온 자식 엔티티명 들어간다
 *  * 자식 엔티티
 *  * @DiscriminatorValue("A") -명칭 정할수 있다
 *
 *
 *  =====================
 *  구현 클래스마다 테이블 전략
 * • 이 전략은 데이터베이스 설계자와 ORM 전문가 둘 다 추천X
 * • 장점
 *  • 서브 타입을 명확하게 구분해서 처리할 때 효과적
 *  • not null 제약조건 사용 가능
 * • 단점
 *  • 여러 자식 테이블을 함께 조회할 때 성능이 느림(UNION SQL 필요)
 *  • 자식 테이블을 통합해서 쿼리하기 어려움
 *
 *    부모 타입으로 검색할 경우 UNION ALL로 자식테이블만 모두 묶어 찾아야 한다
 *    -> 부모테이블이 없다
 *
 *=============================================
 *
 * 실제 서비스에서 복잡도로 인해 상속관계를 써야하는가 생각해봐야한다
 * -> 데이터가 많을경우 테이블을 단순하게 유지해야한다
 * -> json 으로 말아 넣는방법도 생각 (NoSQL??)
 *
 * */


public class InheritanceMappingMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            MovieInheritanceMapping movie = new MovieInheritanceMapping();
            movie.setDirector("aaa");
            movie.setActor("bbbb");
            movie.setName("하하하하영화");
            movie.setPrice(10000);

            em.persist(movie);
            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다


            MovieInheritanceMapping findMovie = em.find(MovieInheritanceMapping.class, movie.getId());
            System.out.println();


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
