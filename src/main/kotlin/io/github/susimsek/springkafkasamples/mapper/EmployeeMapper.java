package io.github.susimsek.springkafkasamples.mapper;

import io.github.susimsek.springkafkasamples.dto.EmployeeDTO;
import io.github.susimsek.springkafkasamples.entity.EmployeeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, EmployeeEntity> {
}
