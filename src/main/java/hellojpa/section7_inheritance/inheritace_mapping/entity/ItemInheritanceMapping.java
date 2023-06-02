package hellojpa.section7_inheritance.inheritace_mapping.entity;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn // 기본값 : 들어온 자식 엔티티명 들어간다
//public class ItemInheritanceMapping {
public abstract class ItemInheritanceMapping {

    @Id @GeneratedValue
    @Column(name = "IIM_ID")
    private Long id;

    private String name;
    private int price;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
