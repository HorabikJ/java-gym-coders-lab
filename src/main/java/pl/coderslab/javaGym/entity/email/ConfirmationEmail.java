package pl.coderslab.javaGym.entity.email;

import pl.coderslab.javaGym.entity.user.User;

import java.time.LocalDateTime;

public interface ConfirmationEmail {

    Long getId();

    void setId(Long id);

    User getUser();

    void setUser(User user);

    String getUniqueParam();

    void setUniqueParam(String uniqueParam);

    LocalDateTime getSendTime();

    void setSendTime(LocalDateTime sendTime);

}
