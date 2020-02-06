package com.sp.ScientificPublications.dto;

import com.sp.ScientificPublications.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	
    @NotEmpty(message = "Provide email.")
    private String email;

    @NotEmpty(message = "Provide password.")
    private String password;

    @NotEmpty(message = "Provide firstname.")
    private String firstname;

    @NotEmpty(message = "Provide lastname.")
    private String lastname;

    private Integer rank;
    private String reviewerStatus;

    private String token;

    private List<String> roles;


    public UserDTO(User user) {
        super();
        id = user.getId();
        email = user.getEmail();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        this.roles = user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList());
    }
}
