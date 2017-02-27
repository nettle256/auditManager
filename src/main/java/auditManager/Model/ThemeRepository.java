package auditManager.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nettle on 2017/2/6.
 */
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    public Theme findById(Long id);
    public List<Theme> findByDeleted(Boolean deleted);
    public List<Theme> findAllByFather(Long father);
}
