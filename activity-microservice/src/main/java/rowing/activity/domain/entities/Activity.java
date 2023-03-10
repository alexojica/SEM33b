package rowing.activity.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rowing.activity.domain.utils.BaseEntity;
import rowing.commons.Position;
import rowing.commons.entities.ActivityDTO;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.time.Duration;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(indexes = {@Index(name = "id", columnList = "id")})
public abstract class Activity<T extends ActivityDTO> extends BaseEntity<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "date", nullable = false)
    private Date start;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "positions")
    @ElementCollection
    private List<Position> positions;

    @Column(name = "applicants")
    @ElementCollection
    private List<String> applicants;

    @Column(name = "boat_type")
    private String boatType;

    /**
     * Mapper that maps a dto to an activity.
     *
     * @param dto that contains activity information
     */
    public Activity(ActivityDTO dto) {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(
                context -> Duration.ofMillis(context.getSource()),
                Integer.class, Duration.class);
        mapper.getConfiguration().setSkipNullEnabled(true);
        if (dto.getId() == null) {
            // This id will change once the activity entity is saved, but it must be non-null
            this.id = UUID.randomUUID();
        } else {
            this.id = dto.getId();
        }
        this.name = dto.getName();
        this.owner = dto.getOwner();

        this.type = dto.getType();
        this.start = dto.getStart();
        this.location = dto.getLocation();
        this.positions = dto.getPositions();
        this.applicants = dto.getApplicants();
        this.boatType = dto.getBoatType();
    }

    /**
     * Add an applicant to the list of applicants.
     *
     * @param applicant to add
     */
    public void addApplicant(String applicant) {
        if (this.applicants == null) {
            this.applicants = new ArrayList<>();
        }
        this.applicants.add(applicant);
    }

    /**
     * Function that compresses an activity to a DTO.
     *
     * @return the dto containing the information that should be sent.
     */
    public ActivityDTO toDto() {
        return new ActivityDTO(
                this.id,
                this.owner,
                this.name,
                this.type,
                this.start,
                this.location,
                this.positions,
                this.applicants,
                this.boatType
                );
    }

    public abstract T getDto();
}
