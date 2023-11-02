package daring.web.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 외부에서의 생성을 열어 둘 필요가 없을 때 / 보안적으로 권장된다.
public class User extends Time{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, String password, String nickname, Role role){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.nickname = name;
        this.password = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
