package ubb.dbsm.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tank")
@SoftDelete(columnName = "is_deleted")
public class Tank implements HasID<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tank_id")
    private int id;

    @Column(name = "tank_name")
    private String name;

    @Column(name = "made_date_year")
    private int yearOfProduction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @Version
    @Column(name = "version")
    private int version;

    @Column(name = "is_deleted", insertable = false, updatable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Override
    public Integer getId() { return id; }
}
