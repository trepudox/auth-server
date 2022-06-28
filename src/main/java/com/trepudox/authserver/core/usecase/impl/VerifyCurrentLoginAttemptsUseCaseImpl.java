package com.trepudox.authserver.core.usecase.impl;

import com.trepudox.authserver.core.exception.APIException;
import com.trepudox.authserver.core.usecase.IVerifyCurrentLoginAttemptsUseCase;
import com.trepudox.authserver.dataprovider.model.UserModel;
import com.trepudox.authserver.dataprovider.model.LoginAttemptsModel;
import com.trepudox.authserver.dataprovider.repository.LoginAttemptsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerifyCurrentLoginAttemptsUseCaseImpl implements IVerifyCurrentLoginAttemptsUseCase {

    @Value("${security.login.maxAttempts}")
    private Long maxAttempts;

    private static final String ERROR_TITLE = "Usuário temporariamente bloqueado";

    private static final String ERROR_DETAIL = "Seu usuário excedeu o limite de tentativas de login, tente novamente mais tarde";

    private final LoginAttemptsRepository loginAttemptsRepository;

    @Override
    public void verify(UserModel user) {
        Optional<LoginAttemptsModel> loginAttemptsOptional = loginAttemptsRepository.findById(user.getUsername());

        if(loginAttemptsOptional.isPresent()) {
            LoginAttemptsModel loginAttempts = loginAttemptsOptional.get();

            if(loginAttempts.getCurrentAttempt() >= maxAttempts) {
                log.warn("Usuário {} bloqueado para fazer login, tentativas excedidas.", user.getUsername());
                throw new APIException(ERROR_TITLE, ERROR_DETAIL, 403);
            }
        }
    }
}
