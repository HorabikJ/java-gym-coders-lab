package pl.coderslab.javaGym.entity.confirmationEmail;

import lombok.*;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import java.time.ZonedDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table
public class ActivationEmailDetails implements ConfirmationEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Column
    private String param;

    @Column
    private ZonedDateTime sendTime;

    @Column
    private Integer minutesExpirationTime;

    public ActivationEmailDetails(User user, String param, ZonedDateTime sendTime,
                                  Integer minutesExpirationTime) {
        this.user = user;
        this.param = param;
        this.sendTime = sendTime;
        this.minutesExpirationTime = minutesExpirationTime;
    }
}
