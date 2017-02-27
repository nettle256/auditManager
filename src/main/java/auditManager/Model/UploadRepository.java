package auditManager.Model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Nettle on 2017/2/8.
 */
public interface UploadRepository  extends JpaRepository<Upload, Long> {
    public Upload findById(Long id);
}