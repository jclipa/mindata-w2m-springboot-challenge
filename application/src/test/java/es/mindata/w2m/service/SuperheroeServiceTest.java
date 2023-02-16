package es.mindata.w2m.service;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import es.mindata.w2m.domain.impl.Superheroe;
import es.mindata.w2m.repository.ISuperheroeRepository;
import es.mindata.w2m.service.impl.SuperheroeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SuperheroeServiceTest {

	@Mock
	private ISuperheroeRepository repository;

	private ISuperheroeService service = new SuperheroeServiceImpl();

	private List<Superheroe> superheroes;

	@BeforeEach
	public void setUp(TestInfo info) {
		MockitoAnnotations.openMocks(this);

		((SuperheroeServiceImpl) this.service).setRepository(this.repository);

		superheroes = new ArrayList<Superheroe>();
		superheroes.add(new Superheroe(1L, "Superman"));
		superheroes.add(new Superheroe(2L, "Spiderman"));
		superheroes.add(new Superheroe(3L, "Manolito el fuerte"));
	}

	@Test
	void getAll_shouldReturnAllSuperheroes() throws Exception {
		final var expResults = this.superheroes;
		Mockito.when(this.repository.findAll()).thenReturn(expResults);

		final var results = this.service.getAll();

		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(expResults.size());
		assertThat(results).isEqualTo(expResults);
	}

	@Test
	void getAll_shouldReturnNoSuperheroes() throws Exception {
		Mockito.when(this.repository.findAll()).thenReturn(new ArrayList<Superheroe>());

		final var results = this.service.getAll();

		assertThat(results).isEmpty();
	}

	@Test
	void getById_shouldReturnSuperheroeById() throws Exception {
		final var superheroeIdParam = 1L;

		final var expResult = this.superheroes.get(0);
		Mockito.when(this.repository.findById(superheroeIdParam)).thenReturn(Optional.ofNullable(expResult));

		final var result = this.service.getById(superheroeIdParam);

		assertThat(result.isPresent()).isTrue();
		assertThat(result.get()).isNotNull();
		assertThat(result.get()).isEqualTo(expResult);
	}

	@Test
	void getById_shouldReturnNotFoundForNonexistentId() throws Exception {
		final var superheroeIdParam = 1L;
		Mockito.when(this.repository.findById(superheroeIdParam)).thenReturn(Optional.empty());

		final var result = this.service.getById(superheroeIdParam);

		assertThat(result.isPresent()).isFalse();
	}

	@Test
	void searchByNombre_shouldReturnSuperheroesWithNombreContainingParameter() throws Exception {
		final var nombreParam = "mano";

		final var expResults = this.superheroes.stream().filter(s -> containsIgnoreCase(s.getNombre(), nombreParam))
				.collect(toList());
		Mockito.when(this.repository.findByNombreContainingIgnoreCase(nombreParam)).thenReturn(expResults);

		final var results = this.service.searchByNombre(nombreParam);

		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(expResults.size());
		assertThat(results).isEqualTo(expResults);
		assertThat(results).containsExactlyInAnyOrder(expResults.get(0));
	}

	@Test
	void create_shouldCreateSuperheroe() throws Exception {
		final var newSuperheroe = new Superheroe("Create Superheroe");

		final var expResult = new Superheroe(4L, "Create Superheroe");
		Mockito.when(this.repository.save(newSuperheroe)).thenReturn(expResult);

		final var result = this.service.create(newSuperheroe);

		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(expResult);
	}

	@Test
	void create_shouldUpdateSuperheroe() throws Exception {
		final var updSuperheroe = this.superheroes.get(0);
		updSuperheroe.setNombre("Batman");

		final var expResult = new Superheroe(1L, "Batman");
		Mockito.when(this.repository.existsById(updSuperheroe.getId())).thenReturn(true);

		final var result = this.service.update(expResult);

		assertThat(result.isPresent()).isTrue();
		assertThat(result.get()).isNotNull();
		assertThat(result.get()).isEqualTo(expResult);
	}

	@Test
	void create_shouldNotUpdateSuperheroe() throws Exception {
		final var updSuperheroe = new Superheroe("Batman");

		Mockito.when(this.repository.existsById(updSuperheroe.getId())).thenReturn(false);

		final var result = this.service.update(updSuperheroe);

		assertThat(result.isPresent()).isFalse();
	}

	@Test
	void create_shouldDeleteSuperheroe() throws Exception {
		final var superheroeIdParam = 1L;

		Mockito.when(this.repository.existsById(superheroeIdParam)).thenReturn(true);

		final var result = this.service.delete(superheroeIdParam);

		assertThat(result).isTrue();
	}

	@Test
	void create_shouldNotDeleteSuperheroe() throws Exception {
		final var superheroeIdParam = 1L;

		Mockito.when(this.repository.existsById(superheroeIdParam)).thenReturn(false);

		final var result = this.service.delete(superheroeIdParam);

		assertThat(result).isFalse();
	}

}