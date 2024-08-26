
![제목 없음-1](https://github.com/user-attachments/assets/20fbcf40-f860-44d5-bdee-c29ab3f00720)


### 온라인 미술품 경매 사이트
# 🚩 프로젝트 정보
- 웹 URL : https://www.a-auc.art/
- DB PORT : 3306
- DB username : root
- 데이터베이스 이름 : ArtAuction_db

# 💬 프로젝트 설명
- 미술 작품을 온라인에서 경매를 통해 구매(입찰)/판매 하는 기능 제공
- 검증된 아티스트와 손수 작업된 작품을 전시하고 소장을 위한 경매 제공
- 투명한 경매, 공정한 거래, 안전한 작품 이전을 목표로 둠

# 💨 개발 기간
- 2024 7.15 ~ 2024 9.3

# 👥 개발 팀원
|                                                               **임재원**                                                               |                                                                **박지완**                                                                |                                                               **김민섭**                                                                |                                                               **김채연**                                                                |
|:-----------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------:|
| [<img src="https://avatars.githubusercontent.com/u/30003447?v=4" height=150 width=150> <br/> @maegon](https://github.com/maegon) | [<img src="https://avatars.githubusercontent.com/u/52211840?v=4" height=150 width=150> <br/> @tron0318](https://github.com/tron0318) | [<img src="https://avatars.githubusercontent.com/u/161572638?v=4" height=150 width=150> <br/> @kimminseop99](https://github.com/kimminseop99) | [<img src="https://avatars.githubusercontent.com/u/90239727?v=4" height=150 width=150> <br/> @kim-chaeyeon](https://github.com/kim-chaeyeon) |

# 🛠 개발 환경
- 운영체제 : Windows 10, 11
- 통합개발환경(IDE) : IntelliJ
- JDK 버전 : JDK 21
- 데이터 베이스 : MySQL
- 빌드 툴 : Gradle
- 관리 툴 : GitHub
- 배포 툴 : Nginxproxymanager, Termius

# ✨ Dependencies
- Spring Boot DevTools
- Lombok
- Spring Data JPA
- MariaDB Driver
- Spring Security
- Spring Web
- Oauth2-client
- Thymeleaf
- Validation
- spring boot starter mail
- Toss Payments
- daumcdn 주소 찾기(다음 카카오 API)

# 💻 기술 스택

### Version Control
<div>
    <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
    <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>

### Backend Technologies
<div>
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
    <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
    <img src="https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=node.js&logoColor=white">
</div>

### Frontend Technologies
<div>
    <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white">
    <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
    <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white">
    <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
    <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">
    <img src="https://img.shields.io/badge/Tailwind CSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white">
    <img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white">
</div>

### Databases
<div>
    <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
    <img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
</div>


# 역할 분담
### 임재원
+ UI, 페이지
  - 로그인, 회원가입, 계정찾기(아이디 찾기, 비밀번호 찾기)
  - 관리자(경매 내역 관리, 신청된 경매 작품 확인, 회원 권한 설정)
+ 기능
  - 소셜 계정으로 로그인 기능
  - 회원가입을 통한 로그인
  - 회원 가입 시 이메일 인증, 카카오 API를 통한 주소 입력
  - 이메일 발송을 통한 아이디 찾기
  - 이메일 발송을 통한 임시 비밀번호 발급(비밀번호 찾기)
  - 경매 내역 리스팅 및 관리
  - 작가가 신청한 작품 리스팅 및 관리
  - 회원 리스팅 및 관리
  - 회원의 권한(role)을 관리자가 변경하여 관리(role 값은 admin, artist, member로 나뉨)


### 박지완
+ UI, 페이지
+ 기능

### 김민섭
+ UI, 페이지
  - 메인
  - 헤더(경매, 작품, 캘린더, 검색)
  - 푸터
  - 그림판
  - 충전
  - 상세내역(주문, 배송, 작품, 진행중인 경매, 예정된 경매)
  - 경매 가이드(howToBuy, howToSell)
  - 예정된 경매 
  - 관리자(경매 추가)
  - 작가(작품 추가)
+ 기능
  - 캘린더 (fullcalendar API 사용)
  - 경매(경매 응찰, 응찰 취소, 낙찰)
  - 알림(예정된 경매 1시간전 알림메일, 경매가 끝난 후 작가, 낙찰자, 응찰자에게 경매 결과 알림메일)
  - 충전(tossPayment 사용)
  - 그림판(그림 그리기, 저장)

### 김채연
+ UI, 페이지
+ 기능
