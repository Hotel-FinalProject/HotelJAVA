<template>

  <div class = "details-container">
    <img class = "img-container" src = "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg">
    <div class="hotel-name">호텔명</div>
    <div class="hotel-info">
          <span class="racting">⭐4.5</span><span>(리뷰갯수)</span>
    </div>
    <div class="hotel-info2">
        <div>전화번호 : 02-1234-1234</div>
        <div>홈페이지 : www.</div>
        <div>주소 :서울시</div>
    </div>
    <!-- TODO: 로그인 JWT토큰 세션에 저장하게끔 | state에 저장해서 사용
    리뷰뿌리기는 review-grid가 v-for 사용해서 가저온 리뷰 최대 3개까지 .
    그 이상 리뷰 조회 기능 필요 시 페이지 별도 생성 필요.
    -->
    <div class = "review-conatiner">
      <div class="review-grid">
        <div class = "review-top">
          <div class="review-racting"> ⭐⭐⭐⭐⭐ </div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="review-content">
          <!-- 리뷰  -->
          </div>
      </div>
      <div class="review-grid">
        <div class = "review-top">
          <div class="review-racting"> ⭐⭐⭐⭐⭐ </div>
          <div class="review-date">2024.10.25</div>
        </div>
        <div class="review-content">
          <!-- 숙소도 전반적으로 깔끔했구요~ 고층이었는데 바다도 보이는 객실이라 좋았어요! 
          동향이었는지 아침마다 햇빛이 엄청 들어왔지만 ㅋㅋㅋㅋ 이중 커튼과 에어컨으로 충분히 커버 가능합니다. 
          무엇보다도 조식이 깔끔하면서도 맛있었어요 콩나물국과 죽이 제일 기억이 많이 남습니다 ㅎㅎㅎ 
          오는정김밥이 숙소 바로 옆이라 예약하기 쉬웠다는 메리트도 있었습니다.ㅋㅋㅋ 
          직원분들도 저희가 체크인을 늦게 하게 됐는데 바로 연락주셔서 주차 안내해주시고, 
          내내 친절하셔서 감사했어요.(전기차는 주차타워를 아예 못 쓰는 것 같아요!) -->
          </div>
      </div>

    </div>

    <div class = "details-reservation">
  
      <div class = "details-middle">
        <div class = "reservation-cal">
          캘린더
        </div>
        <div class = "reservation-person">
        예약인원
        </div>
      </div>
      <hr class = "line"/>
      <div class="details-bottom">
        <div class = "room-container">
          <img class = "room-img-container" src = "https://www.agoda.com/wp-content/uploads/2019/05/Best-hotels-in-Seoul-South-Korea-accommodations-The-Shilla-Seoul.jpg">
          <h2 class="room-name" > 한라 온돌 </h2>
          
          <div class="avg-person"> 
            <img class="person-icon" src ="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png">
            <div class="avg-person-text">기준인원 2인</div>
          </div>
        </div>
        <div class = "reservation-info">
            <h4 class = "reservation-text">숙박</h4>
          
          <div class = "check-info"> 체크인 15:00 ~ 체크아웃 11:00 </div>
          <h2 class = "price"> 36,000원</h2>
          <div class= "reservation-bottom">
            <div class = "room-count">남은객실 3개 </div>
            <router-link to ="/rooms">
              <button class = "reservation_btn"> 예약 및 상세보기 </button>
            </router-link>
          </div>
        </div>
      </div>
      <hr class = "line"/>
      

    </div>    
  </div> 

</template>

<script scoped>
export default {
  name: 'HotelDetails',
};
</script>

<style scoped>
.details-container{
  width: 65%;
  margin: auto;
}
.img-container{
   width: 100%;
}

.hotel-name{
    font-size : 20px;
    font-weight : bold;
}
.hotel-info2{
  margin-top: 20px;
}
.details-right-middle{
  margin-top:20px;
  margin-bottom: 20px;
  display: flex;
}
.review-conatiner{

 display: flex;

}
.review-grid{
  width : 315px;
  height : 150px;
  border : 1px solid lightgray;
  border-radius: 25px;
  margin-top : 15px;
  margin-right : 10px;
  padding : 5px;
}
.review-top{
  margin-top : 10px;
  
  display: flex;
  justify-content: space-between;
}
.review-date{
  color : rgb(109, 109, 109)
}
.review-content{

  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.details-middle{
  display:flex;
  justify-content:space-between;
  margin-top : 20px;
  width : 100%;
}
.reservation-cal{
  width : 100%;
  height : 40px;
  border : 1px solid lightgray;
  border-radius: 5px;
}
.reservation-person{

 width : 100%;
  height : 40px;
  border : 1px solid lightgray;
  border-radius: 5px;
}
.room-img-container {
  width: 100%;
  aspect-ratio: 16/9; /* 16:9 비율 유지 */
  object-fit: cover; /* 이미지가 비율에 맞춰 잘리는 부분이 있을 수 있지만 화면에 꽉 차도록 설정 */
  border-radius: 10px;
}

.details-bottom {
  display: flex;
  margin-top: 30px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.room-container {
  flex: 1 1 45%;
  margin-right: 10px;
  min-width: 300px; 
}

.avg-person{
  display:flex;
}
.person-icon{
  width: 20px;
}

.avg-person-text{
  margin-left: 10px;
  color : rgb(109, 109, 109)
}

.reservation-info {
  flex: 1 1 45%;
  padding: 10px;
  height: 270px;
  border: 1px solid lightgray;
  border-radius: 5px;
  min-width: 300px; /* 최소 크기 설정 */
}

.reservation-text,.check-info {
  color : rgb(109, 109, 109)
}

.reservation-text{
  width : 400px;
}

.room-count{
  color : orange;
  font-weight : bold;
  text-align: center;
}

.reservation-bottom{
  margin-top : 70px;
  display: flex;
  justify-content: space-between;
}

.reservation_btn{
  width: 150px;
  height: 40px;
  background-color: #00aef0;
  border-radius: 5px;
  color: white;
  font-weight : bold;
  text-align : center;
  border: none;
  font-size :15px;
}
.line{
  background:lightgray;
  height:1px;
  border:0;
}

</style>
