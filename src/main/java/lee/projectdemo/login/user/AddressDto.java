package lee.projectdemo.login.user;

import lombok.Data;


import java.util.Objects;

@Data
public class AddressDto {

    private String zipcode;
    private String streetAdr;
    private String detailAdr;

    protected AddressDto() {
    }

    public AddressDto(String zipcode, String streetAdr, String detailAdr) {
        this.zipcode = zipcode;
        this.streetAdr = streetAdr;
        this.detailAdr = detailAdr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDto address = (AddressDto) o;
        return Objects.equals(zipcode, address.zipcode) && Objects.equals(streetAdr, address.streetAdr) && Objects.equals(detailAdr, address.detailAdr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipcode, streetAdr, detailAdr);
    }
}
