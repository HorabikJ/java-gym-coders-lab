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
public class ResetPasswordEmailDetails implements ConfirmationEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE)
    private User user;

    @Column
    private String param;

    @Column
    private LocalDateTime sendTime;

    @Column
    private Integer minutesExpirationTime;

    public ResetPasswordEmailDetails(User user, String param, LocalDateTime sendTime,
                                     Integer minutesExpirationTime) {
        this.user = user;
        this.param = param;
        this.sendTime = sendTime;
        this.minutesExpirationTime = minutesExpirationTime;
    }
}
