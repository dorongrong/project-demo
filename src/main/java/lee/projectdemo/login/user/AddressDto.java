package lee.projectdemo.login.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


import java.util.Objects;

@Data
public class AddressDto {

    @NotEmpty(message = "우편번호 및 도로명 주소를 입력해주세요.")
    private String zipcode;
    private String streetAdr;

    @NotEmpty(message = "상세주소를 입력해주세요.")
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
