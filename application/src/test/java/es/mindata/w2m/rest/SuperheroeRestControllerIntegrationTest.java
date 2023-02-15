package es.mindata.w2m.rest;

import static es.mindata.w2m.rest.impl.SuperheroeRestControllerImpl.PATH_BASE;
import static es.mindata.w2m.rest.impl.SuperheroeRestControllerImpl.PATH_SEARCH;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.mindata.w2m.domain.impl.Superheroe;
import es.mindata.w2m.repository.ISuperheroeRepository;
import es.mindata.w2m.rest.impl.SuperheroeRestControllerImpl;
import es.mindata.w2m.service.ISuperheroeService;
import es.mindata.w2m.service.impl.SuperheroeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SuperheroeRestControllerIntegrationTest {

	@Mock
	private ISuperheroeRepository repository;

	private MockMvc mockMvc;

	private ISuperheroeService service = new SuperheroeServiceImpl();

	private ISuperheroeRestController controller = new SuperheroeRestControllerImpl();

	private List<Superheroe> superheroes;

	private Gson gson = new Gson();

	@BeforeEach
	public void setUp(TestInfo info) {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();

		((SuperheroeServiceImpl) this.service).setRepository(this.repository);
		((SuperheroeRestControllerImpl) this.controller).setService(service);

		superheroes = new ArrayList<Superheroe>();
		superheroes.add(new Superheroe(1L, "Superman"));
		superheroes.add(new Superheroe(2L, "Spiderman"));
		superheroes.add(new Superheroe(3L, "Manolito el fuerte"));
	}

	@Test
	void getAll_shouldReturnAllSuperheroes() throws Exception {
		final List<Superheroe> expResults = this.superheroes;
		Mockito.when(this.repository.findAll()).thenReturn(expResults);

		final String path = PATH_BASE;
		final MvcResult mvcResult = this.mockMvc.perform(get(path)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(expResults.size()))).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final List<Superheroe> responseBody = this.gson.fromJson(responseBodyS, new TypeToken<List<Superheroe>>() {
		}.getType());

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(OK.value());
		assertThat(responseBody).isNotNull();
		assertThat(responseBody.size()).isEqualTo(expResults.size());
		assertThat(responseBody).isEqualTo(expResults);
	}

	@Test
	void getAll_shouldReturnNoSuperheroes() throws Exception {
		Mockito.when(this.repository.findAll()).thenReturn(new ArrayList<Superheroe>());

		final String path = PATH_BASE;
		final MvcResult mvcResult = this.mockMvc.perform(get(path)).andExpect(status().isNoContent())
				.andExpect(jsonPath("$").doesNotExist()).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final List<Superheroe> responseBody = this.gson.fromJson(responseBodyS, new TypeToken<List<Superheroe>>() {
		}.getType());

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(NO_CONTENT.value());
		assertThat(responseBody).isNull();
	}

	@Test
	void getById_shouldReturnSuperheroeById() throws Exception {
		final Long superheroeIdParam = 1L;

		final Superheroe expResult = this.superheroes.get(0);
		Mockito.when(this.repository.findById(superheroeIdParam)).thenReturn(Optional.ofNullable(expResult));

		final String path = PATH_BASE + "/" + superheroeIdParam;
		final MvcResult mvcResult = this.mockMvc.perform(get(path)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON)).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final Superheroe responseBody = this.gson.fromJson(responseBodyS, Superheroe.class);

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(OK.value());
		assertThat(responseBody).isNotNull();
		assertThat(responseBody).isEqualTo(expResult);
	}

	@Test
	void getById_shouldReturnNotFoundForNonexistentId() throws Exception {
		final Long superheroeId = 1L;
		Mockito.when(this.repository.findById(superheroeId)).thenReturn(Optional.empty());

		final String path = PATH_BASE + "/" + superheroeId;
		final MvcResult mvcResult = this.mockMvc.perform(get(path)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist()).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final Superheroe responseBody = this.gson.fromJson(responseBodyS, Superheroe.class);

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
		assertThat(responseBody).isNull();
	}

	@Test
	void searchByNombre_shouldReturnSuperheroesWithNombreContainingParameter() throws Exception {
		final String nombreParam = "mano";

		final List<Superheroe> expResults = this.superheroes.stream()
				.filter(s -> containsIgnoreCase(s.getNombre(), nombreParam)).collect(toList());
		Mockito.when(this.repository.findByNombreContainingIgnoreCase(nombreParam)).thenReturn(expResults);

		final String path = PATH_BASE + "/" + PATH_SEARCH + "/?nombre=" + nombreParam;
		final MvcResult mvcResult = this.mockMvc.perform(get(path)).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(expResults.size()))).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final List<Superheroe> responseBody = this.gson.fromJson(responseBodyS, new TypeToken<List<Superheroe>>() {
		}.getType());

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(OK.value());
		assertThat(responseBody).isNotNull();
		assertThat(responseBody.size()).isEqualTo(expResults.size());
		assertThat(responseBody).isEqualTo(expResults);
		assertThat(responseBody).containsExactlyInAnyOrder(expResults.get(0));
	}

	@Test
	void create_shouldCreateSuperheroe() throws Exception {
		final Superheroe reqBody = new Superheroe("Create Superheroe");

		final Superheroe expResult = new Superheroe(4L, "Create Superheroe");
		Mockito.when(this.repository.save(reqBody)).thenReturn(expResult);

		final String path = PATH_BASE;
		final MvcResult mvcResult = this.mockMvc
				.perform(post(path).content(this.gson.toJson(reqBody)).contentType(APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON)).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final Superheroe responseBody = this.gson.fromJson(responseBodyS, Superheroe.class);

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(OK.value());
		assertThat(responseBody).isNotNull();
		assertThat(responseBody).isEqualTo(expResult);
	}

	@Test
	void create_shouldNotCreateSuperheroe() throws Exception {
		final Superheroe reqBody = null;

		final Superheroe expResult = null;
		Mockito.when(this.repository.save(Mockito.any(Superheroe.class))).thenReturn(expResult);

		final String path = PATH_BASE;
		final MvcResult mvcResult = this.mockMvc
				.perform(post(path).content(this.gson.toJson(reqBody)).contentType(APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final Superheroe responseBody = this.gson.fromJson(responseBodyS, Superheroe.class);

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(BAD_REQUEST.value());
		assertThat(responseBody).isNull();
	}

	@Test
	void create_shouldUpdateSuperheroe() throws Exception {
		final Superheroe reqBody = this.superheroes.get(0);
		reqBody.setNombre("Batman");

		final Superheroe expResult = new Superheroe(1L, "Batman");
		Mockito.when(this.repository.existsById(reqBody.getId())).thenReturn(true);

		final String path = PATH_BASE;
		final MvcResult mvcResult = this.mockMvc
				.perform(put(path).content(this.gson.toJson(reqBody)).contentType(APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON)).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final Superheroe responseBody = this.gson.fromJson(responseBodyS, Superheroe.class);

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(OK.value());
		assertThat(responseBody).isNotNull();
		assertThat(responseBody).isEqualTo(expResult);
	}

	@Test
	void create_shouldNotUpdateSuperheroe() throws Exception {
		final Superheroe reqBody = new Superheroe("Batman");

		Mockito.when(this.repository.existsById(reqBody.getId())).thenReturn(false);

		final String path = PATH_BASE;
		final MvcResult mvcResult = this.mockMvc
				.perform(put(path).content(this.gson.toJson(reqBody)).contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound()).andReturn();

		final String responseBodyS = mvcResult.getResponse().getContentAsString();
		final Superheroe responseBody = this.gson.fromJson(responseBodyS, Superheroe.class);

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
		assertThat(responseBody).isNull();
	}

	@Test
	void create_shouldDeleteSuperheroe() throws Exception {
		final Long superheroeIdParam = 1L;

		Mockito.when(this.repository.existsById(superheroeIdParam)).thenReturn(true);

		final String path = PATH_BASE + "/" + superheroeIdParam;
		final MvcResult mvcResult = this.mockMvc.perform(delete(path)).andExpect(status().isNoContent())
				.andExpect(jsonPath("$").doesNotExist()).andReturn();

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(NO_CONTENT.value());
	}

	@Test
	void create_shouldNotDeleteSuperheroe() throws Exception {
		final Long superheroeIdParam = 10L;

		Mockito.when(this.repository.existsById(superheroeIdParam)).thenReturn(false);

		final String path = PATH_BASE + "/" + superheroeIdParam;
		final MvcResult mvcResult = this.mockMvc.perform(delete(path)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$").doesNotExist()).andReturn();

		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
	}

}