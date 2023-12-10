package com.example.hobbyproject.model;

import com.example.hobbyproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private Long id;
    private String email;
    private String userName;
    private String password;
    private String phone;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    public static UserModel fromEntity(User user) {
        return UserModel.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .password(user.getPassword())
                .phone(user.getPhone())
                .regDate(user.getRegDate())
                .updateDate(user.getUpdateDate())
                .build();
    }
}
