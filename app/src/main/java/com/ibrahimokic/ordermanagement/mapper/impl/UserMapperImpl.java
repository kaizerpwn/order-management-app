package com.ibrahimokic.ordermanagement.mapper.impl;

import com.ibrahimokic.ordermanagement.domain.dto.UserDto;
import com.ibrahimokic.ordermanagement.domain.entity.User;
import com.ibrahimokic.ordermanagement.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements Mapper<User, UserDto> {
    private final ModelMapper modelMapper;
    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public UserDto mapTo(User user) {
        return modelMapper.map(user, UserDto.class);
    }
    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
    @Override
    public List<User> mapListToEntityList(List<UserDto> userDtos) {
        return userDtos.stream()
                .map(this::mapFrom)
                .collect(Collectors.toList());
    }
    public List<UserDto> mapUserListToDtoList(List<User> users) {
        return users.stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }
}
