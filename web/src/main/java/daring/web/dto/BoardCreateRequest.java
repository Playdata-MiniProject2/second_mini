package daring.web.dto;

import daring.web.domain.Board;
import daring.web.domain.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardCreateRequest {

    private String writer;
    private String title;
    private String content;
    private MultipartFile uploadImage;

    public Board toEntity(User user) {
        return Board.builder()
                .user(user)
                .writer(writer)
                .title(title)
                .content(content)
                .likeCnt(0)
                .build();
    }

}
