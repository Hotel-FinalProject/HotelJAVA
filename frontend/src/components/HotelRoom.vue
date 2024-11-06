<template>
  <div class="details-container">
    <img
      class="img-container"
      src="https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg"
    />
    <div class="hotel-name">객실</div>
    <div class="hotel-info2">
      <div class="hotel-tel">전화번호 : 02-1234-1234</div>
      <div class="hotel-site">홈페이지 : www.</div>
      <div class="hotel-addr">주소 : 서울시</div>
      <div class="avg-person">
        <img
          class="person-icon"
          src="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png"
        />
        <div class="avg-person-text">기준인원 2인</div>
      </div>
    </div>
    <div class="details-reservation">
      <h2>객실 선택</h2>
      <div class="details-middle">
        <div class="reservation-cal">캘린더
        <VDatePicker v-model.range="range" />
          <p>선택된 시작 날짜: {{ 
    range.start 
      ? `${range.start.getFullYear()}.${(range.start.getMonth() + 1).toString().padStart(2, '0')}.${range.start.getDate().toString().padStart(2, '0')} (${range.start.toLocaleDateString('ko-KR', { weekday: 'short' })})` 
      : '선택되지 않음' 
  }}</p>
         <p>선택된 시작 날짜: {{ 
    range.start 
      ? `${range.end.getFullYear()}.${(range.end.getMonth() + 1).toString().padEnd(2, '0')}.${range.end.getDate().toString().padEnd(2, '0')} (${range.end.toLocaleDateString('ko-KR', { weekday: 'short' })})` 
      : '선택되지 않음' 
  }}</p>
        </div>
        <div class="reservation-person">예약인원</div>
      </div>
      <div class="details-bottom">
        <div class="reservation-info">
          <h4 class="reservation-text">숙박</h4>

          <div class="check-info"> 체크인 15:00 ~ 체크아웃 11:00 </div>
          <h2 class="price"> 36,000원</h2>
          <div class="reservation-bottom">
            <div class="room-count">남은객실 3개 </div>
            <button class="reservation_btn" @click="handleReservation">
              예약하기
            </button>
          </div>
        </div>
      </div>
    </div>

    <h2> 후기 </h2>
    <div class="review-container">
      <div class="review-grid">
        <div class="review-top">
          <div class="review-rating"> ⭐⭐⭐⭐⭐ </div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="reviewer">작성자명</div>
        <div class="room-name">객실명</div>
        <div class="review-content">
          숙소도 전반적으로 깔끔했구요~ 고층이었는데 바다도 보이는 객실이라 좋았어요!
          동향이었는지 아침마다 햇빛이 엄청 들어왔지만 ㅋㅋㅋㅋ 이중 커튼과 에어컨으로 충분히 커버 가능합니다.
          무엇보다도 조식이 깔끔하면서도 맛있었어요 콩나물국과 죽이 제일 기억이 많이 남습니다 ㅎㅎㅎ
          오는정김밥이 숙소 바로 옆이라 예약하기 쉬웠다는 메리트도 있었습니다.ㅋㅋㅋ
          직원분들도 저희가 체크인을 늦게 하게 됐는데 바로 연락주셔서 주차 안내해주시고,
          내내 친절하셔서 감사했어요.(전기차는 주차타워를 아예 못 쓰는 것 같아요!)
        </div>
      </div>

      <div class="review-grid">
        <div class="review-top">
          <div class="review-rating"> ⭐⭐⭐⭐⭐ </div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="reviewer">작성자명</div>
        <div class="room-name">객실명</div>

        <div class="review-content">리뷰</div>
      </div>
      <div class="review-grid">
        <div class="review-top">
          <div class="review-rating"> ⭐⭐⭐⭐⭐ </div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="reviewer">작성자명</div>
        <div class="room-name">객실명</div>
        <div class="review-content">리뷰</div>
      </div>
    </div>
    <button class="review-all-btn">리뷰 더보기 </button>
    <div class="amenities-container">
      <h2> 기본 정보 및 시설/서비스 </h2>
      <div> 주차 가능 </div>
      <div> 조리 불가 </div>
      <div> 픽업 서비스 가능 </div>
      <hr />
      <div> 목욕시설 </div>
      <div> TV </div>
      <div> 냉장고 </div>
      <div> 테이블 </div>
      <div> 드라이기 </div>
      <hr />
    </div>

    <!-- Login Modal -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <span class="close" @click="showModal = false">&times;</span>
        <h2>로그인 필요</h2>
        <p>예약을 진행하시려면 로그인이 필요합니다.</p>
        <router-link to="/login">
          <button class="modal-btn" @click="onLoginSuccess" >로그인하러 가기</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script >

