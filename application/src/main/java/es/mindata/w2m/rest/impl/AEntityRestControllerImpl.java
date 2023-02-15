package es.mindata.w2m.rest.impl;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.mindata.w2m.annotations.TimedExecution;
import es.mindata.w2m.domain.IEntity;
import es.mindata.w2m.rest.IEntityRestController;
import es.mindata.w2m.service.IEntityService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AEntityRestControllerImpl<E extends IEntity<PK>, PK> implements IEntityRestController<E, PK> {

	public static final String PATH_BY_ID = "/{id}";

	@Autowired
	private IEntityService<E, PK> service;

	@TimedExecution
	@GetMapping(produces = APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<List<E>> getAll() {
		final List<E> results = this.service.getAll();

		if (null != results && 0 < results.size()) {
			return ResponseEntity.ok(results);

		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@TimedExecution
	@GetMapping(value = PATH_BY_ID, produces = APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<E> getById(@PathVariable PK id) {
		final Optional<E> optional = this.service.getById(id);

		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@TimedExecution
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<E> create(@RequestBody E entity) {
		entity.setId(null);
		return ResponseEntity.ok(this.service.create(entity));
	}

	@TimedExecution
	@PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@Override
	public ResponseEntity<E> update(@RequestBody E entity) {
		ResponseEntity<E> result = ResponseEntity.notFound().build();

		if (null != entity.getId()) {
			final Optional<E> updateResult = this.service.update(entity);

			if (updateResult.isPresent()) {
				result = ResponseEntity.ok(entity);
			}
		}

		return result;
	}

	@TimedExecution
	@DeleteMapping(value = PATH_BY_ID)
	@Override
	public ResponseEntity<Void> delete(@PathVariable PK id) {
		ResponseEntity<Void> result = ResponseEntity.notFound().build();
		boolean deleted = this.service.delete(id);

		if (deleted) {
			result = ResponseEntity.noContent().build();
		}

		return result;
	}

}
