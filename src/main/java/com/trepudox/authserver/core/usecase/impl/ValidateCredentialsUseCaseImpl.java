package com.trepudox.authserver.core.usecase.impl;

import com.trepudox.authserver.core.exception.APIException;
import com.trepudox.authserver.core.usecase.IUpdateLoginAttemptsUseCase;
import com.trepudox.authserver.core.usecase.IValidateCredentialsUseCase;
import com.trepudox.authserver.core.usecase.IVerifyCurrentLoginAttemptsUseCase;
import com.trepudox.authserver.dataprovider.dto.JwtResponseDTO;
import com.trepudox.authserver.dataprovider.model.UserModel;
import com.trepudox.authserver.dataprovider.repository.UserRepository;
import com.trepudox.authserver.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidateCredentialsUseCaseImpl implements IValidateCredentialsUseCase {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final IUpdateLoginAttemptsUseCase updateLoginAttemptsUseCase;
    private final IVerifyCurrentLoginAttemptsUseCase verifyCurrentLoginAttemptsUseCase;

    public JwtResponseDTO validate(String username, String sentEncodedPassword) {
        UserModel user = userRepository.findById(username)
                .orElseThrow(() -> new APIException("Login incorreto", "Usuário não encontrado na base de dados", 403));

        verifyCurrentLoginAttemptsUseCase.verify(user);

        if(user.getPassword().equals(sentEncodedPassword)) {
            return jwtTokenUtil.generateToken(user);
        } else {
            updateLoginAttemptsUseCase.update(user);
            return null;
        }

    }

}
