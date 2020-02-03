package com.sp.ScientificPublications.dto;

import com.sp.ScientificPublications.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    @NotEmpty(message = "Provide email.")
    private String email;

    @NotEmpty(message = "Provide password.")
    private String password;

    @NotEmpty(message = "Provide firstname.")
    private String firstname;

    @NotEmpty(message = "Provide lastname.")
    private String lastname;

    private String token;

    public UserDTO(User user) {
        super();
        email = user.getEmail();
        firstname = user.getFirstname();
        lastname = user.getLastname();
    }
}
