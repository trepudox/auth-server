package com.trepudox.authserver.core.usecase;

import com.trepudox.authserver.dataprovider.dto.JwtResponseDTO;

public interface IValidateCredentialsUseCase {

    JwtResponseDTO validate(String username, String sentEncodedPassword);

}
