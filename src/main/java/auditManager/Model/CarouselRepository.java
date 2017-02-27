package auditManager.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nettle on 2017/2/8.
 */

public interface CarouselRepository extends JpaRepository<Carousel, Long> {
    public Carousel findById(Long id);
    public List<Carousel> findAllByDeleted(Boolean deleted);
}
