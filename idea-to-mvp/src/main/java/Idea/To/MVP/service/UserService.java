package Idea.To.MVP.service;


import Idea.To.MVP.DTO.UserDto;
import Idea.To.MVP.Exceptions.UserAlreadyExistException;
import Idea.To.MVP.Exceptions.UserNotFoundException;
import Idea.To.MVP.Repository.UserRepository;
import Idea.To.MVP.Request.CreateUserReq;
import Idea.To.MVP.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    public User createUser(CreateUserReq createUserReq) {
        return Optional.of(createUserReq).filter(user ->!userRepository.existsByEmail(user.getEmail()))
                .map(createUserRequest -> {
                    User user = new User();
                    user.setUserName(createUserReq.getUsername());
                    user.setPassword(createUserReq.getPassword());
                    user.setEmail(createUserReq.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(() -> new UserAlreadyExistException("There is already a user with this: " + createUserReq.getEmail() + " email"));
    }

    public void deleteUser(UUID id) {
        userRepository.findById(id).ifPresentOrElse(userRepository :: delete, () -> {
            throw new UserNotFoundException("User with id: " + id + " not found");
        });
    }

    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
