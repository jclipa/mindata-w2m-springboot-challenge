package es.mindata.w2m.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.mindata.w2m.domain.impl.Superheroe;

@Repository
public interface ISuperheroeRepository extends IEntityRepository<Superheroe, Long> {

	public List<Superheroe> findByNombreContainingIgnoreCase(String nombre);

}
