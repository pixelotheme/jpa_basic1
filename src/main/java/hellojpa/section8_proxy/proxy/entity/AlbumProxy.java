package hellojpa.section8_proxy.proxy.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
public class AlbumProxy extends ItemProxy {

    private String artist;
}
