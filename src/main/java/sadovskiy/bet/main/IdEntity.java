package sadovskiy.bet.main;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class IdEntity implements Serializable {

    public IdEntity(){}

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Getter
    @Setter
    @Column(updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_US")
    private Calendar created;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "en_US")
    private Calendar updated;

    @PreUpdate
    protected void preUpdate() {
        updated = Calendar.getInstance();
    }

    @PrePersist
    protected void prePersist() {
        updated = Calendar.getInstance();
        created = Calendar.getInstance();
    }



}

