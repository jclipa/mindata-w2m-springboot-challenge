package es.mindata.w2m.service;

import java.util.List;
import java.util.Optional;

import es.mindata.w2m.domain.IEntity;

public interface IEntityService<E extends IEntity<PK>, PK> {

	public List<E> getAll();

	public boolean exists(PK id);

	public Optional<E> getById(PK id);

	public E create(E entity);

	public Optional<E> update(E entity);

	public boolean delete(PK id);

}
