package com.trepudox.authserver.core.usecase.impl;

import com.trepudox.authserver.core.exception.APIException;
import com.trepudox.authserver.core.mapper.UserMapper;
import com.trepudox.authserver.core.usecase.IRegisterNewUserUseCase;
import com.trepudox.authserver.dataprovider.dto.UserDTO;
import com.trepudox.authserver.dataprovider.dto.UserCredentialsDTO;
import com.trepudox.authserver.dataprovider.enums.EnProfile;
import com.trepudox.authserver.dataprovider.model.UserModel;
import com.trepudox.authserver.dataprovider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegisterNewUserUseCaseImpl implements IRegisterNewUserUseCase {

    private static final String ERROR_TITLE = "Não foi possível realizar o cadastro";
    private static final String ERROR_DETAIL = "Este usuario já está cadastrado";

    private final UserRepository userRepository;

    @Override
    public UserDTO register(UserCredentialsDTO credentials) {
        if(userRepository.existsById(credentials.getUsername()))
            throw new APIException(ERROR_TITLE, ERROR_DETAIL, 422);

        UserModel user = UserModel.builder()
                .username(credentials.getUsername())
                .password(credentials.getPassword())
                .profile(EnProfile.LEITOR)
                .score(0L)
                .creationDateTime(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return UserMapper.INSTANCE.userModelToDTO(user);
    }
}
