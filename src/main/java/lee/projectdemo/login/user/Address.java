package lee.projectdemo.login.user;


import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter // @Data는 임베디드 타입은 참조객체이기 때문에 잘못하면 오류를 야기할 수 있음
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
