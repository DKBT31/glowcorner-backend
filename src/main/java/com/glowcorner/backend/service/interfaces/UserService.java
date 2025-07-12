package com.glowcorner.backend.service.interfaces;

import com.glowcorner.backend.model.DTO.User.UserDTOByCustomer;
import com.glowcorner.backend.model.DTO.User.UserDTOByManager;
import com.glowcorner.backend.model.DTO.User.UserDTOByStaff;
import com.glowcorner.backend.model.DTO.request.User.CreateCustomerRequest;
import com.glowcorner.backend.model.DTO.request.User.CreateUserRequest;

import java.util.List;

public interface UserService {

    /* Manager */
    UserDTOByManager updateUserByManager(String userID, UserDTOByManager userDTOByManager);
    void deleteUser(String userId);
    UserDTOByManager createUser(CreateUserRequest request);

    List <UserDTOByManager> searchUserByNameManager(String name);
    UserDTOByManager getUserById(String userID);
    List<UserDTOByManager> getAllUsers();
    UserDTOByManager getUserByEmail(String email);

    /* Customer */
    UserDTOByCustomer updateUserByCustomer(String userID, UserDTOByCustomer userDTOByCustomer);
    UserDTOByCustomer createCustomer(CreateCustomerRequest request);

    UserDTOByCustomer getUserByIdForCustomer(String userId);

    /* Staff */

    UserDTOByStaff getStaffById(String userID);
    UserDTOByStaff updateUserByStaff(String userID, UserDTOByStaff userDTOByStaff);

}
