package es.mindata.w2m.domain.impl;

import es.mindata.w2m.domain.IEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "superheroe")
public class Superheroe implements IEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "superheroe_id", nullable = false, insertable = false, updatable = false)
	private Long id = null;

	@Column(name = "nombre", nullable = false)
	private String nombre = null;

	/*
	 * Constructors
	 */

	public Superheroe(String nombre) {
		this();
		this.nombre = nombre;
	}

}
