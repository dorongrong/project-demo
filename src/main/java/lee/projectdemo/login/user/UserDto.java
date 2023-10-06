package lee.projectdemo.login.user;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotEmpty(message = "아이디를 입력해주세요.")
    @Size(min = 5, max = 12, message = "아이디는 5자 이상 12자 이하로 입력해주세요.")
    private String loginId;

    @NotEmpty(message = "이름을 입력해주세요.")
    private String loginName;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자로 포함해서 6~12자리 이내로 입력해주세요.")
    private String password;

    @Valid //주소 검증을 위해
    private AddressDto addressDto;

    public UserDto(String loginId, String loginName, String password, AddressDto address){
        this.loginId = loginId;
        this.loginName = loginName;
        this.password = password;
        this.addressDto = address;
    }

    public UserDto(){

    }

}
