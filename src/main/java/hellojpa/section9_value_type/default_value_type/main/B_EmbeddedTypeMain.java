package hellojpa.section9_value_type.default_value_type.main;

import hellojpa.section9_value_type.default_value_type.entity.Address;
import hellojpa.section9_value_type.default_value_type.entity.MemberDefaultValueType;
import hellojpa.section9_value_type.default_value_type.entity.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 값타입 - 임베디드 타입(복합 값 타입) = 내장 타입
 *
 *
 * --------------
 *
 * 임베디드 타입
 * • 새로운 값 타입을 직접 정의할 수 있음
 * • JPA는 임베디드 타입(embedded type)이라 함
 * • 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함 (composite)
 * • int, String과 같은 값 타입
 *
 *-----------------------------
 * 엔티티 예제
 * -Member-
 *  id
 *  name
 *  startDate
 *  endDate
 *  city
 *  street
 *  zipcode
 *
 * ------------
 * 임베디드 타입 사용법
 * • @Embeddable: 값 타입을 정의하는 곳에 표시
 * • @Embedded: 값 타입을 사용하는 곳에 표시
 * • 기본 생성자 필수
 *
 * -------------
 *
 * 임베디드 타입의 장점
 * • 재사용
 * • 높은 응집도
 * • Period.isWork()처럼 해당 값 타입만 사용하는 의미 있는 메소드를 만들 수 있음
 * • 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존함
 *  값 타입 : 엔티티 안의 필드라고 생각하면 편하다
 *
 *------------
 *
 * 임베디드 타입과 테이블 매핑
 *  • 임베디드 타입은 엔티티의 값일 뿐이다.
 *  • 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다.
 *  • 객체와 테이블을 아주 세밀하게(find-grained) 매핑하는 것이 가능
 *  • 잘 설계한 ORM 애플리케이션은 매핑한 테이블의 수보다 클래스의 수가 더 많음
 *
 * --> 주소 라고 표현 -> 세부적인 임베디드 타입으로 매핑
 *
 * 모든 곳에서 써야하는가에 보면 그렇지는 않다 적절한 곳에 사용하자자 *
 *
 * ========================
 *
 * 임베디드 타입과 연관관계
 *
 * 임베디드 타입은 - 엔티티 를 가질수 있다 - id 값만 외래 키로 가지고 있으면 되니 어렵지 않다
 *
 * ================
 *
 * @AttributeOverride: 속성 재정의
 * • 한 엔티티에서 같은 값 타입을 사용하면?
 * • 컬럼 명이 중복됨
 * • @AttributeOverrides, @AttributeOverride를 사용해서
 * 컬러 명 속성을 재정의
 *
 *     @AttributeOverrides({
 *             @AttributeOverride(name="city",
 *                     column=@Column(name="WORK_CITY")),
 *             @AttributeOverride(name="street",
 *                     column=@Column(name="WORK_STREET")),
 *             @AttributeOverride(name="zipcode",
 *                     column=@Column(name="WORK_ZIPCODE"))
 *     })
 *
 * ========================
 *
 *
 * */


public class B_EmbeddedTypeMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            MemberDefaultValueType member = new MemberDefaultValueType();
            member.setUsername("hello");
            member.setHomeAddress(new Address("city", "street", "zip"));
            member.setWorkPeriod(new Period());

            em.persist(member);
            em.flush();
            em.clear(); // flush 는 영속성 컨텍스트를 초기화 하지 않는다

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


}
