package com.example.hobbyproject.entity;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
//서버 측에서는 엔터티가 데이터베이스와의 매핑 및 비즈니스 로직을 담당
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    //글 작성 시 동일 제목, 내용 방지(중복 제거)
    @Column(unique = true)
    private String title;

    @Column(unique = true)
    private String contents;

    @Column
    private String writer;

    //MariaDB에서는 자동으로 카멜 케이스를 언더스코어 케이스로 매핑
    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updateDate;

    @Column
    private int hits;

    @Column
    private int likes;

    //삭제여부를 구분할 수 있는 플래그 값으로 처리. 레코드는 남겨둠(초기에는 물리데이터를 그냥 삭제 했다가 주석과 같은 이유로 수정)
    //법적 제약조건 : 고객정보를 일정기간 보관해야 하는 경우, 플래그성으로 처리 할 수 있음.
    //성능 및 관리 측면 : 1년 후에는 다른 저장소로 백업하거나(저널링이라 부름) 폐기하는 방안.
    @Column
    private boolean deleted;

    @Column
    private LocalDateTime deletedDate;

}
