package mx.edu.uteq.HolaMundo.repository;

import mx.edu.uteq.HolaMundo.entity.OfertaEducativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaEducativaRepo extends JpaRepository<OfertaEducativa, Long> {
    // Puedes agregar métodos personalizados si es necesario
}
