package es.mindata.w2m.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.mindata.w2m.domain.IEntity;
import es.mindata.w2m.repository.IEntityRepository;
import es.mindata.w2m.service.IEntityService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AEntityServiceImpl<E extends IEntity<PK>, PK> implements IEntityService<E, PK> {

	@Autowired
	private IEntityRepository<E, PK> repository;

	@Override
	public List<E> getAll() {
		return this.repository.findAll();
	}

	@Override
	public boolean exists(PK id) {
		return this.repository.existsById(id);
	}

	@Override
	public Optional<E> getById(PK id) {
		return this.repository.findById(id);
	}

	@Override
	public E create(E entity) {
		return this.repository.save(entity);
	}

	@Override
	public Optional<E> update(E entity) {
		Optional<E> result = Optional.empty();

		if (null != entity.getId()) {
			final var exists = this.exists(entity.getId());

			if (exists) {
				this.repository.save(entity);
				result = Optional.ofNullable(entity);
			}
		}

		return result;
	}

	@Override
	public boolean delete(PK id) {
		final var result = this.exists(id);

		if (result) {
			this.repository.deleteById(id);
		}

		return result;
	}

}
