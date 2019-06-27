package pl.coderslab.javaGym.entity.confirmationEmail;

import lombok.*;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class ResetPasswordEmailDetails implements ConfirmationEmail {

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

    public ResetPasswordEmailDetails(User user, String param, ZonedDateTime sendTime,
                                     Integer minutesExpirationTime) {
        this.user = user;
        this.param = param;
        this.sendTime = sendTime;
        this.minutesExpirationTime = minutesExpirationTime;
    }
}
