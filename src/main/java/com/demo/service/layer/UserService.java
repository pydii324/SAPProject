package com.demo.service.layer;

import com.demo.dto.UserDTO;
import com.demo.exception.user.UserAlreadyExistsException;
import com.demo.exception.user.UserNotExistsException;
import com.demo.model.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Create
    boolean saveUser(UserDTO userDTO)
        throws UserAlreadyExistsException;

    // Read
    List<UserEntity> getAllUsers();
    UserEntity getUserById(Long id)
        throws UserNotExistsException;
    UserEntity getUserByUsername(String username)
            throws UserNotExistsException;

    // Update
    void modifyUserById(Long id, UserDTO newUserData)
        throws UserNotExistsException;

    // Delete
    void deleteUserById(Long id)
        throws UserNotExistsException;

    // Grant Admin Privileges to User
    boolean grantAdminPrivileges(String username);

    // Take Admin Privileges from User
    boolean takeAdminPrivileges(String username);
}
