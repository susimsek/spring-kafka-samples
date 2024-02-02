package io.github.susimsek.springkafkasamples.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.github.susimsek.springkafkasamples.controller.EmployeeController;
import io.github.susimsek.springkafkasamples.dto.EmployeeDTO;
import io.github.susimsek.springkafkasamples.entity.EmployeeEntity;
import io.github.susimsek.springkafkasamples.mapper.EmployeeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class EmployeeModelAssembler
    extends RepresentationModelAssemblerSupport<EmployeeEntity, EmployeeDTO>
    implements PagedRepresentationModelAssembler<EmployeeEntity, EmployeeDTO> {

    private final EmployeeMapper employeeMapper;
    private final PagedResourcesAssembler<EmployeeEntity> pagedResourcesAssembler;

    public EmployeeModelAssembler(EmployeeMapper employeeMapper,
                                  PagedResourcesAssembler<EmployeeEntity> pagedResourcesAssembler) {
        super(EmployeeController.class, EmployeeDTO.class);
        this.employeeMapper = employeeMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EmployeeDTO toModel(EmployeeEntity entity) {
        var model =  employeeMapper.toDto(entity);
        model.add(linkTo(
            methodOn(EmployeeController.class)
                .findOne(entity.getId()))
            .withSelfRel());
        return model;
    }

    @Override
    public CollectionModel<EmployeeDTO> toCollectionModel(Iterable<? extends EmployeeEntity> entities) {
        var models = super.toCollectionModel(entities);
        models.add(linkTo(methodOn(EmployeeController.class).findAll()).withSelfRel());
        return models;
    }

    @Override
    public PagedModel<EmployeeDTO> toPagedModel(Page<EmployeeEntity> page) {
        return pagedResourcesAssembler .toModel(page, this);
    }
}