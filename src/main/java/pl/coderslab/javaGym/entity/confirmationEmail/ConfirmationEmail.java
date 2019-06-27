package pl.coderslab.javaGym.entity.confirmationEmail;

import pl.coderslab.javaGym.entity.user.User;

import java.time.ZonedDateTime;

public interface ConfirmationEmail {

    Long getId();

    void setId(Long id);

    User getUser();

    void setUser(User user);

    String getParam();

    void setParam(String uniqueParam);

    ZonedDateTime getSendTime();

    void setSendTime(ZonedDateTime sendTime);

    Integer getMinutesExpirationTime();

    void setMinutesExpirationTime(Integer minutesExpirationTime);

}
