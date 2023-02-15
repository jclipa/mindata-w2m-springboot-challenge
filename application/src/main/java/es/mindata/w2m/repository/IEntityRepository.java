package es.mindata.w2m.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import es.mindata.w2m.domain.IEntity;

@NoRepositoryBean
public interface IEntityRepository<E extends IEntity<PK>, PK> extends JpaRepository<E, PK> {

}
