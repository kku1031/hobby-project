package com.example.hobbyproject.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.hobbyproject.entity.Post;
import com.example.hobbyproject.entity.PostLike;
import com.example.hobbyproject.entity.User;
import com.example.hobbyproject.exception.UserException;
import com.example.hobbyproject.model.post.PostResponse;
import com.example.hobbyproject.model.user.*;
import com.example.hobbyproject.repository.PostLikeRepository;
import com.example.hobbyproject.repository.PostRepository;
import com.example.hobbyproject.repository.UserRepository;
import com.example.hobbyproject.type.UserExceptionType;
import com.example.hobbyproject.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    // 특정 회원 정보 조회(보안상 비밀번호, 가입일, 회원 정보 수정일 노출 X)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        return UserResponse.of(user);
    }

    // 특정 회원이 작성한 글 목록 반환(삭제일과 삭제자 아이디는 보안상 내리지 않음, 작성자의 아이디와 이름만 내림)
    @Transactional
    public List<PostResponse> getPostsByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        List<Post> postList = postRepository.findByUser(user);

        List<PostResponse> postResponseList = new ArrayList<>();

        postList.stream().forEach((e) -> {
            postResponseList.add(PostResponse.of(e));
        });

        return postResponseList;
    }

    // 중복된 이메일 검증
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserExceptionType.DUPLICATE_EMAIL);
        }
    }

    // 회원 등록
    public UserModel registerUser(UserInput userInput) {

        validateEmail(userInput.getEmail());

        //암호화하여 저장
        String encryptPassword = PasswordUtils.hashPassword(userInput.getPassword());

        // 새로운 사용자 엔터티 생성
        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(encryptPassword)
                .phone(userInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();

        // 사용자 엔터티를 저장소에 저장
        User savedUser = userRepository.save(user);

        return UserModel.fromEntity(savedUser);
    }

    // 회원 수정
    @Transactional
    public UserModel updateUser(Long id, UserUpdate userUpdate) {
        // 회원 id 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        // 사용자 정보가 있는 경우에만 연락처를 업데이트.
        user.setPhone(userUpdate.getPhone());
        user.setUpdateDate(LocalDateTime.now());

        // 업데이트된 유저 정보를 저장하고 반환.
        User updatedUser = userRepository.save(user);
        return UserModel.fromEntity(userRepository.save(user));
    }

    // 회원 비밀번호 수정
    @Transactional
    public UserModel updateUserPassword(Long id, UserInputPassword userInputPassword) {

        User user = userRepository.findByIdAndPassword(id, userInputPassword.getPassword())
                .orElseThrow(() -> new UserException(UserExceptionType.PASSWORD_NOT_MATCH));

        //암호화되어서 저장
        String newEncryptedPassword = PasswordUtils.hashPassword(userInputPassword.getNewPassword());
        user.setPassword(newEncryptedPassword);

        return UserModel.fromEntity(userRepository.save(user));
    }

    // 회원 삭제(성매매,불법도박 등 범죄 후기글. 회원 정보는 삭제하지만, 그 사람이 쓴 글은 추론 할 수 있게 남겨둠.)
    public UserModel deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        // 사용자 엔터티 삭제
        userRepository.delete(user);

        // 삭제된 사용자 정보를 UserModel로 변환하여 반환
        return UserModel.fromEntity(user);
    }

    // 회원 ID(이메일)을 찾기(이름과 전화번호에 해당하는 이메일을 찾는다)
    public UserResponse findUser(UserInputFind userInputFind) {

        User user = userRepository.findByUserNameAndPhone(userInputFind.getUserName(), userInputFind.getPhone())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        return UserResponse.of(user);
    }

    // 회원 비밀번호 초기화
    public UserModel resetUserPassword(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        String resetPassword = PasswordUtils.getResetPassword();
        String resetEncryptPassword = PasswordUtils.hashPassword(resetPassword);

        user.setPassword(resetEncryptPassword);

        User save = userRepository.save(user);

        return UserModel.fromEntity(save);
    }

//        원래는 외부 API와 문자 연동해야함.
//        String message = String.format("[%s]님의 임시 비밀번호가 [%s]로 초기화 되었습니다."
//                , user.getUserName()
//                , resetPassword);
//        sendSMS(message);

    // 내가 좋아요한  게시글을 보는 API
    public List<PostLike> likePost(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        List<PostLike> postLikeList = postLikeRepository.findByUser(user);

        return postLikeList;
    }

    /// JWT 토큰 발행 API(회원 이메일, 비밀번호 이용)
    public UserLoginToken createToken(UserLogin userLogin) {

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new UserException(UserExceptionType.PASSWORD_NOT_MATCH);
        }

        String token = JWT.create()
                .withExpiresAt(new Date())
                .withClaim("user_id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("project".getBytes()));

        return UserLoginToken.builder().token(token).build();
    }
}
