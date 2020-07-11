package fiap.stock.portal.address.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "addresses")
public class Address {

    @Id
    private String id;

    private String loginId;
    private String zipCode;
    private String complement;
    private String city;
    private String state;
    private String country;

    public Address() {
    }

    public Address(String zipCode, String complement, String city, String state, String country) {
        this.zipCode = zipCode;
        this.complement = complement;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Address(String loginId, String zipCode, String complement, String city, String state, String country) {
        this.loginId = loginId;
        this.zipCode = zipCode;
        this.complement = complement;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getComplement() {
        return complement;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

}
