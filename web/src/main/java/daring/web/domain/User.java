package daring.web.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
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

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Board> boards;     // 작성글

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Like> likes;       // 유저가 누른 좋아요

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
