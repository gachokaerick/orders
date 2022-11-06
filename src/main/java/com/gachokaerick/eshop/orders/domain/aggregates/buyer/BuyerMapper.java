package com.gachokaerick.eshop.orders.domain.aggregates.buyer;

import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import com.gachokaerick.eshop.orders.service.mapper.UserMapper;
import java.util.Objects;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Buyer} and its DTO {@link BuyerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface BuyerMapper extends EntityMapper<BuyerDTO, Buyer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    BuyerDTO toDto(Buyer s);

    @Named("email")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    BuyerDTO toDtoEmail(Buyer buyer);

    default void partialUpdate(Buyer entity, BuyerDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null && entity.getId() != null) {
            if (!Objects.equals(dto.getId(), entity.getId())) {
                return;
            }
        }

        if (dto.getFirstName() != null) {
            entity.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }
        if (dto.getGender() != null) {
            entity.setGender(dto.getGender());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
    }
}
