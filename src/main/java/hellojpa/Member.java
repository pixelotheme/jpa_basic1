package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // jpa 관리 대상 지정 어노테이션
//@Table(name = "MBR") // 직접 테이블 명시할 수 있다
public class Member {
    @Id // DB PK 와 매핑
    private Long id;

//    @Column(name = "username") 컬럼명 명시 가능
    private String name;

    public Member(){

    }

    public Member(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
