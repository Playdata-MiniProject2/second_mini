package daring.web.config.auth;

import daring.web.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String email;
    private String password;
    private String nickname;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
    }
}
