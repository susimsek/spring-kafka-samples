package io.github.susimsek.springkafkasamples.assembler;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

public interface PagedRepresentationModelAssembler<T, D extends RepresentationModel<?>> {
    PagedModel<D> toPagedModel(Page<T> page);
}