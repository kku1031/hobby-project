package com.example.hobbyproject.error;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class CreateResponseError {

    //클라이언트로부터 전송된 데이터의 유효성 검사에 실패한 경우 발생하는 오류 정보를 담는 클래스

    private String field;
    private String message;

    public static CreateResponseError of(FieldError e) {
        return CreateResponseError.builder()
                .field(e.getField())
                .message(e.getDefaultMessage())
                .build();
    }

    public static List<CreateResponseError> of(List<ObjectError> errors) {
        List<CreateResponseError> responseErrors = new ArrayList<>();
        if (errors != null) {
            errors.stream().forEach((e) -> {
                responseErrors.add(CreateResponseError.of((FieldError) e));
            });
        }
        return responseErrors;
    }
}
