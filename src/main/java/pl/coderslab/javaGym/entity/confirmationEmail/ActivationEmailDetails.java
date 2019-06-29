package pl.coderslab.javaGym.entity.confirmationEmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


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
    private LocalDateTime sendTime;

    @Column
    private Integer minutesExpirationTime;

    public ActivationEmailDetails(User user, String param, LocalDateTime sendTime,
                                  Integer minutesExpirationTime) {
        this.user = user;
        this.param = param;
        this.sendTime = sendTime;
        this.minutesExpirationTime = minutesExpirationTime;
    }
}
