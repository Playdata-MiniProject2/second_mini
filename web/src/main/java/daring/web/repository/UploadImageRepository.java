package daring.web.repository;

import daring.web.domain.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {

}
