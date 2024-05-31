package com.demo.service.logic;

import com.demo.config.PasswordEncryptor;
import com.demo.dto.UserDTO;
import com.demo.exception.user.UserAlreadyExistsException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.user.UserRole;
import com.demo.model.user.UserEntity;
import com.demo.repository.UserRepository;
import com.demo.service.layer.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;

    // Create
    public boolean saveUser(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByEmailIgnoreCase(userDTO.getEmail()) != null
        || userRepository.findByUsername(userDTO.getUsername()) != null) {
            return false;
            //throw new UserAlreadyExistsException("User already exists!");
        }
        UserEntity user = new UserEntity();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(
                passwordEncryptor.passwordEncoder()
                .encode(userDTO.getPassword())
        );
        user.setEmail(userDTO.getEmail());
        user.setRole(UserRole.ROLE_USER);

        userRepository.save(user);
        return true;
    }

    // Read
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    public UserEntity getUserById(Long id) throws UserNotExistsException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new UserNotExistsException("User with id: "+id+" does not exist!");
        return optionalUser.get();
    }
    public UserEntity getUserByUsername(String username) throws UserNotExistsException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotExistsException("User with username: "+username+" does not exist!");
        return user;
    }

    // Update
    public void modifyUserById(Long id, UserDTO newUserData) throws UserNotExistsException {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new UserNotExistsException("User with id: "+ id +" does not exist!");
        UserEntity existingUser = optionalUser.get();
        if (newUserData.getFirstName() != null)
            existingUser.setFirstName(newUserData.getFirstName());
        if (newUserData.getLastName() != null)
            existingUser.setLastName(newUserData.getLastName());
        if (newUserData.getEmail() != null)
            existingUser.setEmail(newUserData.getEmail());
        if (newUserData.getUsername() != null)
            existingUser.setUsername(newUserData.getUsername());
        userRepository.save(existingUser);
    }

    // Delete
    public void deleteUserById(Long id) throws UserNotExistsException{
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new UserNotExistsException("User with ID "+ id + " was not found!");
        userRepository.deleteById(id);
    }

    // Adding ADMIN role to user
    public boolean grantAdminPrivileges(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null)
            return false;
        user.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user);
        return true;
    }

    // Removing ADMIN role from user
    public boolean takeAdminPrivileges(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null)
            return false;

        user.setRole(UserRole.ROLE_USER);
        userRepository.save(user);
        return true;
    }
}
