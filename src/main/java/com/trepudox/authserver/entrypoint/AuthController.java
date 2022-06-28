package com.trepudox.authserver.entrypoint;

import com.trepudox.authserver.core.usecase.IRegisterNewUserUseCase;
import com.trepudox.authserver.core.usecase.IValidateCredentialsUseCase;
import com.trepudox.authserver.dataprovider.dto.UserDTO;
import com.trepudox.authserver.dataprovider.dto.UserCredentialsDTO;
import com.trepudox.authserver.dataprovider.dto.JwtResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IValidateCredentialsUseCase validateCredentialsUseCase;
    private final IRegisterNewUserUseCase registerNewUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody UserCredentialsDTO credentials) {
        JwtResponseDTO tokenResponse = validateCredentialsUseCase.validate(credentials.getUsername(), credentials.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserCredentialsDTO credentials) {
        UserDTO newUser = registerNewUserUseCase.register(credentials);
        return ResponseEntity.status(HttpStatus.OK).body(newUser);
    }

}
