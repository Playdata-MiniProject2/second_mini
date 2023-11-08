package daring.web.domain;

import daring.web.dto.BoardDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Board : 실제 DB와 매칭될 클래스 (Entity Class)

// JPA에서는 프록시 생성을 위해 기본 생성자를 반드시 하나 생성해야 한다.
// 생성자 자동 생성 : NoArgsConstructor, AllArgsConstructor
// NoArgsConstructor : 객체 생성 시 초기 인자 없이 객체를 생성할 수 있다.

@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 외부에서의 생성을 열어 둘 필요가 없을 때 / 보안적으로 권장된다.
@Getter
@Entity
//@AllArgsConstructor
//@Builder
@Table(name = "board")  // 이거 보고 테이블 생성
public class Board extends Time{

    @Id // PK Field
    @GeneratedValue(strategy= GenerationType.IDENTITY)  // PK의 생성 규칙
    private Long id;

    @Column(length = 10, nullable = false)
    private String writer;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    //@OneToMany(mappedBy = "board")
    private List<Like> likes;       // 좋아요
    private Integer likeCnt;        // 좋아요 수

    @OneToOne(fetch = FetchType.LAZY)
    private UploadImage uploadImage;

    // Java 디자인 패턴, 생성 시점에 값을 채워줌
    @Builder
    public Board(Long id, String title, String content, String writer, User user, Integer likeCnt, UploadImage uploadImage) {
        // Assert 구문으로 안전한 객체 생성 패턴을 구현
        Assert.hasText(writer, "writer must not be empty");
        Assert.hasText(title, "title must not be empty");
        Assert.hasText(content, "content must not be empty");

        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.user = user;
        this.likes=new ArrayList<>();
        this.likeCnt=likeCnt;
        this.uploadImage=uploadImage;
    }

    public void update(BoardDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void likeChange(Integer likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void setUploadImage(UploadImage uploadImage) {
        this.uploadImage = uploadImage;
    }
}
