package auditManager.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nettle on 2017/2/8.
 */

public interface ArticleRepository extends JpaRepository<Article, Long> {
    public Article findById(Long id);
    public List<Article> findAllById(List<Long> ids);
}
