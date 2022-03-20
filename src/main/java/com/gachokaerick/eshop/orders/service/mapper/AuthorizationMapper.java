package com.gachokaerick.eshop.orders.service.mapper;

import com.gachokaerick.eshop.orders.domain.aggregates.order.Authorization;
import com.gachokaerick.eshop.orders.domain.aggregates.order.OrderMapper;
import com.gachokaerick.eshop.orders.service.dto.AuthorizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Authorization} and its DTO {@link AuthorizationDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface AuthorizationMapper extends EntityMapper<AuthorizationDTO, Authorization> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    AuthorizationDTO toDto(Authorization s);
}
