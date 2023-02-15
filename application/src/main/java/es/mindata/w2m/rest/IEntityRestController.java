package es.mindata.w2m.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;

import es.mindata.w2m.domain.IEntity;

public interface IEntityRestController<E extends IEntity<PK>, PK> {

	public ResponseEntity<List<E>> getAll();

	public ResponseEntity<E> getById(PK id);

	public ResponseEntity<E> create(E entity);

	public ResponseEntity<E> update(E entity);

	public ResponseEntity<Void> delete(PK id);

}
