package hellojpa.section9_value_type.default_value_type.main;

import hellojpa.section9_value_type.default_value_type.entity.Address;
import hellojpa.section9_value_type.default_value_type.entity.AddressEntity;
import hellojpa.section9_value_type.default_value_type.entity.MemberDefaultValueType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

/**
 * 값타입 - 값 타입 컬렉션
 *
 * 값 타입 컬렉션
 * • 값 타입을 하나 이상 저장할 때 사용
 * • @ElementCollection, @CollectionTable 사용
 * • 관계형 데이터베이스는 컬렉션을 같은 테이블에 저장할 수 없다.
 * • 컬렉션을 저장하기 위한 별도의 테이블이 필요함
 *
 *
 *    @ElementCollection
 *     @CollectionTable(name = "FAVORITE_FOOD",
 *             joinColumns = @JoinColumn(name = "MDVT_ID"))
 *     @Column(name = "FOOD_NAME")// 값 1개 컬럼 이름만 지정하는것이라 가능함 - address는 전체이름지정이라 안된다
 *     private Set<String> favoriteFoods = new HashSet<>();
 *
 *     @ElementCollection//값타입 컬렉션 지정
 *     @CollectionTable(name = "ADDRESS",
 *             joinColumns = @JoinColumn(name = "MDVT_ID"))
 *     private List<Address> addressHistory = new ArrayList<>();
 *
 *
 * -----------------------------
 *
 * 엔티티의 필드인 값 타입 과 같이
 * 컬렉션또한 엔티티의 라이프 사이클에 의존한다
 *
 * • 참고: 값 타입 컬렉션은 영속성 전에(Cascade)
 * + 고아 객체 제거 기능을 필수로 가진다고 볼 수 있다.
 *
 *
 * -------------------------------------
 *
 * 값 타입 컬렉션의 제약사항
 *
 * • 값 타입은 엔티티와 다르게 식별자 개념이 없다.
 * • 값은 변경하면 추적이 어렵다.
 *
 * • 값 타입 컬렉션에 변경 사항이 발생하면, 주인 엔티티와 연관된 모든 데이터를 삭제하고,
 * 값 타입 컬렉션에 있는 현재 값을 모두 다시 저장한다.
 *
 * findMember.getAddressHistory().remove(new Address("old1", "street", "10000"));
 * findMember.getAddressHistory().add(new Address("newCity1", "street", "10000"));
 *
 * -> 식별자가 없어 어쩔수 없이 전체를 지워버리는 상황
 *
 * • 값 타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본 키를 구성해야 함
 * -> null 입력X, 중복 저장 불가
 *
 * --------------------------------
 *
 *값 타입 컬렉션 대안
 * • 실무에서는 상황에 따라 값 타입 컬렉션 대신에 일대다 관계를 고려
 * • 일대다 관계를 위한 엔티티를 만들고, 여기에서 값 타입을 사용
 * • 영속성 전이(Cascade) + 고아 객체 제거를 사용해서 값 타입 컬렉션 처럼 사용
 * • EX) AddressEntity
 *
 * 결국 1:N 단방향 매핑으로
 * member 에서 외래 키를 관리하지만
 * 실제 외래 키는 Address에 있다
 *
 * member 에 insert -> address update 된다다
 *--------------
 *
 * 정리
 *• 엔티티 타입의 특징
 *  • 식별자O
 *  • 생명 주기 관리
 *  • 공유
 *• 값 타입의 특징
 *  • 식별자X
 *  • 생명 주기를 엔티티에 의존
 *  • 공유하지 않는 것이 안전(복사해서 사용)
 *  • 불변 객체로 만드는 것이 안전
 *
 *  ------------------------------
 *
 * 값 타입은 정말 값 타입이라 판단될 때만 사용
 * 엔티티와 값 타입을 혼동해서 엔티티를 값 타입으로 만들면 안됨
 * 식별자가 필요하고, 지속해서 값을 추적, 변경해야 한다면 그것은 값 타입이 아닌 엔티티
 *
 *
 * */


public class D_CollectionValueTypeMain {

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


            member1.getFavoriteFoods().add("치킨");
            member1.getFavoriteFoods().add("족발");
            member1.getFavoriteFoods().add("피자");

            member1.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member1.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

            em.persist(member1);


            em.flush();
            em.clear();
            System.out.println("--------------------------------------------");
            MemberDefaultValueType findMember = em.find(MemberDefaultValueType.class, member1.getId());

            List<AddressEntity> addressHistory = findMember.getAddressHistory();
            for (AddressEntity a : addressHistory) {
                System.out.println("a.getCity() = " + a.getAddress().getCity());
            }

            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String f : favoriteFoods) {
                System.out.println("f = " + f);
            }

            //주소를 바꾸고 싶어도 immutable 하게 유지해야한다
//            findMember.getHomeAddress().setCity
            Address a = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newC",a.getStreet(),a.getZipcode()));

//            컬렉션 변경 : 치킨 -> 한식
//             set 컬렉션 타입이 String 이라 갈아 끼울수 없다
            System.out.println("============== FAVORITEFOODS =============");
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            //주소 컬렉션 변경 : 대상 object를 찾을때 equals 를 찾아온다 - 그래서 재정의가 필요
//            System.out.println("============== ADDRESS =============");
//            findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "10000"));
            findMember.getAddressHistory().add(new AddressEntity("newCity1", "street", "10000"));
            //엔티티로 변경후
            List<AddressEntity> addressHistory1 = findMember.getAddressHistory();



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
