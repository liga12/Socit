package socit.service.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Log4j
public class Passworder {

    @Getter
    @Setter
    @NotNull(message = "Password must be not null")
    @Size(min = 8, max = 248, message = "Password must be in the range from 8 to 248 characters")
    private String password;

    @Getter
    @Setter
    @NotNull(message = "passwordConfirmation must be not null")
    @Size(min = 8, max = 248, message = "PasswordConfirmation must be in the range from 8 to 248 characters")
    private String passwordConfirmation;

    public Passworder(String password, String passwordConfirmation) {
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
      log.debug("Set values: password = "+password+", passwordConfirmation = "+passwordConfirmation);
    }
}


