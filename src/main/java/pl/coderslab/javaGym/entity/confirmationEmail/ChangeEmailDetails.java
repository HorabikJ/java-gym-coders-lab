package pl.coderslab.javaGym.entity.confirmationEmail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class ChangeEmailDetails implements ConfirmationEmail {

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
    private String newEmail;

    @Column
    private Integer minutesExpirationTime;

    public ChangeEmailDetails(User user, String param, LocalDateTime sendTime,
                              String newEmail, Integer minutesExpirationTime) {
        this.user = user;
        this.param = param;
        this.sendTime = sendTime;
        this.newEmail = newEmail;
        this.minutesExpirationTime = minutesExpirationTime;
    }
}