package com.trepudox.authserver.core.mapper;

import com.trepudox.authserver.dataprovider.dto.UserDTO;
import com.trepudox.authserver.dataprovider.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userModelToDTO(UserModel userModel);

}
