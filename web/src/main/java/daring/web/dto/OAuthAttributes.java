package daring.web.dto;

import lombok.Builder;
import lombok.Getter;
import daring.web.domain.Role;
import daring.web.domain.User;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;
    private String password;
    private String nickname;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String email,
                           String password,
                           String nickname) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .password((String) attributes.get("password"))
                .nickname((String) attributes.get("nickname"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.GUEST)
                .build();
    }
}
