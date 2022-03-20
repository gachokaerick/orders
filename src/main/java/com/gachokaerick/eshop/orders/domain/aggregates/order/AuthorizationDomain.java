package com.gachokaerick.eshop.orders.domain.aggregates.order;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.AuthorizationDTO;

public class AuthorizationDomain {

    private static final String domainName = "Authorization";
    private AuthorizationDTO dto;
    private AuthorizationBuilder builder;

    private AuthorizationDomain(AuthorizationBuilder builder) {
        this.dto = builder.dto;
    }

    public static class AuthorizationBuilder {

        private AuthorizationDTO dto;

        public AuthorizationBuilder() {}

        public AuthorizationBuilder withDTO(AuthorizationDTO dto) {
            this.dto = dto;
            return this;
        }

        private boolean isAcceptable() {
            if (dto == null) {
                throw DomainException.throwDomainException(domainName, "authorization is required");
            }
            if (dto.getId() == null && dto.getStatus() == null) {
                throw DomainException.throwDomainException(domainName, "authorization status is required");
            }
            if (dto.getId() == null && dto.getAuthId() == null) {
                throw DomainException.throwDomainException(domainName, "authorization authID is required");
            }
            if (dto.getId() == null && dto.getCurrencyCode() == null) {
                throw DomainException.throwDomainException(domainName, "authorization currency code is required");
            }
            if (dto.getId() == null && dto.getAmount() == null) {
                throw DomainException.throwDomainException(domainName, "authorization amount is required");
            }
            if (dto.getId() == null && dto.getExpirationTime() == null) {
                throw DomainException.throwDomainException(domainName, "authorization expiration time is required");
            }
            if (dto.getId() == null && dto.getPaymentProvider() == null) {
                throw DomainException.throwDomainException(domainName, "authorization payment provider is required");
            }
            if (dto.getId() == null && dto.getOrder() == null) {
                throw DomainException.throwDomainException(domainName, "authorization order is required");
            }
            if (dto.getOrder() != null && dto.getOrder().getId() == null) {
                throw DomainException.throwDomainException(domainName, "authorization order ID is required");
            }
            return true;
        }

        public AuthorizationDomain build() {
            if (isAcceptable()) {
                return new AuthorizationDomain(this);
            }
            throw DomainException.throwDomainException(domainName, "cannot build authorization with invalid data");
        }
    }
}
