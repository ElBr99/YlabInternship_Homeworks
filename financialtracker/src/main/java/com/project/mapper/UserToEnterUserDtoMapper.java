package com.project.mapper;


import com.project.dtos.EnterUserDto;
import com.project.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserToEnterUserDtoMapper {

    UserToEnterUserDtoMapper INSTANCE = Mappers.getMapper(UserToEnterUserDtoMapper.class);

    EnterUserDto userToEnterUserDto(User user);


}
