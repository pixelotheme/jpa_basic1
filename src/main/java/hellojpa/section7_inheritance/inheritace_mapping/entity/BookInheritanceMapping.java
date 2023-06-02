package hellojpa.section7_inheritance.inheritace_mapping.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class BookInheritanceMapping extends ItemInheritanceMapping {

    private String author;
    private String isbn;
}
