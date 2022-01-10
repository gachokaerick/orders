package com.gachokaerick.eshop.orders.domain.aggregates.buyer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gachokaerick.eshop.orders.domain.Address;
import com.gachokaerick.eshop.orders.domain.User;
import com.gachokaerick.eshop.orders.domain.aggregates.order.Order;
import com.gachokaerick.eshop.orders.domain.enumeration.Gender;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Erick Gachoka
 */
@Entity
@Table(name = "buyer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Buyer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @OneToOne(optional = false, cascade = { CascadeType.PERSIST })
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "buyer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "buyer" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    Buyer id(Long id) {
        this.setId(id);
        return this;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    Buyer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    Buyer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return this.gender;
    }

    Buyer gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return this.email;
    }

    Buyer email(String email) {
        this.setEmail(email);
        return this;
    }

    void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    Buyer phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return this.user;
    }

    void setUser(User user) {
        this.user = user;
    }

    Buyer user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setBuyer(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setBuyer(this));
        }
        this.addresses = addresses;
    }

    Buyer addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    Buyer addAddresses(Address address) {
        this.addresses.add(address);
        address.setBuyer(this);
        return this;
    }

    Buyer removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setBuyer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Buyer)) {
            return false;
        }
        return id != null && id.equals(((Buyer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Buyer{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", gender='" + getGender() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
