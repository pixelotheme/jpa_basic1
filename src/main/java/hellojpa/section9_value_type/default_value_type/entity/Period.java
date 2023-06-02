package hellojpa.section9_value_type.default_value_type.entity;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable // 값타입 임을 jpa 에게 알려주는 어노테이션
public class Period {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    //메서드로 활용 가능
//    public boolean isWork(){
//
//    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
