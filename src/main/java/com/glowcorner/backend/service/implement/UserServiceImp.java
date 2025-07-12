package com.glowcorner.backend.service.implement;

import com.glowcorner.backend.entity.mongoDB.Cart;
import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.entity.mongoDB.Authentication;
import com.glowcorner.backend.enums.Status.UserStatus;
import com.glowcorner.backend.model.DTO.User.UserDTOByCustomer;
import com.glowcorner.backend.model.DTO.User.UserDTOByManager;
import com.glowcorner.backend.model.DTO.User.UserDTOByStaff;
import com.glowcorner.backend.model.DTO.request.User.CreateCustomerRequest;
import com.glowcorner.backend.model.DTO.request.User.CreateUserRequest;
import com.glowcorner.backend.model.mapper.CreateMapper.User.Customer.CreateCustomerRequestMapper;
import com.glowcorner.backend.model.mapper.CreateMapper.User.Manager.CreateUserRequestMapper;
import com.glowcorner.backend.model.mapper.User.*;
import com.glowcorner.backend.repository.AuthenticationRepository;
import com.glowcorner.backend.repository.CartRepository;
import com.glowcorner.backend.repository.UserRepository;
import com.glowcorner.backend.service.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final UserMapperManager userMapperManager;

    private final UserMapperCustomer userMapperCustomer;

    private final CreateUserRequestMapper createUserRequestMapper;

    private final CreateCustomerRequestMapper customerCreateRequestMapper;

    private final AuthenticationRepository authenticationRepository;

    private final CartRepository cartRepository;

    public UserServiceImp(
            UserRepository userRepository,
            UserMapperManager userMapperManager,
            UserMapperCustomer userMapperCustomer,
            CreateUserRequestMapper createUserRequestMapper,
            CreateCustomerRequestMapper customerCreateRequestMapper,
            AuthenticationRepository authenticationRepository,
            CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.userMapperManager = userMapperManager;
        this.userMapperCustomer = userMapperCustomer;
        this.createUserRequestMapper = createUserRequestMapper;
        this.customerCreateRequestMapper = customerCreateRequestMapper;
        this.authenticationRepository = authenticationRepository;
        this.cartRepository = cartRepository;
    }

    /* Manager */

    // Get all users
    @Override
    public List<UserDTOByManager> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapperManager::toUserDTO)
                .toList();
    }

    // Get user by ID
    @Override
    public UserDTOByManager getUserById(String userId) {
        if(userRepository.findByUserID(userId).isPresent())
            return userMapperManager.toUserDTO(userRepository.findByUserID(userId).get());
        return null;
    }

    // Create a new user
    @Override
    public UserDTOByManager createUser(CreateUserRequest request) {
        User user = createUserRequestMapper.fromCreateRequest(request);
        user = userRepository.save(user);
        return userMapperManager.toUserDTO(user);
    }

    @Override
    public UserDTOByManager updateUserByManager(String userID, UserDTOByManager userDTOByManager) {
        try {
            // Tìm user trong DB
            User existingUser = userRepository.findByUserID(userID)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Cập nhật thông tin user
            if (userDTOByManager.getFullName() != null) existingUser.setFullName(userDTOByManager.getFullName());
            if (userDTOByManager.getEmail() != null) existingUser.setEmail(userDTOByManager.getEmail());
            if (userDTOByManager.getPhone() != null) existingUser.setPhone(userDTOByManager.getPhone());
            if (userDTOByManager.getAddress() != null) existingUser.setAddress(userDTOByManager.getAddress());
            if (userDTOByManager.getSkinType() != null) existingUser.setSkinType(userDTOByManager.getSkinType());
            if (userDTOByManager.getAvatar_url() != null) existingUser.setAvatar_url(userDTOByManager.getAvatar_url());
            if (userDTOByManager.getOrders() != null) existingUser.setOrders(userDTOByManager.getOrders());

            // Nếu có thay đổi role
            if (userDTOByManager.getRole() != null && !userDTOByManager.getRole().equals(existingUser.getRole())) {
                existingUser.setRole(userDTOByManager.getRole());

                // Tìm authentication của user
                Optional<Authentication> authOptional = authenticationRepository.findByUserID(existingUser.getUserID());
                if (authOptional.isPresent()) {
                    Authentication auth = new Authentication();
                    auth.setUserID(existingUser.getUserID());
                    authenticationRepository.save(auth);

                }
            }

            // Lưu lại user
            User updatedUser = userRepository.save(existingUser);

            return userMapperManager.toUserDTO(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Fail to update user: " + e.getMessage(), e);
        }
    }



    // Delete a user
    @Override
    public void deleteUser(String userID) {
        User existingUser = userRepository.findByUserID(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setStatus(UserStatus.DISABLE); // Assuming UserStatus enum has DISABLE status
        userRepository.save(existingUser);
    }

    // Search user by name
    @Override
    public List<UserDTOByManager> searchUserByNameManager(String name) {
        List<User> users = userRepository.findByFullNameContainingIgnoreCase(name);
        return users.stream()
                .map(userMapperManager::toUserDTO)
                .toList();
    }


    /* Customer
     * */

    // Create a customer account
    @Override
    public UserDTOByCustomer createCustomer(CreateCustomerRequest request) {
        User user = customerCreateRequestMapper.fromCreateRequest(request);
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        user.setCart(cart);
        cartRepository.save(cart);
        user = userRepository.save(user);
        return userMapperCustomer.toUserDTO(user);
    }

    @Override
    public UserDTOByCustomer getUserByIdForCustomer(String userId) {
        User user = userRepository.findByUserID(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapperCustomer.toUserDTO(user);
    }

    @Override
    public UserDTOByStaff getStaffById(String userID) {
        return null;
    }

    @Override
    public UserDTOByStaff updateUserByStaff(String userID, UserDTOByStaff userDTOByStaff) {
        return null;
    }

    // Users update themselves
    @Override
    public UserDTOByCustomer updateUserByCustomer(String userID, UserDTOByCustomer userDTOByCustomer) {
        try {
            //Find existing user
            User existingUser = userRepository.findByUserID(userID)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            //Update
            if(userDTOByCustomer.getFullName() != null) existingUser.setFullName(userDTOByCustomer.getFullName());
            if(userDTOByCustomer.getEmail() != null) existingUser.setEmail(userDTOByCustomer.getEmail());
            if(userDTOByCustomer.getPhone() != null) existingUser.setPhone(userDTOByCustomer.getPhone());
            if(userDTOByCustomer.getAddress() != null) existingUser.setAddress(userDTOByCustomer.getAddress());
            if(userDTOByCustomer.getSkinType() != null) existingUser.setSkinType(userDTOByCustomer.getSkinType());
            if(userDTOByCustomer.getAvatar_url() != null) existingUser.setAvatar_url(userDTOByCustomer.getAvatar_url());

            //Save update
            User updatedUser = userRepository.save(existingUser);

            //Convert updated user entity to DTO
            return userMapperCustomer.toUserDTO(updatedUser);
        } catch (Exception e) {
            throw  new RuntimeException("Fail to update user: " + e.getMessage(), e);
        }
    }

    // Get user by email
    @Override
    public UserDTOByManager getUserByEmail(String email) {
        if (!email.isEmpty()) {
            User user = userRepository.findByEmail(email).orElse(null);
            return userMapperManager.toUserDTO(user);
        }
        return null;
    }

}
