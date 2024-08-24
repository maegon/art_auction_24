
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
- 백엔드 : SpringBoot, Spring Security, Spring Data JPA
- 프론트엔드 : HTML, CSS, Javascript, Bootstrap, Thymeleaf, jQuery, Tailwind
- 데이터베이스 : MariaDB, Workbench, MySQL, SQLyog, DBeaver

# 역할 분담
### 임재원
 + UI
   - 페이지 : 로그인, 회원가입, 계정찾기, 관리자페이지(회원 권한 설정, 신청된 경매 작품 확인, 경매 내역 관리, 작가 신청자 확인)
 + 기능
   - 사이트 배포 
   - 일반 회원 및 카카오, 네이버, 구글을 통한 소셜로그인 기능
   - 아이디/비밀번호 분실 시 이메일을 통해 아이디 복구 및 임시 비밀번호 발급 기능
   - 아이디 기억하기 기능
   - 회원 가입 전 약관 동의 고지 안내
   - 약관 동의 후 회원가입 기능
   - 회원 가입시 이메일 인증 및 카카오API를 통한 주소 입력 기능
   - 회원 가입시 환영 이메일 발송 기능
   - 관리자가 회원 권한 설정 페이지를 통해 사용자의 권한(role) 변경 시 DB, 실제 사용자 권한 변경 기능
   - 회원 목록 리스팅
   - 작가가 신청한 경매 작품 리스팅
   - 경매 내역 리스팅

### 박지완
 + UI
   - 페이지 : 
 + 기능
   - 

### 김민섭
 + UI
   - 페이지 : 
 + 기능
   - 

### 김채연
 + UI
   - 페이지 : 
 + 기능
   - 
