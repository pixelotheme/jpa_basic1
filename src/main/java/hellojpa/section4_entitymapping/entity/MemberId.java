package hellojpa.section4_entitymapping.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 기본키 매핑
 * • @Id
 * • @GeneratedValue
 *
 * 기본 키 매핑 방법
 * • 직접 할당: @Id만 사용
 * • 자동 생성(@GeneratedValue)
 *  • IDENTITY: 데이터베이스에 위임, MYSQL
 *  • SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용, ORACLE @SequenceGenerator 필요
 *  • TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용, @TableGenerator 필요
 *  • AUTO: 방언에 따라 자동 지정, 기본값 - 방언에 맞춰 자동 지정
 *
 *  ===============
 *  IDENTITY 전략 - 특징
 * • 기본 키 생성을 데이터베이스에 위임
 * • 주로 MySQL, PostgreSQL, SQL Server, DB2에서 사용(예: MySQL의 AUTO_ INCREMENT)
 * • JPA는 보통 트랜잭션 커밋 시점에 INSERT SQL 실행
 * • AUTO_ INCREMENT는 데이터베이스에 INSERT SQL을 실행 한 이후에 ID 값을 알 수 있음
 * • IDENTITY 전략은 em.persist() 시점에 즉시 INSERT SQL 실행하고 DB에서 식별자를 조회
 *
 * -------- 문제점
 *
 * 쿼리 동기화시 ID 값이 null 로 들어올떄 DB 에서 값을 넣어준다
 * -> id 값은 DB에 들어 갈때만 확인 가능
 * -> 영속성 컨텍스트의 1차캐시에 @ID 값에 들어갈 기본 키 값을 모른다
 * -> 그래서 IDENTITY 속성에서만 em.persist 할때 바로 insert 쿼리가 날라가고
 * DB에서 기본 키 값을 가져와 영속성 컨텍스트에 세팅해준다
 **** 다른 전략은 flush 호출시 날라간다
 *
 * -> jdbc driver에 insert 했을때 id 값을 리턴받는 로직이 있다
 * 따로 select 해서 가져오진 않는다
 * -> 모아서 insert 해주는 기능을 사용하진 못한다
 *
 * ========
 * SEQUENCE 전략 - 특징
 * • 데이터베이스 시퀀스는 유일한 값을 순서대로 생성하는 특별한 데이터베이스 오브젝트(예: 오라클 시퀀스)
 * • 오라클, PostgreSQL, DB2, H2 데이터베이스에서 사용
 *
 * 직접 매핑 지정 가능
 * @Entity
 * @SequenceGenerator(
 *  name = “MEMBER_SEQ_GENERATOR",
 *  sequenceName = “MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
 *  initialValue = 1, allocationSize = 1)
 * public class Member {
 *  @Id
 *  @GeneratedValue(strategy = GenerationType.SEQUENCE,
 *  generator = "MEMBER_SEQ_GENERATOR")
 *  private Long id;
 * }
 *
 * 속성
 * name : 식별자 생성기 이름  /기본값 :필수
 * sequenceName : 데이터베이스에 등록되어 있는 시퀀스 이름 /기본값 : hibernate_sequence
 * initialValue : DDL 생성 시에만 사용됨, 시퀀스 DDL을 생성할 때 처음 시작하는수를 지정한다. 기본값 : 1
 * allocationSize : 시퀀스 한 번 호출에 증가하는 수
 * (성능 최적화에 사용됨 데이터베이스 시퀀스 값이 하나씩 증가하도록 설정되어 있으면 이 값 을 반드시 1로 설정해야 한다)
 * 기본값 :50
 * catalog, schema : 데이터베이스 catalog, schema 이름
 *
 * ---------- 문제점
 *
 * 미리 db 에서 ID 시퀀스를 가져오는 select를 날린다
 * -> 성능문제가 생기는것이 아닌가?
 * ->최적화 : allocationSize를 사용 (기본값 : 50) 메모리에서 50개씩 미리 가져온다
 * -> 메모리에서 다쓰면 다시 select 해온다
 * -> 여러 web 서버가 있어도 동시성 이슈 없이 해결된다
 * ***
 *
 * DB 에서 seq 세팅이 start with 1 increment by 50 으로 생성된다 - 호출시마다 50 씩 늘어난다
 * jpa 로 persist 날릴때 seq call 을 2번 날린다
 * 첫번째 = 1
 * 두번째 = 51
 * -> 메모리에서 50까지 쓰려고 했는데 첫 호출시 1이 나오면 1번밖에 못쓰니
 * 한번더 호출한다 -> 51 까지 가져와 메모리에 저장해둔다
 * -> 웹서버 메모리에서는 1~ 51까지 쓴다
 * -> 여러 대 서버를 써도 가능한가?
 * 가능하다 -> 동시 호출 하더라도 호출한 서버마다 50 개씩 넣어줘 각각 값의 범위가 다르다
 * 물론 순서대로 들어가지는 않는다다 *
 *
 * ======================
 * TABLE 전략 - 잘 쓰진 않는다
 * • 키 생성 전용 테이블을 하나 만들어서 데이터베이스 시퀀스를 흉
 * 내내는 전략
 * • 장점: 모든 데이터베이스에 적용 가능
 * • 단점: 성능
 *
 *@Entity
 * @TableGenerator(
 *  name = "MEMBER_SEQ_GENERATOR",
 *  table = "MY_SEQUENCES",
 *  pkColumnValue = “MEMBER_SEQ", allocationSize = 1)
 * public class Member {
 *  @Id
 *  @GeneratedValue(strategy = GenerationType.TABLE,
 *  generator = "MEMBER_SEQ_GENERATOR")
 *  private Long id;
 *
 * 속성
 * name 식별자 생성기 이름 / 필수
 * table 키생성 테이블명 / hibernate_sequences
 * pkColumnName 시퀀스 컬럼명 sequence_name
 * valueColumnName 시퀀스 값 컬럼명 / next_val
 * pkColumnValue 키로 사용할 값 이름 / 엔티티 이름
 * initialValue 초기 값, 마지막으로 생성된 값이 기준이다. / 0
 * allocationSize 시퀀스 한 번 호출에 증가하는 수(성능 최적화에 사용됨) / 50
 * catalog, schema 데이터베이스 catalog, schema 이름
 * uniqueConstraints(DDL) 유니크 제약 조건을 지정할 수 있다.
 *
 * =============
 * 권장하는 식별자 전략
 * • 기본 키 제약 조건: null 아님, 유일, 변하면 안된다.
 * • 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자.
 * • 예를 들어 주민등록번호도 기본 키로 적절하기 않다.
 * • 권장: Long형 + 대체키 + 키 생성전략 사용 ()
 *
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class MemberId {

    @Id // DB PK 와 매핑
    @GeneratedValue(strategy = GenerationType.IDENTITY) //- id 값은 DB에 들어 갈때만 확인 가능
    private Long id;
    // int, long primitive type은 null 불가 0이 실제 순서인지 null 대신 들어간것인지 구분 불가
    // Integer 10억에서 한번 돈다, Long 으로 해도 전체적으로 부하가 크지않다
    // 오히러 Integer 에서 Long 으로 테이블을 바꾸는것이 더 어렵다

    @Column(name = "name") //컬럼명 명시 가능
    private String username;

    public MemberId(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
