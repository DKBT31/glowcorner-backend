package com.glowcorner.backend.model.mapper.CreateMapper.User.Customer;

import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.enums.Role;
import com.glowcorner.backend.enums.Status.UserStatus;
import com.glowcorner.backend.model.DTO.request.User.CreateCustomerRequest;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class CreateCustomerRequestMapper {

    private final CounterServiceImpl counterServiceImpl;

    public CreateCustomerRequestMapper(CounterServiceImpl counterServiceImpl) {
        this.counterServiceImpl = counterServiceImpl;
    }

    public User fromCreateRequest(CreateCustomerRequest request) {
        if (request == null){
            return null;
        }

        User user = new User();
        user.setUserID(counterServiceImpl.getNextUserID());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

}
