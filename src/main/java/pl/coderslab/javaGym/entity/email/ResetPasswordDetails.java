package pl.coderslab.javaGym.entity.email;

import lombok.*;
import pl.coderslab.javaGym.entity.user.User;

import javax.persistence.*;
import java.time.ZonedDateTime;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table
public class ResetPasswordDetails implements ConfirmationEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @OneToOne
    private User user;

    @NonNull
    @Column
    private String param;

    @NonNull
    @Column
    private ZonedDateTime sendTime;

    @NonNull
    @Column
    private Integer minutesExpirationTime;

}
