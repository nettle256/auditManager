package auditManager.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nettle on 2017/2/10.
 */

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    public Photo findById(Long id);
    public List<Photo> findAllByDeleted(Boolean deleted);
}
