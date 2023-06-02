package hellojpa.section10_JPQL1.main;

import hellojpa.section8_proxy.proxy.entity.MemberProxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * JPQL
 *
 * JPQL
 * • JPA를 사용하면 엔티티 객체를 중심으로 개발
 * • 문제는 검색 쿼리
 * • 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색
 * • 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
 * • 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요
 *
 * • JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공
 * • SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
 * • JPQL은 엔티티 객체를 대상으로 쿼리
 * • SQL은 데이터베이스 테이블을 대상으로 쿼리
 *
 * //검색
 *  List<MemberProxy> resultList = em.createQuery("select m from MemberProxy m where m.username like '%KIM%'"
 *                             , MemberProxy.class)
 *                            .getResultList();
 *
 *
 * ----
 * JPQL
 * • 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
 * • SQL을 추상화해서 특정 데이터베이스 SQL에 의존X
 * • JPQL을 한마디로 정의하면 객체 지향 SQL
 *
 *
 * ===========================
 *  Criteria 소개
 *
 * - JPQL 은 단순 string 을 sql 로 작성하는것
 * -> 동적 쿼리를 만들기 너무 어렵다
 *
 * • 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
 * • JPQL 빌더 역할
 * • JPA 공식 기능
 * • 단점: 너무 복잡하고 실용성이 없다.
 * • Criteria 대신에 QueryDSL 사용 권장
 *
 *  CriteriaBuilder cb = em.getCriteriaBuilder();
 *  CriteriaQuery<MemberProxy> query = cb.createQuery(MemberProxy.class);
 *
 *  Root<MemberProxy> m = query.from(MemberProxy.class);
 *
 *  CriteriaQuery<MemberProxy> cq = query.select(m);
 *
 *  String username = "sdg";
 *  if(username != null){
 *      cq = cq.where(cb.equal(m.get("username"), "kim"));
 *  }
 *  List<MemberProxy> resultList = em.createQuery(cq).getResultList();
 *
 * ========================
 *
 * QueryDSL 소개
 * • 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
 * • JPQL 빌더 역할
 * • 컴파일 시점에 문법 오류를 찾을 수 있음
 * • 동적쿼리 작성 편리함
 * • 단순하고 쉬움
 * • 실무 사용 권장
 *
 * //JPQL
 *  //select m from Member m where m.age > 18
 *
 * JPAFactoryQuery query = new JPAQueryFactory(em);
 *  QMember m = QMember.member;
 *  List<Member> list =
 *      query.selectFrom(m)
 *      .where(m.age.gt(18))
 *      .orderBy(m.name.desc())
 *      .fetch();
 *
 * =======================
 *
 * 네이티브 SQL 소개
 * • JPA가 제공하는 SQL을 직접 사용하는 기능
 * • JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
 * • 예) 오라클 CONNECT BY, 특정 DB만 사용하는 SQL 힌트
 *
 * == createNativeQuery 사용 ==
 *
 *  List<MemberProxy> resultList = em.createNativeQuery("select MP_ID, USERNAME, TP_ID from MEMBERPROXY",
 *              MemberProxy.class)
 *          .getResultList();
 *
 *  -> 네이티브 쿼리를 사용할때는 spring 에서 직접 사용하는것이 더 편할수도 있다
 *  ====================
 *
 *  JDBC 직접 사용, SpringJdbcTemplate 등
 *
 *  • JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링
 *    JdbcTemplate, 마이바티스등을 함께 사용 가능
 *
 *  • 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시 필요 - 주의점
 *  -> JPQL 관련 메서드로 직접 날릴때는 자동으로 flush가 발생하지만...
 *     spring 을 사용할 경우는 직접 해줘야 한다
 *
 *  • 예) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 플러시
 *
 *   flush 발생 시기 -> tx commit시점, JPA query 나갈때
 *
 *   em.persist()
 *
 *   em.fush() --- 중간에 flush 를 시킨뒤에 execute 시켜줘야 db에 반영된 데이터를 가져온다다
 *
    dbconn.executeQuery("select * from member");
 *
 * */


public class A_JPQLIntroMain {

    public static void main(String[] args){
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            //Criteria 사용 준비
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<MemberProxy> query = cb.createQuery(MemberProxy.class);
//
//            Root<MemberProxy> m = query.from(MemberProxy.class);
//
//            CriteriaQuery<MemberProxy> cq = query.select(m);
//
//            String username = "sdg";
//            if(username != null){
//                cq = cq.where(cb.equal(m.get("username"), "kim"));
//            }
//
//            List<MemberProxy> resultList = em.createQuery(cq).getResultList();
            //Criteria 종료 -------------------

            /*
            JPQL 시작 -----------------
            List<MemberProxy> resultList = em.createQuery("select m from MemberProxy m where m.username like '%KIM%'"
                            , MemberProxy.class)
                    .getResultList();
            JPQL 종료 -------------------
            */

            // createNativeQuery 사용 =============
            List<MemberProxy> resultList = em.createNativeQuery("select MP_ID, USERNAME, TP_ID from MEMBERPROXY",
                            MemberProxy.class)
                    .getResultList();
            // createNativeQuery 종료 ==============

            //===========JDBC 직접 사용

            /*
            * flush 발생 시기 -> tx commit시점, JPA query 나갈때
            *
            * em.persist()
            *
            * em.fush()
            *
            * dbconn.executeQuery("select * from member");
            * */


            for (MemberProxy memberProxy : resultList) {
                System.out.println("memberProxy.getUsername() = " + memberProxy.getUsername());
            }

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
