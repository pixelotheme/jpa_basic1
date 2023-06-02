package hellojpa.section7_inheritance.inheritace_mapping.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class AlbumInheritanceMapping extends ItemInheritanceMapping {

    private String artist;
}
