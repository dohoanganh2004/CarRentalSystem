package com.example.rentalcarsystem.mapper;

import com.example.rentalcarsystem.dto.request.user.RegisterRequestDTO;
import com.example.rentalcarsystem.dto.response.user.ProfileResponseDTO;
import com.example.rentalcarsystem.dto.response.user.RegisterResponseDTO;
import com.example.rentalcarsystem.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role.id", source = "roleId")
    User toUser(RegisterRequestDTO dto);


    @Mapping(source = "role.roleName", target = "roleName")
//    @Mapping(source = "phoneNo", target = "phoneNo")
    RegisterResponseDTO toRegisterResponseDTO(User user);

    @Mapping(source = "phoneNo", target = "phoneNumber")
    @Mapping(source = "email", target = "emailAddress")
    @Mapping(source = "dateOfBirth", target = "birthDate")
    ProfileResponseDTO toProfileResponseDTO(User user);


}
