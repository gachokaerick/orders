package com.gachokaerick.eshop.orders.service.dto;

import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.Buyer;
import com.gachokaerick.eshop.orders.domain.aggregates.buyer.BuyerDomain;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link Address} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class AddressDTO implements Serializable {

    private Long id;

    private String street;

    @NotNull
    private String city;

    @NotNull
    private String town;

    @NotNull
    private String country;

    private String zipcode;

    private BuyerDTO buyer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public BuyerDTO getBuyer() {
        return buyer;
    }

    public void setBuyer(BuyerDTO buyer) {
        this.buyer = buyer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressDTO)) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, addressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressDTO{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", town='" + getTown() + "'" +
            ", country='" + getCountry() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            ", buyer=" + getBuyer() +
            "}";
    }

    public Address toEntity() {
        Buyer buyer = new BuyerDomain.BuyerBuilder().withDTO(getBuyer()).build().getBuyer();
        Address address = new Address();
        address.setId(getId());
        address.setStreet(getStreet());
        address.setCity(getCity());
        address.setTown(getTown());
        address.setCountry(getCountry());
        address.setZipcode(getZipcode());
        address.setBuyer(buyer);
        return address;
    }
}
