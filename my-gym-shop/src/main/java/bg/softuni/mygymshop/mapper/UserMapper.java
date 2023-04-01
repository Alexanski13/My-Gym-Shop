package bg.softuni.mygymshop.mapper;

import bg.softuni.mygymshop.model.dtos.UserDTO;
import bg.softuni.mygymshop.model.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDto(UserEntity user) {
        return new UserDTO()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setAge(user.getAge());
    }

    public UserEntity toEntity(UserDTO userDto) {
        return new UserEntity()
                .setId(userDto.getId())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setEmail(userDto.getEmail())
                .setUsername(userDto.getUsername())
                .setAge(userDto.getAge());
    }
}
