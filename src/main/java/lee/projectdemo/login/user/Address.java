package lee.projectdemo.login.user;


import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {

    private String zipcode;
    private String streetAdr;
    private String detailAdr;

    protected Address() {
    }

    public Address(String zipcode, String streetAdr, String detailAdr) {
        this.zipcode = zipcode;
        this.streetAdr = streetAdr;
        this.detailAdr = detailAdr;
    }


























}
