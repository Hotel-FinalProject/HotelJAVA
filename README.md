# HOTEL JAVA

> 호텔 예약 및 관리 시스템

## 💡 팀 소개

|[<img src="https://avatars.githubusercontent.com/u/81737413?v=4" width="140px" /> ](https://github.com/vsuminv)  | [<img src="https://avatars.githubusercontent.com/u/175531369?v=4" width="140px" /> ](https://github.com/ilPadrin0)|[<img src="https://avatars.githubusercontent.com/u/90806422?v=4" width="140px" /> ](https://github.com/ChoiTHs) |
|--|--|--|
|**이수민** |**이동진** |**최태형** |
| 시스템 관리자(Backend)<ul><li>호텔관리자 생성 / 로그인</li> <li>사용자 / 호텔관리자 조회 및 검색</li><li>권한별 계정상태 관리</li><li>리뷰 신고 관리</li></ul>결제<br/>포트원 API를 활용한 결제 / 결제 취소<br /> 호텔 찜 / 찜 해제<br/>배포 (CI/CD)  |ERD 설계 <br />데이터 모델링 <br /> 호텔, 객실 API 연동 <br /> 각 페이지 호텔, 객실 정보 표시 <ul><li>메인, 검색, 호텔, 객실 페이지</li></ul>지도, 마커 API 연동 <br /> 호텔 관리자 <ul><li>대시보드</li><li>객실 관리</li><li>예약 관리</li></ul>검색 및 필터링 기능<br /> 관리자 페이지 디자인  |ERD 설계 <br />JWT 토큰 활용한 회원가입 / 로그인 <br />소셜 로그인 <br />이메일 인증 <br /> 리뷰 <br />마이페이지 <ul><li>찜한 호텔 목록</li><li>예약 목록</li><li>리뷰 목록 </li></ul>  라우터 가드 <br /> 시스템 관리자 (Frontend)
<br />
<br />

## 프로젝트 소개

노션 링크

## ⭐ 배포 링크
http://43.200.45.122/

## 📚 기술스택
프론트엔드<br/>
<img src="https://img.shields.io/badge/Vue.js-4FC08D?style=for-the-badge&logo=css3&logoColor=white"/>![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
<br/>
백엔드<br/>
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F.svg?&style=for-the-badge&logo=SpringBoot&logoColor=white)![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
<br/>
인프라<br/>
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)![Amazon S3](https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white)![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)
<br />
협업툴 <br/>
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)

![image](https://github.com/user-attachments/assets/8cee5be0-cc25-4981-90f7-85bc68854ee0)


### ERD
![image](https://github.com/user-attachments/assets/8149d8b5-573e-45cd-9a33-fb9474a0320d)


### 시스템 아키텍쳐
![image](https://github.com/user-attachments/assets/30a9716d-038b-4730-b1a7-d28bcfc362c8)


| 구분 | 설명                       |
| ----------------------------------- | -------------------------- |
| **/config** | SpringSecurity principal 개체, jwt 관리|
| **/controller** | 웹 브라우저의 요청 전담 처리 |
| **/service**  | 데이터를 전달받아 가공 처리  |
| **/repository**  | DB에 접근하는 메서드 관리 |
| **/DTO**  | 계층 간 데이터 교환을 위해 사용되는 객체 관리 |
| **/model**  | Controller에서 생성한 데이터를 담아서 View로 전달할 때 사용하는 객체 관리 |

### API

- 회원 접근 권한 설정 api 구분

| 구분 | 설명                       |
| ----------------------------------- | -------------------------- |
| **/api** | 비회원 접근 권한 부여|
| **/api/auth** |비회원 접근 권한 미부여|

![image](https://github.com/user-attachments/assets/221df25c-44e7-4a4a-8d73-0ca9eea40020)




	
## 트러블슈팅
	
### 프론트엔드
	
### 백엔드
