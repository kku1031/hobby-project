package com.example.hobbyproject.repository;

import com.example.hobbyproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndPassword(Long id, String password);

    // 회원 ID(이메일)을 찾기(이름과 전화번호에 해당하는 이메일을 찾는다)
    Optional<User> findByUserNameAndPhone(String userName, String phone);
}