package org.antonakospanos.iot.atlas.web.dto;

import java.io.Serializable;

public interface Dto<E extends Object, T extends Object> extends Serializable {

	public T getId();

	public Dto<E, T> fromEntity(E entity);

	public E toEntity();

}