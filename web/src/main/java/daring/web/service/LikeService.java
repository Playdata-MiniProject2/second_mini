package daring.web.service;

import daring.web.domain.Board;
import daring.web.domain.Like;
import daring.web.domain.User;
import daring.web.repository.BoardRepository;
import daring.web.repository.LikeRepository;
import daring.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void addLike(String email, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User boardUser = board.getUser();

        // 자신이 누른 좋아요가 아니라면
        //if (!boardUser.equals(loginUser)) {
        //    boardUser.likeChange(boardUser.getReceivedLikeCnt() + 1);
        //}
        board.likeChange(board.getLikeCnt() + 1);

        likeRepository.save(Like.builder()
                .user(loginUser)
                .board(board)
                .build());
    }

    @Transactional
    public void deleteLike(String email, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User boardUser = board.getUser();

        // 자신이 누른 좋아요가 아니라면
        //if (!boardUser.equals(loginUser)) {
        //    boardUser.likeChange(boardUser.getReceivedLikeCnt() - 1);
        //}
        board.likeChange(board.getLikeCnt() - 1);

        likeRepository.deleteByUserEmailAndBoardId(email, boardId);
    }

    public Boolean checkLike(String email, Long boardId) {
        return likeRepository.existsByUserEmailAndBoardId(email, boardId);
    }
}
