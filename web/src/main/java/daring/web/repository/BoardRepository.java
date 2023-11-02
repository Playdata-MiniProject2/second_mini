package daring.web.repository;

import daring.web.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import daring.web.domain.User;

import java.util.List;

// JpaRepository<Entity 클래스, PK 타입>
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContaining(String keyword);

    Board findByUser(User user);
}
