package es.mindata.w2m.domain;

public interface IEntity<PK> {

	public PK getId();

	public void setId(PK id);

}
