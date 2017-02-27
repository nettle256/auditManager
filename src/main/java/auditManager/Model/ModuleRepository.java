package auditManager.Model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nettle on 2017/2/8.
 */
public interface ModuleRepository extends JpaRepository<Module, Long> {
    public Module findById(Long id);
    public List<Module> findAllByDeleted(Boolean deleted);
}
