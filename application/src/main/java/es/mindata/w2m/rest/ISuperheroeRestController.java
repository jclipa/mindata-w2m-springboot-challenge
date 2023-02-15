package es.mindata.w2m.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;

import es.mindata.w2m.domain.impl.Superheroe;

public interface ISuperheroeRestController extends IEntityRestController<Superheroe, Long> {

	public ResponseEntity<List<Superheroe>> searchByNombre(String nombre);

}
