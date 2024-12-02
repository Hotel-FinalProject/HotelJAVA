# HOTEL JAVA

> 호텔 예약 및 관리 시스템

## 💡 팀 소개

|[<img src="https://avatars.githubusercontent.com/u/81737413?v=4" width="140px" /> ](https://github.com/vsuminv)  | [<img src="https://avatars.githubusercontent.com/u/175531369?v=4" width="140px" /> ](https://github.com/ilPadrin0)|[<img src="https://avatars.githubusercontent.com/u/90806422?v=4" width="140px" /> ](https://github.com/ChoiTHs) |
|--|--|--|
|**이수민** |**이동진** |**최태형** |
| 시스템 관리자(Backend)<ul><li>호텔관리자 생성 / 로그인</li> <li>사용자 / 호텔관리자 조회 및 검색</li><li>권한별 계정상태 관리</li><li>리뷰 신고 관리</li></ul>결제<br/><ul><li>포트원 API를 활용한 결제 / 결제 취소</li></ul><br /> 호텔 찜 / 찜 해제<br/>배포 (CI/CD)  |ERD 설계 <br />데이터 모델링 <br /> 호텔, 객실 API 연동 <br /> 각 페이지 호텔, 객실 정보 표시 <ul><li>메인, 검색, 호텔, 객실 페이지</li></ul>지도, 마커 API 연동 <br /> 호텔 관리자 <ul><li>대시보드</li><li>객실 관리</li><li>예약 관리</li></ul>검색 및 필터링 기능<br /> 관리자 페이지 디자인  |ERD 설계 <br />JWT 토큰 활용한 회원가입 / 로그인 <br />소셜 로그인 <br />이메일 인증 <br /> 리뷰 <br />마이페이지 <ul><li>찜한 호텔 목록</li><li>예약 목록</li><li>리뷰 목록 </li></ul>  라우터 가드 <br /> 시스템 관리자 (Frontend)
<br />
<br />

## 프로젝트 소개
### 개발 일정 : 2024.10.22 ~ 2024.11.20 (약 1달) <br />
### 개발 인원 : 3명

### 팀 노션  : https://www.notion.so/Final-Team-Project-119ddc2b002680dc9e2ff6d85aeea917

## 깃허브 링크
https://github.com/Hotel-FinalProject/HotelJAVA/tree/dev
## ⭐ 배포 링크
http://43.200.45.122/

## 📚 기술스택
### <strong>프론트엔드</strong><br/>
<img src="https://img.shields.io/badge/Vue.js-4FC08D?style=for-the-badge&logo=css3&logoColor=white"/>![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
<br/>
### <strong>백엔드</strong><br/>
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F.svg?&style=for-the-badge&logo=SpringBoot&logoColor=white)![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
<br/>
### <strong>인프라</strong><br/>
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)![Amazon S3](https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white)![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)![Nginx](https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white)
<br />
### <strong>협업툴</strong><br/>
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)

![image](https://github.com/user-attachments/assets/8cee5be0-cc25-4981-90f7-85bc68854ee0)


### ERD
![image](https://github.com/user-attachments/assets/8149d8b5-573e-45cd-9a33-fb9474a0320d)


### 시스템 아키텍쳐
![image](https://github.com/user-attachments/assets/30a9716d-038b-4730-b1a7-d28bcfc362c8)

### 시연 영상
#### 사용자 <br >
https://github.com/user-attachments/assets/cc8d2d2f-24cf-4a7d-a1f1-8f8d1c76cc0a
#### 호텔 관리자
https://github.com/user-attachments/assets/5c763c65-4045-4b61-a651-45440ef11d85
#### 시스템 관리자
https://github.com/user-attachments/assets/1506593f-6907-4de7-a9fb-72ed0104864b
#### 사용자 - 로그인 X
https://github.com/user-attachments/assets/f555991e-7c58-4b05-b6c0-39d940e2db88

### 주요기능
#### 1. 사용자 <br >
#### - 랜덤 호텔 객실 조회
![스크린샷 2024-12-02 155318](https://github.com/user-attachments/assets/4175725b-2abf-4181-bfd7-bec0e27c0f71)
#### - 검색 및 필터링, 지도
        - 카카오 지도 API 기반으로 페이지 구현
        - 특정 조건 검색 시 검색 조건에 대한 지도 , 마커 표시
|                                                          검색 및 필터링                                                        |                                                      지도                                                     |
| :-----------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------: |
|         <img src="https://github.com/user-attachments/assets/a9e5b079-c451-4ab2-a6f9-0e09119b993a" alt="description" width="4000">                                             | ![스크린샷 2024-12-02 155523](https://github.com/user-attachments/assets/462791d6-7625-40cb-93f4-ec3a6086d415) |

#### - 객실 조회 / 찜 / 리뷰
|                                                         객실 상세 조회 / 찜 기능                                                       |                                                     리뷰 조회 및 신고                                                     |
| :-----------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------: |
|         ![스크린샷 2024-12-02 155959](https://github.com/user-attachments/assets/d3de0fda-c910-4a64-8a1b-dc9cac56725c)              | ![스크린샷 2024-12-02 160058](https://github.com/user-attachments/assets/e656c96d-ca5f-4e32-8908-93829ce0c501) |


#### - 객실 결제 / 결제 취소
        - 포트원 API 기반으로 신용카드 , 카카오페이, 토스페이 결제 구현
![스크린샷 2024-12-02 160550](https://github.com/user-attachments/assets/6383b804-e05a-414d-8214-09a3c00aefd2)
#### 2. 호텔 관리자 <br >
|                                                         객실 조회 및 수정                                                       |                                                     예약 조회 및 수정                                                    |
| :-----------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------: |
|         ![스크린샷 2024-12-02 164427](https://github.com/user-attachments/assets/30982089-1cad-4edd-95a3-f25385aa67a4)             | ![스크린샷 2024-12-02 164629](https://github.com/user-attachments/assets/1e8c918f-0894-439f-b027-8e0012707bfb) |



#### 3. 시스템 관리자 <br >
#### - 사용자 계정 관리
	- 2차 검증(비밀번호 확인)을 통해 보안성 강화
 	- 사용자 정보 조회 / 검색 및 계정 상태 관리
![스크린샷 2024-12-02 162010](https://github.com/user-attachments/assets/9d808c6d-de15-4b25-b3c6-4a293c1e8e0d)
|                                                         호텔 관리자 생성                                                      |                                                     리뷰 신고 관리                                                   |
| :-----------------------------------------------------------------------------------------------------------------------: | :-----------------------------------------------------------------------------------------------------------------------: |
|         ![스크린샷 2024-12-02 162239](https://github.com/user-attachments/assets/caa7dd96-8aa8-4b35-8d64-b9e37b6dfa97)             | ![스크린샷 2024-12-02 162128](https://github.com/user-attachments/assets/3a4d3ad9-91c4-4bbd-bc3a-1ab28bee5aa3)|


### API

- 회원 접근 권한 설정 api 구분

| 구분 | 설명                       |
| ----------------------------------- | -------------------------- |
| **/api** | 비회원 접근 권한 부여|
| **/api/auth** |비회원 접근 권한 미부여|

![image](https://github.com/user-attachments/assets/221df25c-44e7-4a4a-8d73-0ca9eea40020)


