package hellojpa.section7_inheritance.inheritace_mapping.main;

import hellojpa.section7_inheritance.inheritace_mapping.entity.MemberInheritanceMapping;
import hellojpa.section7_inheritance.inheritace_mapping.entity.MovieInheritanceMapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

/**
 * 고급 매핑 - MappedSuperClass
 *
 * 상속 관계 매핑이 아닌
 *
 * 공통된 부모의 속성만 가져오는것
 * 공통된 매핑 정보가 필요할때 사용
 *
 * @MappedSuperclass
 *
 * 사용할 엔티티에서 상속 받으면 된다
 * member, team 에 설정
 *
 *
 * ==============
 * @MappedSuperclass
 * • 상속관계 매핑X
 * • 엔티티X, 테이블과 매핑X
 * • 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공 - 필드와 테이블의 매핑을 이야기 한다
 * • 조회, 검색 불가(em.find(BaseEntity.class) 불가)
 * • 직접 생성해서 사용할 일이 없으므로 추상 클래스 권장
 *
 * =====조건
 * @MappedSuperclass
 * • 테이블과 관계 없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할
 * • 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
 * • 참고: @Entity 클래스는 엔티티나 @MappedSuperclass로 지정한 클래스만 상속 가능
 *
 *
 * */


public class MappedSuperClassMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            MemberInheritanceMapping member = new MemberInheritanceMapping();
            member.setUsername("Kim");
            member.setCreateBy("Kim");
            member.setCreateDate(LocalDateTime.now());
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
