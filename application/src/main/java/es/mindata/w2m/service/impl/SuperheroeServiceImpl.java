package es.mindata.w2m.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import es.mindata.w2m.domain.impl.Superheroe;
import es.mindata.w2m.repository.ISuperheroeRepository;
import es.mindata.w2m.service.ISuperheroeService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Service
public class SuperheroeServiceImpl extends AEntityServiceImpl<Superheroe, Long> implements ISuperheroeService {

	@Override
	public List<Superheroe> searchByNombre(@RequestParam String nombre) {
		return this.getRepository().findByNombreContainingIgnoreCase(nombre);
	}

	/*
	 * Getters & Setters
	 */

	@Override
	public ISuperheroeRepository getRepository() {
		return (ISuperheroeRepository) super.getRepository();
	}

}
