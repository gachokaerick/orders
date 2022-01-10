package com.gachokaerick.eshop.orders.domain.aggregates.buyer;

import com.gachokaerick.eshop.orders.domain.exception.DomainException;
import com.gachokaerick.eshop.orders.service.dto.BuyerDTO;
import com.gachokaerick.eshop.orders.service.mapper.UserMapper;
import com.gachokaerick.eshop.orders.validators.EmailValidator;

public class BuyerDomain {

    private static final String domainName = "buyer";
    private final BuyerDTO buyerDTO;

    private BuyerDomain(BuyerBuilder builder) {
        this.buyerDTO = builder.buyerDTO;
    }

    public Buyer getBuyer() {
        UserMapper userMapper = new UserMapper();
        Buyer buyer = new Buyer();
        buyer.setId(buyerDTO.getId());
        buyer.setFirstName(buyerDTO.getFirstName());
        buyer.setLastName(buyerDTO.getLastName());
        buyer.setGender(buyerDTO.getGender());
        buyer.setEmail(buyerDTO.getEmail());
        buyer.setPhone(buyerDTO.getPhone());
        if (buyerDTO.getId() == null && buyerDTO.getUser() != null) {
            buyer.setUser(userMapper.newUserFromDTO(buyerDTO.getUser()));
        }
        return buyer;
    }

    public static class BuyerBuilder {

        private BuyerDTO buyerDTO;

        public BuyerBuilder() {}

        public BuyerBuilder withDTO(BuyerDTO buyerDTO) {
            this.buyerDTO = buyerDTO;
            return this;
        }

        private boolean isAcceptable() {
            EmailValidator validator = new EmailValidator();

            if (buyerDTO == null) {
                throw DomainException.throwDomainException(domainName, " buyer dto cannot be null");
            }
            if (buyerDTO.getId() == null && buyerDTO.getFirstName() == null) {
                throw DomainException.throwDomainException(domainName, " first name cannot be null");
            }
            if (buyerDTO.getId() == null && buyerDTO.getLastName() == null) {
                throw DomainException.throwDomainException(domainName, " last name cannot be null");
            }
            if (buyerDTO.getId() == null && buyerDTO.getGender() == null) {
                throw DomainException.throwDomainException(domainName, " gender cannot be null");
            }
            if (buyerDTO.getId() == null && buyerDTO.getEmail() == null) {
                throw DomainException.throwDomainException(domainName, " email cannot be null");
            }
            if (buyerDTO.getId() == null && !validator.validate(buyerDTO.getEmail())) {
                throw DomainException.throwDomainException(domainName, " email is invalid");
            }
            if (buyerDTO.getId() == null && buyerDTO.getPhone() == null) {
                throw DomainException.throwDomainException(domainName, " phone cannot be null");
            }
            if (buyerDTO.getId() == null && buyerDTO.getUser() == null) {
                throw DomainException.throwDomainException(domainName, " user dto cannot be null");
            }
            if (buyerDTO.getUser() != null && buyerDTO.getUser().getId() == null) {
                throw DomainException.throwDomainException(domainName, " user id cannot be null");
            }

            return true;
        }

        public BuyerDomain build() {
            if (isAcceptable()) {
                return new BuyerDomain(this);
            }
            throw DomainException.throwDomainException(domainName, "cannot create buyer with invalid data");
        }
    }
}
