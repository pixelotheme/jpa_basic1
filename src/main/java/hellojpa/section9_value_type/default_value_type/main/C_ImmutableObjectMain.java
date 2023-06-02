package hellojpa.section9_value_type.default_value_type.main;

import hellojpa.section9_value_type.default_value_type.entity.Address;
import hellojpa.section9_value_type.default_value_type.entity.MemberDefaultValueType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 값타입 - 값 타입과 불변 객체
 *
 * 값 타입은 복잡한 객체 세상을 조금이라도 단순화하려고
 * 만든 개념이다. 따라서 값 타입은 단순하고 안전하게 다
 * 룰 수 있어야 한다
 *
 * -----------------
 * 값 타입 공유 참조
 * • 임베디드 타입 같은 값 타입을 여러 엔티티에서 공유하면 위험함
 * • 부작용(side effect) 발생
 *
 * 공유 하고자 했다면 값 타입이 아닌 엔티티로 만들어 사용해야 한다
 *
 * ----
 * 값 타입 복사
 * • 값 타입의 실제 인스턴스인 값을 공유하는 것은 위험
 * • 대신 값(인스턴스)를 복사해서 사용
 *
 * -------------
 *
 * 객체 타입의 한계
 * • 항상 값을 복사해서 사용하면 공유 참조로 인해 발생하는 부작용을 피할 수 있다.
 * • 문제는 임베디드 타입처럼 직접 정의한 값 타입은 자바의 기본
 * 타입이 아니라 객체 타입이다.
 * • 자바 기본 타입에 값을 대입하면 값을 복사한다.
 * • 객체 타입은 참조 값을 직접 대입하는 것을 막을 방법이 없다.
 * • 객체의 공유 참조는 피할 수 없다.
 *
 * member2.setHomeAddress(어찌되었든 참조가 들어간다)
 *
 * - 기본 타입 은 a = b 로 값 자체를 복사 한다
 *
 * ============================== 해결
 *
 * 불변 객체
 * • 객체 타입을 수정할 수 없게 만들면 부작용을 원천 차단
 * • 값 타입은 불변 객체(immutable object)로 설계해야함
 * • 불변 객체: 생성 시점 이후 절대 값을 변경할 수 없는 객체
 * • 생성자로만 값을 설정하고 수정자(Setter)를 만들지 않으면 됨 - private 레벨로 만들어도 된다
 * • 참고: Integer, String은 자바가 제공하는 대표적인 불변 객체
 *
 * 불변이라는 작은 제약으로 부작용이라는 큰 재앙을 막을수 있다
 *
 * =====================
 *
 * 값 타입의 비교
 * • 동일성(identity) 비교: 인스턴스의 참조 값을 비교, == 사용
 * • 동등성(equivalence) 비교: 인스턴스의 값을 비교, equals() 사용
 * • 값 타입은 a.equals(b)를 사용해서 동등성 비교를 해야 함
 * • 값 타입의 equals() 메소드를 적절하게 재정의(주로 모든 필드 사용)
 *
 *
 *     @Override
 *     public boolean equals(Object o) {
 *         if (this == o) return true;
 *         if (!(o instanceof Address)) return false;
 *         Address address = (Address) o;
 *         return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
 *     }
 *
 *     @Override
 *     public int hashCode() {
 *         return Objects.hash(city, street, zipcode);
 *     }
 * */


public class C_ImmutableObjectMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Address address = new Address("city", "street", "10000");


            MemberDefaultValueType member1 = new MemberDefaultValueType();
            member1.setUsername("member1");
            member1.setHomeAddress(address);
            em.persist(member1);

            //공유 참조 안되게 새로 생성성 - 객체 타입의 한계
           Address copyAdr = new Address(address.getCity(), address.getStreet(), address.getZipcode());

            MemberDefaultValueType member2 = new MemberDefaultValueType();
            member2.setUsername("member2");
            member2.setHomeAddress(copyAdr);
            em.persist(member2);

            //member1 의 주소만 바꾸고 싶다 -값 타입 공유 참조 side effect
            //불변 객체로 만든다
//            member1.getHomeAddress().setCity("newCity");


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
