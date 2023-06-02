package hellojpa.section4_entitymapping.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Table 속성
 *  name : 매핑할 테이블 이름 엔티티 이름을 사용
 * catalog : 데이터베이스 catalog 매핑
 * schema : 데이터베이스 schema 매핑
 * uniqueConstraints : (DDL) DDL 생성 시에 유니크 제약 조건 생성
 *
 * ============================
 *
 *  hibernate.hbm2ddl.auto
 *  옵션 설명
 *  create : 기존테이블 삭제 후 다시 생성 (DROP + CREATE)
 *  create-drop : create와 같으나 종료시점에 테이블 DROP
 *  update : 변경분만 반영(운영DB에는 사용하면 안됨)
 *  validate : 엔티티와 테이블이 정상 매핑되었는지만 확인
 *  none : 사용하지 않음
 *
 *  테스트 서버에서도 직접 테이블 넣어주는 방법을 쓰자
 *  update 도 쓰지말자 - db 락걸릴수도 있다
 *
 * DDL 생성기능
 * @Column(unique = true, length = 10)
 * 런타임에 영향을 주진 않는다 - 테이블 생성시에만 영향
 *
 * ========================
 *
 * 매핑 어노테이션 정리
 * @Column : 컬럼 매핑
 * @Temporal : 날짜 타입 매핑
 * @Enumerated : enum 타입 매핑
 * @Lob : BLOB, CLOB 매핑
 * @Transient : 특정 필드를 컬럼에 매핑하지 않음(매핑 무시)
 *
 * ======================
 * @Column 속성
 * insertable,updatable : 등록, 변경 가능 여부 / 기본값 : TRUE
 * nullable(DDL) : null 값의 허용 여부를 설정한다. false로 설정하면 DDL 생성 시에 not null 제약조건이 붙는다.
 * ----
 * unique(DDL) : @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약조건을 걸 때 사용한다.
 * -- unique 를 직접 컬럼에서 쓰면 제약조건 이름이 랜덤으로 생성된다 - 어떤컬럼을 위한것인지 구분 힘들다
 * -> @Table -> uniqueConstraints 속성을 사용한다
 *
 * length(DDL) 문자 길이 제약조건, String 타입에만 사용한다. 기본값 : 255
 * columnDefinition (DDL) : 데이터베이스 컬럼 정보를 직접 줄 수 있다.
 * ex) varchar(100) default ‘EMPTY' / 기본값 : 필드의 자바 타입과 방언 정보를 사용한다
 *
 *precision, scale(DDL)
 * BigDecimal 타입에서 사용한다(BigInteger도 사용할 수 있다).
 * precision은 소수점을 포함한 전체 자 릿수를, scale은 소수의 자릿수
 * 다. 참고로 double, float 타입에는 적용되지 않는다. 아주 큰 숫자나
 * 정 밀한 소수를 다루어야 할 때만 사용한다.
 * precision=19,
 * scale=2
 *
 * ===============
 * @Enumerated - enum 타입 매핑시
 * value
 * • EnumType.ORDINAL: enum 순서를 데이터베이스에 저장
 * • EnumType.STRING: enum 이름을 데이터베이스에 저장
 * 기본값 : EnumType.ORDINAL
 * ORDINAL 사용 x - enum 중간에 추가된경우 0,1 의 순서가 0,1,2 로 변경되며
 * 기존 데이터 1 의 USER 였던 데이터가 1의 GUEST 의미로 변경된다
 *
 * =========================
 * @Temporal - 날짜 타입(java.util.Date, java.util.Calendar)을 매핑할 때 사용
 *
 * 참고: LocalDate, LocalDateTime을 사용할 때는 생략 가능(최신 하이버네이트 지원)
 *
 * value
 * • TemporalType.DATE: 날짜, 데이터베이스 date 타입과 매핑
 * (예: 2013–10–11)
 * • TemporalType.TIME: 시간, 데이터베이스 time 타입과 매핑
 * (예: 11:11:11)
 * • TemporalType.TIMESTAMP: 날짜와 시간, 데이터베이 스
 * timestamp 타입과 매핑(예: 2013–10–11 11:11:11)
 *
 * =================
 *@Lob
 * 데이터베이스 BLOB, CLOB 타입과 매핑
 * • @Lob에는 지정할 수 있는 속성이 없다.
 * • 매핑하는 필드 타입이 문자면 CLOB 매핑, 나머지는 BLOB 매핑
 * • CLOB: String, char[], java.sql.CLOB
 * • BLOB: byte[], java.sql. BLOB
 *
 * ============================
 * 기본 키 매핑
 *
 * */


@Entity // jpa 관리 대상 지정 어노테이션
public class Member {
    @Id // DB PK 와 매핑
    private Long id;

    @Column(name = "name") //컬럼명 명시 가능
    private String username;

    private BigDecimal age;

    //enum 사용을위 한 어노테이션
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    //db 는 date - 날짜, time - 시간, timestamp - 날짜+시간
    //구분되어 타입을 지정해줘야한다
    //java date 타입 안에는 날짜 + 시간 모두있다
//    @Temporal(TemporalType.TIMESTAMP)
    //년월일
    private LocalDate createDate;

//    @Temporal(TemporalType.TIMESTAMP)
    //년월일 시간 까지 자동동
   private LocalDateTime lastModifiedDate;
    //vachar를 넘어서는 값을 Lob 을 사용한다
    @Lob // - 문자 타입이면 CLOB 으로 생성된다 / 바이트면 BLOB
    private String description;

    @Transient // db 와 관계없이 메모리에서만 사용
    private int temp;

    public Member(){

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

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
