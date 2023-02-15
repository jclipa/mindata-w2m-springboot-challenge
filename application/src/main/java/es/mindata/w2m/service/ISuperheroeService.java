package es.mindata.w2m.service;

import java.util.List;

import es.mindata.w2m.domain.impl.Superheroe;

public interface ISuperheroeService extends IEntityService<Superheroe, Long> {

	public List<Superheroe> searchByNombre(String nombre);

}
