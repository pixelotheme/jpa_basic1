package hellojpa.section9_value_type.default_value_type.entity;

import javax.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    @Id
    @GeneratedValue
    @Column(name = "ADVT_ID")
    private Long id;

    // 엔티티로 한번 wrapping 해준다
    private Address address;

    public AddressEntity() {
    }

    public AddressEntity(Address address) {
        this.address = address;
    }

    public AddressEntity(String city, String street, String zipecode) {
        this.address = new Address(city, street, zipecode);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
