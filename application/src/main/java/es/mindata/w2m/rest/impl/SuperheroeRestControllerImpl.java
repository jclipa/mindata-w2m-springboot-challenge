package es.mindata.w2m.rest.impl;

import static es.mindata.w2m.rest.impl.SuperheroeRestControllerImpl.PATH_BASE;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.mindata.w2m.annotations.TimedExecution;
import es.mindata.w2m.domain.impl.Superheroe;
import es.mindata.w2m.rest.ISuperheroeRestController;
import es.mindata.w2m.service.ISuperheroeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@RestController
@RequestMapping(PATH_BASE)
@CacheConfig(cacheNames = "superheroes")
@Tag(name = "Superheroes")
@SecurityRequirement(name = "user")
public class SuperheroeRestControllerImpl extends AEntityRestControllerImpl<Superheroe, Long>
		implements ISuperheroeRestController {

	public static final String PATH_BASE = "/superheroes";
	public static final String PATH_SEARCH = "/search";

	@Cacheable(key = "{#root.methodName}")
	@Override
	public ResponseEntity<List<Superheroe>> getAll() {
		log.info("Pasando por getAll");
		return super.getAll();
	}

	@Cacheable(key = "{#root.methodName, #id}")
	@Override
	public ResponseEntity<Superheroe> getById(@PathVariable Long id) {
		log.info("Pasando por getById");
		return super.getById(id);
	}

	@Cacheable(key = "{#root.methodName, #nombre}")
	@TimedExecution
	@GetMapping(PATH_SEARCH)
	@Override
	public ResponseEntity<List<Superheroe>> searchByNombre(@RequestParam String nombre) {
		log.info("Pasando por searchByNombre");

		final List<Superheroe> results = this.getService().searchByNombre(nombre);

		if (null != results && 0 < results.size()) {
			return ResponseEntity.ok(results);

		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@CacheEvict(beforeInvocation = true, allEntries = true)
	@Override
	public ResponseEntity<Superheroe> create(@RequestBody Superheroe entity) {
		return super.create(entity);
	}

	@CacheEvict(beforeInvocation = true, allEntries = true)
	@Override
	public ResponseEntity<Superheroe> update(@RequestBody Superheroe entity) {
		return super.update(entity);
	}

	@CacheEvict(beforeInvocation = true, allEntries = true)
	@Override
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		return super.delete(id);
	}

	@Override
	public ISuperheroeService getService() {
		return (ISuperheroeService) super.getService();
	}

}
