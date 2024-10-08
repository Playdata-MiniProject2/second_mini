package daring.web.dto;

import daring.web.domain.Board;   // Board Entity를 가져옴
import daring.web.domain.Like;
import daring.web.domain.UploadImage;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

// DTO : 데이터 전달 목적
// 데이터를 캡슐화한 데이터 전달 객체
@Getter
@Setter
@ToString   // 객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메서드
@NoArgsConstructor  // 인자 없이 객체 생성 가능
public class BoardDto {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private Integer likeCnt;
    private MultipartFile newImage;
    private UploadImage uploadImage;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static BoardDto of(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .likeCnt(board.getLikes().size())
                .uploadImage(board.getUploadImage())
                .build();
    }

    /*public Board toEntity(){
        Board board = Board.builder()
                .id(id)
                .writer(writer)
                .title(title)
                .content(content)
                .likeCnt(0)
                .uploadImage(uploadImage)
                .build();
        return board;
    }*/

    //
    @Builder
    public BoardDto(Long id, String title, String content, String writer, Integer likeCnt, MultipartFile newImage, UploadImage uploadImage, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.likeCnt = likeCnt;
        this.uploadImage = uploadImage;
        this.newImage = newImage;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
