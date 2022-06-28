package com.trepudox.authserver.core.usecase.impl;

import com.trepudox.authserver.core.exception.APIException;
import com.trepudox.authserver.core.usecase.IUpdateLoginAttemptsUseCase;
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
public class UpdateLoginAttemptsUseCaseImpl implements IUpdateLoginAttemptsUseCase {

    @Value("${security.login.maxAttempts}")
    private long maxAttempts;

    private static final String ERROR_TITLE = "Login incorreto";

    private static final String ERROR_DETAIL = "Verifique se as credenciais estão corretas. Tentativa %d de %d";

    private final LoginAttemptsRepository loginAttemptsRepository;

    @Override
    public void update(UserModel user) {
        Optional<LoginAttemptsModel> loginAttemptsOptional = loginAttemptsRepository.findById(user.getUsername());

        LoginAttemptsModel loginAttempts = null;
        if(loginAttemptsOptional.isPresent()) {
            loginAttempts = loginAttemptsOptional.get();
            loginAttempts.setCurrentAttempt(loginAttempts.getCurrentAttempt() + 1);
        } else {
            loginAttempts = new LoginAttemptsModel(user.getUsername(), 1L);
        }

        loginAttemptsRepository.save(loginAttempts);
        throw new APIException(ERROR_TITLE, String.format(ERROR_DETAIL, loginAttempts.getCurrentAttempt(), maxAttempts), 403);
    }

}
