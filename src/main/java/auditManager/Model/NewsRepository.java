package auditManager.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nettle on 2017/2/27.
 */
public interface NewsRepository extends JpaRepository<News, Long> {
    public News findById(Long id);
    public News findByIdAndDeleted(Long id, Boolean deleted);
    public List<News> findAllByDeleted(Boolean deleted);
    public List<News> findAllByPublished(Boolean published);
    public List<News> findAllByPublishedAndDeleted(Boolean published, Boolean deleted);
}
