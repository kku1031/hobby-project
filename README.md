# hobby-project
- **주제:** 취미 공유 서비스 플랫폼
- **설명:** 사용자는 자신의 취미에 관한 정보를 공유하고, 해당 취미에 관련된 모임을 예약할 수 있는 플랫폼입니다.
- **효과:** 사용자는 자신의 취미와 관심사에 맞는 이벤트를 쉽게 찾을 수 있으며, 게시판을 통해 정보를 공유할 수 있습니다.

## ERD

추가 예정

## 사용 기술 스택
- **언어:** ![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=java&logoColor=white)
- **프레임워크:** ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=spring&logoColor=white)
- **데이터베이스:** ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariadb&logoColor=white)
- **데이터베이스 ORM:** ![JPA](https://img.shields.io/badge/JPA-4285F4?style=flat-square&logo=jpa&logoColor=white)
- **보안:** ![Spring Security](https://img.shields.io/badge/Spring%20Security-5F6A6A?style=flat-square&logo=spring&logoColor=white), ![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=json-web-tokens&logoColor=white)

## 주요 기능

# 기능

## [회원]

- **유저**
  - 일반 회원 및 회원을 관리하는 관리자 (권한 부여)
- **회원가입**
  - 아이디, 비밀번호, 이름, 전화번호 필요
- **로그인**
  - 아이디, 비밀번호로 로그인
  - 소셜 로그인
- **Mypage**
  - 내 정보(아이디, 비밀번호)
  - 나의 활동(작성한 게시글, 댓글 등)
- **Admin Page**
  - 가입한 회원 정보 조회

## [모임 예약]

- **정렬 방식**
  - 회원 위치 기준 날짜/거리순/관심사순/추천
- **모임 생성 글**
  - 모임 생성
  - 모임 소개
  - 모임 참가/참가 취소
  - 참가 비용 결제

## [자유게시판]

- **정렬 방식**
  - 회원 위치 기준 날짜/거리순/관심사순/추천
- **게시글 검색**
  - Elastic Search 활용
- **기본 CRUD**
  - 게시글 전체 조회
  - 특정 게시글 조회
  - 게시글 조회 시 조회수 증가
  - 게시글 작성
  - 게시글 수정
  - 게시글 전체 삭제(물리+논리) : 이미 데이터 분석에 사용을 했거나, 고객 정보 보호 기한이 지났을 때 물리적으로 삭제할 필요성
  - 최근 게시글 n개 목록 반환
  
- **게시글 댓글**
- **게시글 추천**
- **파일 업로드**

## [고객문의]

- **채팅 기능**
  - 사용자가 궁금한 점 문의

## [알림]

- **고객 문의 답변 알림**
- **내가 작성한 게시글 댓글 알림**
