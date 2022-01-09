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
        if (buyerDTO.getUser() != null) {
            buyer.setUser(userMapper.userFromId(buyerDTO.getUser().getId()));
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
                throwDomainException("buyer dto cannot be null");
            }
            if (buyerDTO.getFirstName() == null) {
                throwDomainException("first name cannot be null");
            }
            if (buyerDTO.getLastName() == null) {
                throwDomainException("last name cannot be null");
            }
            if (buyerDTO.getGender() == null) {
                throwDomainException("gender cannot be null");
            }
            if (buyerDTO.getEmail() == null) {
                throwDomainException("email cannot be null");
            }
            if (!validator.validate(buyerDTO.getEmail())) {
                throwDomainException("email is invalid");
            }
            if (buyerDTO.getPhone() == null) {
                throwDomainException("phone cannot be null");
            }
            if (buyerDTO.getId() == null && buyerDTO.getUser() == null) {
                throwDomainException("user dto cannot be null");
            }
            if (buyerDTO.getUser() != null && buyerDTO.getUser().getId() == null) {
                throwDomainException("user id cannot be null");
            }

            return true;
        }

        private void throwDomainException(String message) {
            throw DomainException.throwDomainException(domainName, message);
        }

        public BuyerDomain build() {
            if (isAcceptable()) {
                return new BuyerDomain(this);
            }
            throw DomainException.throwDomainException(domainName, "cannot create buyer with invalid data");
        }
    }
}
