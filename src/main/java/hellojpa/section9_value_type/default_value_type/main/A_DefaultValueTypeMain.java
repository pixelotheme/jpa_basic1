package hellojpa.section9_value_type.default_value_type.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 값타입 - 기본값 타입
 *
 *--------------------
 * JPA의 데이터 타입 분류
 *
 * 엔티티 타입
 *  • @Entity로 정의하는 객체
 *  • 데이터가 변해도 식별자로 지속해서 추적 가능
 *  • 예) 회원 엔티티의 키나 나이 값을 변경해도 식별자로 인식 가능
 *
 * 값 타입
 *  • int, Integer, String처럼 단순히 값으로 사용하는 자바 기본 타입이나 객체
 *  • 식별자가 없고 값만 있으므로 변경시 추적 불가
 *  • 예) 숫자 100을 200으로 변경하면 완전히 다른 값으로 대체
 *
 *
 * -------------------------------
 *
 *값 타입 분류
 *
 * 기본값 타입
 *  • 자바 기본 타입(int, double)
 *  • 래퍼 클래스(Integer, Long)
 *  • String
 * 임베디드 타입(embedded type, 복합 값 타입) - x,y 좌표를 묶어 position 클래스로 만들어 쓸수 있다
 * 컬렉션 값 타입(collection value type) - 기본값 타입, 임베디드 타입을 넣을수 있다
 *
 *=======================================
 *
 * 기본값 타입
 * • 예): String name, int age
 * • 생명주기를 엔티티의 의존
 *   • 예) 회원을 삭제하면 이름, 나이 필드도 함께 삭제
 * • 값 타입은 공유하면X
 *   • 예) 회원 이름 변경시 다른 회원의 이름도 함께 변경되면 안됨
 *
 * --> 공유 되면 side effect 발생(부수 효과)
 *
 * -----------------
 *
 * 참고: 자바의 기본 타입은 절대 공유되지 않는다
 * • int, double 같은 기본 타입(primitive type)은 절대 공유되지 않음
 * • 기본 타입은 항상 값을 복사함
 * • Integer같은 래퍼 클래스나 String 같은 특수한 클래스는 공유 가능한 객체이지만
 *  값을 변경 할 수 없어 side effect 발생 여지 x
 *
 *  --> reference 를 가져가 공유된다
 *
 *
 *
 * */


public class A_DefaultValueTypeMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {


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