import { useAuthStore } from "@/store/register_login"; 


export default {
  name: "HotelRoom",
 
  data() {
    return {
      isLoggedIn: false, // 사용자의 로그인 상태
      showModal: false, // 모달의 표시 상태
      range: { start: null, end: null },

    };
  },
  
  created() {
    // 로그인 상태 확인
    const authStore = useAuthStore();
    authStore.checkLoginStatus(); 
    this.isLoggedIn = authStore.LoggedIn; 
  },
  methods: {
    handleReservation() {
      if (this.isLoggedIn) {
        // 예약 처리 로직
        this.$router.push("/payment"); // 결제 페이지로 이동
      } else {
        this.showModal = true; // 로그인 모달을 표시
        
      }
    },
  },
};

</script>


<style>
.details-container {
  width: 60%;
  margin: auto;
}
.img-container {
  width: 1100px;
}
.hotel-name {
  font-size: 20px;
  font-weight: bold;
}
.hotel-info2 {
  color: rgb(109, 109, 109);
  margin-top: 20px;
}
.review-container {
  display: flex;
}
.review-grid {
  width: 300px;
  height: 150px;
  border: 1px solid lightgray;
  border-radius: 5px;
  margin-top: 15px;
  margin-right: 20px;
  padding: 5px;
}
.review-top {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}
.review-date {
  color: rgb(109, 109, 109);
}
.reviewer {
  margin-top: 10px;
  margin-bottom: 10px;
  color: rgb(109, 109, 109);
}
.review-content {
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.details-middle {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  width: 100%;
}
.reservation-cal {
  width: 100%;
  height: 40px;
  border: 1px solid lightgray;
  border-radius: 5px;
}
.reservation-person {
  width: 100%;
  height: 40px;
  border: 1px solid lightgray;
  border-radius: 5px;
}
.details-bottom {
  margin-top: 30px;
  margin-bottom: 20px;
}
.room-container {
  width: 50%;
}
.room-name {
  font-weight: bold;
}
.avg-person {
  margin-top: 10px;
  display: flex;
}
.person-icon {
  width: 20px;
}
.avg-person-text {
  margin-left: 10px;
  color: rgb(109, 109, 109);
}
.reservation-info {
  padding: 10px;
  height: 270px;
  border: 1px solid lightgray;
  border-radius: 5px;
}
.reservation-text,
.check-info {
  color: rgb(109, 109, 109);
}
.reservation-text {
  width: 400px;
}
.room-count {
  color: orange;
  font-weight: bold;
  text-align: center;
}
.reservation-bottom {
  margin-top: 70px;
  display: flex;
  justify-content: space-between;
}
.reservation_btn {
  width: 150px;
  height: 40px;
  background-color: #00aef0;
  border-radius: 5px;
  color: white;
  font-weight: bold;
  text-align: center;
  border: none;
  font-size: 15px;
}
.review-all-btn {
  margin-top: 15px;
  width: 100%;
  height: 40px;
  color: white;
  background-color: #00aef0;
  border-radius: 5px;
  font-weight: bold;
  text-align: center;
  border: none;
}
.amenities-container {
  margin-top: 30px;
}
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
}
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}
.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}
.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
.modal-btn {
  margin-top: 10px;
  background-color: #00aef0;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
}
</style>
