package com.trepudox.authserver.core.usecase;

import com.trepudox.authserver.dataprovider.dto.UserDTO;
import com.trepudox.authserver.dataprovider.dto.UserCredentialsDTO;

public interface IRegisterNewUserUseCase {

    UserDTO register(UserCredentialsDTO credentials);

}
