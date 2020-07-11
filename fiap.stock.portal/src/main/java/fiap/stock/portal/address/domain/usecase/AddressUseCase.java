package fiap.stock.portal.address.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.portal.address.domain.Address;
import fiap.stock.portal.address.domain.AddressService;
import fiap.stock.portal.address.domain.exception.AddressNotFoundException;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressUseCase {

    public static class AddressPayload {
        @JsonProperty
        String code;

        @JsonProperty("zip_code")
        String zipCode;

        @JsonProperty
        String complement;

        @JsonProperty
        String city;

        @JsonProperty
        String state;

        @JsonProperty
        String country;

        public AddressPayload() {
        }

        public AddressPayload(Address address) {
            this.code = address.getId();
            this.zipCode = address.getZipCode();
            this.complement = address.getComplement();
            this.city = address.getCity();
            this.state = address.getState();
            this.country = address.getCountry();
        }
    }

    private final AddressService addressService;

    public AddressUseCase(AddressService addressService) {
        this.addressService = addressService;
    }

    public AddressPayload insertNewClientAddress(String loginId, AddressPayload addressPayload) throws InvalidSuppliedDataException {
        addressService.validLoginId(loginId);

        addressService.validZipCode(addressPayload.zipCode);
        addressService.validComplement(addressPayload.complement);
        addressService.validCity(addressPayload.city);
        addressService.validState(addressPayload.state);
        addressService.validCountry(addressPayload.country);

        Address address = new Address(
                loginId,
                addressPayload.zipCode,
                addressPayload.complement,
                addressPayload.city,
                addressPayload.state,
                addressPayload.country
        );

        addressService.save(address);

        return new AddressPayload(address);
    }

    public List<AddressPayload> findAllAddresses(String loginId) throws InvalidSuppliedDataException {
        addressService.validLoginId(loginId);

        List<Address> addressList = addressService.findAllByLoginId(loginId);

        return addressList.stream()
                .map(AddressPayload::new)
                .collect(Collectors.toList());
    }

    public void removeClientAddressRecord(String loginId, String addressCode) throws InvalidSuppliedDataException, AddressNotFoundException {
        addressService.validLoginId(loginId);

        addressService.ensureAddressRecordExist(loginId, addressCode);

        addressService.removeById(addressCode);
    }

}
