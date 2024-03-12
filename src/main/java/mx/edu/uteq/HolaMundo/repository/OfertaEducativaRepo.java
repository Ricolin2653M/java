package mx.edu.uteq.HolaMundo.repository;

import java.util.List;
import mx.edu.uteq.HolaMundo.entity.OfertaEducativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaEducativaRepo extends JpaRepository<OfertaEducativa, Long> {
    public List<OfertaEducativa> findByActivo(boolean activo);
}
