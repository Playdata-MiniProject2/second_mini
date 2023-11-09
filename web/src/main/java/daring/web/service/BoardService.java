package daring.web.service;

import daring.web.domain.UploadImage;
import daring.web.domain.User;
import daring.web.dto.BoardCreateRequest;
import daring.web.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import daring.web.domain.Board;
import daring.web.repository.BoardRepository;
import daring.web.dto.BoardDto;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BoardService {

    //boardRepository 객체 생성
    private BoardRepository boardRepository;
    private final UploadImageService uploadImageService;
    private final UserRepository userRepository;


    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 4; // 한 페이지에 존재하는 게시글 수

    // Entity -> Dto로 변환
    private BoardDto convertEntityToDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .likeCnt(board.getLikeCnt())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }


    @Transactional
    public List<BoardDto> getBoardlist(Integer pageNum) {
        Page<Board> page = boardRepository.findAll(PageRequest.of(
                pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));

        List<Board> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (Board board : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    @Transactional
    public BoardDto getPost(Long id) {
        Optional<Board> optBoard = boardRepository.findById(id);
        return BoardDto.of(optBoard.get());
    }

    @Transactional
    public Long savePost(BoardCreateRequest req, String email, Authentication auth) throws IOException {
        User loginUser = userRepository.findByEmail(email).get();
        Board savedBoard = boardRepository.save(req.toEntity(loginUser));
        UploadImage uploadImage = uploadImageService.saveImage(req.getUploadImage(), savedBoard);
        if (uploadImage != null) {
            savedBoard.setUploadImage(uploadImage);
        }
        return savedBoard.getId();
    }

    @Transactional
    public Long editPost(Long boardId, BoardDto dto) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        Board board = optBoard.get();
        // 게시글에 이미지가 있었으면 삭제
        if (board.getUploadImage() != null) {
            uploadImageService.deleteImage(board.getUploadImage());
            board.setUploadImage(null);
        }

        UploadImage uploadImage = uploadImageService.saveImage(dto.getNewImage(), board);
        if (uploadImage != null) {
            board.setUploadImage(uploadImage);
        }
        board.update(dto);

        return board.getId();
    }

    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    // 검색 API
    @Transactional
    public List<BoardDto> searchPosts(String keyword) {
        List<Board> boardEntities = boardRepository.findByTitleContaining(keyword);
        List<BoardDto> boardDtoList = new ArrayList<>();

        if (boardEntities.isEmpty()) return boardDtoList;

        for (Board board : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }


    // 페이징
    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }

    public Integer[] getPageList(Integer curPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        // 총 게시글 갯수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림으로 계산)
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;

        // 페이지 번호 할당
        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
    }
}
