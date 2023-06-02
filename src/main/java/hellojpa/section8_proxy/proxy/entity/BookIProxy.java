package hellojpa.section8_proxy.proxy.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
public class BookIProxy extends ItemProxy {

    private String author;
    private String isbn;
}
