package com.glowcorner.backend.model.mapper.User;

import com.glowcorner.backend.entity.mongoDB.User;
import com.glowcorner.backend.model.DTO.User.UserDTOByCustomer;
import org.springframework.stereotype.Component;


@Component
public class UserMapperCustomer {

    public UserDTOByCustomer toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTOByCustomer(
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getSkinType(),
                user.getAvatar_url(),
                user.getSkinCareRoutine()
        );
    }

}
