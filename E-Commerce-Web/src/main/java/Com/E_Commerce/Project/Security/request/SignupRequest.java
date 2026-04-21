package Com.E_Commerce.Project.Security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min=3,max=20)
    private String userName;
    @Email
    private String Email;

    private Set<String> role;//multiple role can send
   @NotBlank
   @Size(min=8, max = 16)
    private String password;
}
