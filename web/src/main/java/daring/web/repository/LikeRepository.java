package daring.web.repository;

import daring.web.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteByUserEmailAndBoardId(String email, Long boardId);
    Boolean existsByUserEmailAndBoardId(String email, Long boardId);
    List<Like> findAllByUserEmail(String email);
}
