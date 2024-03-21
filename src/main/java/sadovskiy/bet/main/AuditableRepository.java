package sadovskiy.bet.main;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AuditableRepository <E extends AuditableEntity> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {
}
