package com.example.hobbyproject.controller;

import com.example.hobbyproject.entity.PostLike;
import com.example.hobbyproject.error.CreateResponseError;
import com.example.hobbyproject.model.post.PostResponse;
import com.example.hobbyproject.model.user.*;
import com.example.hobbyproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 특정 회원 정보 조회(보안상 비밀번호, 가입일, 회원 정보 수정일 노출 X)
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        UserResponse userResponse = userService.getUserById(id);

        return ResponseEntity.ok(userResponse);
    }

    // 특정 회원이 작성한 글 목록 반환
    @GetMapping("/{lists}/{id}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Long id) {
        List<PostResponse> postsByUserId = userService.getPostsByUserId(id);
        return ResponseEntity.ok(postsByUserId);
    }

    // 회원 등록
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserInput userInput,
                                          Errors errors) {

        //필수 항목 미입력시 에러 처리(Object는 FieldError 반환)
        if (errors.hasErrors()) {
            List<CreateResponseError> responseErrors = new ArrayList<>();

            errors.getAllErrors().forEach(e -> {
                responseErrors.add(CreateResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        UserModel registeredUser = userService.registerUser(userInput);

        return ResponseEntity.ok(registeredUser);

    }

    // 회원 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody @Valid UserUpdate userUpdate) {

        UserModel updatedUser = userService.updateUser(id, userUpdate);

        return ResponseEntity.ok(updatedUser);
    }

    // 비밀번호 수정(리소스의 부분적인 수정은 patch가 적절)
    // 회원 엔터티에 여러 필드가 있고, 비밀번호만을 업데이트한다면 @PatchMapping
    // 비밀번호만을 가지고 있는 경우에는 @PutMapping 적절.
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id,
                                                @RequestBody @Valid UserInputPassword userInputPassword,
                                                Errors errors) {

        if (errors.hasErrors()) {
            List<CreateResponseError> responseErrors = new ArrayList<>();

            errors.getAllErrors().forEach(e -> {
                responseErrors.add(CreateResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        UserModel updateUserPassword = userService.updateUserPassword(id, userInputPassword);

        return ResponseEntity.ok(updateUserPassword);
    }

    // 회원 삭제(성매매,불법도박 등 범죄 후기글. 회원 정보는 삭제하지만, 그 사람이 쓴 글은 추론 할 수 있게 남겨둠.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        UserModel deletedUser = userService.deleteUser(id);
        return ResponseEntity.ok(deletedUser);
    }

    // 회원 ID(이메일)을 찾기(이름과 전화번호에 해당하는 이메일을 찾는다)
    @GetMapping("/find")
    public ResponseEntity<?> findUser(@RequestBody UserInputFind userInputFind) {

        UserResponse userResponse = userService.findUser(userInputFind);

        return ResponseEntity.ok(userResponse);

    }

    // 회원 비밀번호 초기화(리소스 수정에 더 가까워서 PUT 사용)
    @PutMapping("/{id}/password/reset")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id) {

        UserModel userModel = userService.resetUserPassword(id);

        return ResponseEntity.ok(userModel);
    }

    // 내가 좋아요한 게시글을 보는 API
    @GetMapping("/{id}/posts/like")
    public ResponseEntity<?> likePost(@PathVariable Long id) {

        List<PostLike> postLikeList = userService.likePost(id);

        return ResponseEntity.ok(postLikeList);
    }

    // JWT 토큰 발행 API(회원 이메일, 비밀번호 이용)
    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin,
                                         Errors errors) {

        List<CreateResponseError> responseErrorList = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(CreateResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        UserLoginToken token = userService.createToken(userLogin);
        return ResponseEntity.ok(token);
    }


}