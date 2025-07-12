package com.glowcorner.backend.model.mapper.CreateMapper.User.Manager;

import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.enums.Status.UserStatus;
import com.glowcorner.backend.model.DTO.request.User.CreateUserRequest;
import com.glowcorner.backend.service.implement.CounterServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestMapper {

    private final CounterServiceImpl counterServiceImpl;

    public CreateUserRequestMapper(CounterServiceImpl counterServiceImpl) {
        this.counterServiceImpl = counterServiceImpl;
    }

    public User fromCreateRequest(CreateUserRequest request) {
        if (request == null){
            return null;
        }

        User user = new User();
        user.setUserID(counterServiceImpl.getNextUserID());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setSkinType(request.getSkinType());
        return user;
    }

}
