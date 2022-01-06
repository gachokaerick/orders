package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    PaymentDTO toDto(Payment s);
}
