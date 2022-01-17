package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.service.dto.PaymentDTO;
import com.gachokaerick.eshop.orders.service.mapper.EntityMapper;
import java.util.Objects;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrderMapper.class })
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    PaymentDTO toDto(Payment s);

    default void partialUpdate(Payment entity, PaymentDTO dto) {
        if (dto == null) {
            return;
        }

        if (dto.getId() != null && entity.getId() != null) {
            if (!Objects.equals(dto.getId(), entity.getId())) {
                return;
            }
        }

        if (dto.getCreateTime() != null) {
            entity.setCreateTime(dto.getCreateTime());
        }
        if (dto.getUpdateTime() != null) {
            entity.setUpdateTime(dto.getUpdateTime());
        }
        if (dto.getPaymentStatus() != null) {
            entity.setPaymentStatus(dto.getPaymentStatus());
        }
        if (dto.getPayerCountryCode() != null) {
            entity.setPayerCountryCode(dto.getPayerCountryCode());
        }
        if (dto.getPayerEmail() != null) {
            entity.setPayerEmail(dto.getPayerEmail());
        }
        if (dto.getPayerName() != null) {
            entity.setPayerName(dto.getPayerName());
        }
        if (dto.getPayerSurname() != null) {
            entity.setPayerSurname(dto.getPayerSurname());
        }
        if (dto.getPayerId() != null) {
            entity.setPayerId(dto.getPayerId());
        }
        if (dto.getCurrency() != null) {
            entity.setCurrency(dto.getCurrency());
        }
        if (dto.getAmount() != null) {
            entity.setAmount(dto.getAmount());
        }
        if (dto.getPaymentId() != null) {
            entity.setPaymentId(dto.getPaymentId());
        }
    }
}
