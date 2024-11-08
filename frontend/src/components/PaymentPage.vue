<template>
<div class="details-container" >
    <h3>숙소</h3>
    <div class="cancle-info-container">
        <div class="cancle-info">
            <h4 class = "cancle-info-title">예약 완료 후 무료 취소 안내</h4>
            <div class = "cancle-info-detail" > · 예약일시 기준 체크인 시각 이전일 경우 무료취소가 가능합니다.</div>
            <div class = "cancle-info-detail"> · 숙소 정책에 따라 일부 상품은 무료취소가 불가능합니다.</div>
            <hr>
            <div class="cancle-info-details"> 10분 이내 무료 취소 </div>
            <div class = "cancle-info-detail"> (단, 숙소 정책에 따라 취소수수료 부가 예외 규정이 적용되지 않을 수 있습니다.)</div>
        </div>
    </div>

    <div class = "reservation-conatiner">
        <h2 class = "hotel-name"> {{ dataObj.hotelName }}</h2>
        <h2 class = "hotel-roomname"> {{ dataObj.roomName }} </h2>
        <div class="reservation-conatiner-info">
            <div class="check-in-conatiner">
                <div class="check">체크인</div>
                <h2>2024.10.26</h2>
                <div class="check-time">{{ dataObj.checkIn }}</div>
            </div>
            <div class="check-out-conatiner">
                <div class="check">체크아웃</div>
                <h2>2024.10.26</h2>
                <div class="check-time">{{ dataObj.checkOut }}</div>
            </div>
        </div>
        <div class="avg-person"> 
            <img class="person-icon" src ="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png">
            <div class="avg-person-text">기준인원 2인</div>
        </div>
        <h2 class="price"> {{ dataObj.roomPrice}}원</h2>
        <div class ="reservation-info">
            <div class="reservation-info-conatiner">
                <h3> 예약자 정보 <span class="asterisk">*</span></h3>
                <div class="info-container">
                    <div class="user-conatiner">
                        <span class="user-info"> 성명</span><span class="asterisk">*</span><br>
                        <div>
                            <input class="user-input" type="text">
                        </div>
                    </div>
                    <div class="user-conatiner">
                        <span class="user-info"> 휴대폰 번호</span><span class="asterisk">*</span><br>
                        <div>
                            <input class="user-input"  type="tel">
                        </div>
                    </div>
                    <div class="user-conatiner">
                        <span class="user-info"> 요청사항</span><br>
                        
                        <div>
                            <input class="request"  type="text">
                        </div>
                    </div>
                </div>         
            </div>
        </div>
    </div>
    <hr>
    <div class="payment-conatiner">
        <div class ="payment-info-title">
            <div class="payment-info-text"> 결제 금액</div>  
            <div class="payment-info-detail">(세금 및 봉사료 포함)</div>
        </div>
        <div class="payment-info">
            <div class ="payment-text">상품금액</div>
            <div class ="payment">{{ dataObj.roomPrice ? dataObj.roomPrice.toLocaleString() : '가격 정보 없음' }}원</div>
        </div>
        <div class="payment-info">
            <div class ="payment-text">마일리지</div>
            <div class ="payment">10마일리지</div>
        </div>
        <hr class="dot-line"/>
        <div class="payment-total-conatiner">
            <div class="payment-info-text">총 결제 금액</div>
            <div class ="payment-total">{{ dataObj.roomPrice ? dataObj.roomPrice.toLocaleString() : '가격 정보 없음' }}원</div>
        </div>
    </div>
    <div class="payment-method-conatiner">
        <h3>결제 수단</h3>
        <div> <button @click="submitReservation('html5_inicis')" class="submit-button">카드 결제</button></div>
        <div> <button @click="submitReservation('kakaopay')" class="submit-button">카카오페이</button></div>
        <div> <button @click="submitReservation('tosspay')" class="submit-button">토스페이</button></div>
        
   
    </div>
    <div class="payment-agree-conatiner">
        <div class ="payment-agree-top">
            <div class="payment-agree">
                <div class="payment-agree-text"> 현장결제</div>
                <div class="payment-agree-text-detail"> 추가인원 비용등의 현장결제 발생 상품을 확인하세요.</div>
            </div>
            <div class="payment-agree">
                <div class="payment-agree-text"> 취소불가 및 수수료</div>
                <div class="payment-agree-text-detail"> 취소 및 환불규정에 따라 취소불가, 수수료가 발생할 수 있습니다.</div>
            </div>
            <div class="payment-agree">
                <div class="payment-agree-text"> 미성년자 및 법정대리인 필수</div>
                <div class="payment-agree-text-detail">미성년자는 법정대리인 동행 없이 투숙이 불가능합니다.</div>
            </div>
        </div>
    </div>
    <div class = "check-conatiner">
        <input type="checkbox"><span>전체동의</span>

        <div class="check-detail-conatiner">
            <div>
                <input type="checkbox"><span class="check-text">[필수] 만 14세 이상 이용 동의</span>
            </div>
            <div>
                <input type="checkbox"><span class="check-text">[필수] 이용 규칙</span>
            </div>
            <div>
                <input type="checkbox"><span class="check-text">[필수] 취소 및 환불 규칙</span>
            </div>
            <div>
                <input type="checkbox"><span class="check-text">[선택] 이벤트, 혜택 정보 수신 동의</span>
            </div>
            <div>
                <input type="checkbox"><span class="check-text">[선택] 이벤트, 혜택 정보 전송을 위한 개인정보 수집 및 이용 동의</span>
            </div>
        </div>
        <div class="info"> (주) OO플랫폼은 통신판매 중개자로서 통신판매의 당사자가 아니며 상품 예약, 이용 및 환불과는 관련한 의무와 책임은 각 판매자에게 있습니다. </div>
    </div>
    
</div>

</template>


<script>
import axios from 'axios';
import { useAuthStore } from "@/store/register_login";
export default {
  name: 'paymentPage',

  data() {
    return {
      userName: '',
      userPhone: '',
      dataObj: history.state || {},
      isLoggedIn: false,
    };
  },
  async created() {
    const authStore = useAuthStore();

    try {
      // 로그인 상태 확인 및 로그인 처리
      await authStore.checkLoginStatus();  // 로그인 상태 체크
      if (authStore.LoggedIn) {
        // 로그인 상태가 True일 경우 사용자 정보 설정
        this.isLoggedIn = authStore.LoggedIn;
        this.userName = authStore.currentUser;
        this.userPhone = authStore.phone;
        this.userid = authStore.userId;
        this.userName = authStore.userName;
        this.phone = authStore.phone;
        this.token = authStore.accessToken;
      } else {
        console.log("로그인되지 않았습니다.");
      }
    } catch (error) {
      console.error('로그인 상태 확인 실패:', error);
      alert('로그인 상태 확인 중 오류가 발생했습니다.');
    }
  },

  methods: {
    async submitReservation(pg_method) {
      const token = this.token;
      const userId = this.userid;

      const IMP = window.IMP; // 아임포트 객체
      IMP.init('imp45605876'); // 가맹점 식별코드

      IMP.request_pay(
        {
          pg: pg_method,
          pay_method: 'card',
          merchant_uid: `mid_${new Date().getTime()}`,
          name: '숙소 예약 결제',
          amount: this.dataObj.roomPrice,
          buyer_name: this.userName,
          buyer_tel: this.userPhone,
        },
        async (rsp) => {
          if (rsp.success) {
            const imp_uid = rsp.imp_uid;
            try {
              
              const reservationResponse = await axios.post(`http://localhost:8081/api/auth/reservation/${imp_uid}`, { 
                userId: userId,
                checkIn: '',
                checkOut: '',
                totalPrice: this.dataObj.roomPrice,
                roomId: this.dataObj.roomId,
                checkInDate: this.dataObj.checkInDate,
                checkOutDate: this.dataObj.checkOutDate,
                guestNum: 2,
                imp_uid: imp_uid,
              },
              {
                headers:{
                    Authorization: `Bearer ${token}`
                }
              }
              );

              alert('결제가 완료되었습니다.');
              console.log(reservationResponse.data);
            } catch (error) {
              console.error('서버 요청 실패:', error);
              alert('결제 정보 저장에 실패하였습니다.');
            }
          } else {
            alert(`결제에 실패하였습니다: ${rsp.error_msg}`);
          }
        }
      );
    },
  },
};


     

</script>
<style>
.details-container{
  width: 60%;
  margin: auto; 
}
.cancle-info-container{
    margin-top: 10px;
    background-color: rgb(245,245,244);
    padding: 15px;
}
.cancle-info-title{
    margin-bottom:10px;
}
.cancle-info-detail{
    color: gray;
}
hr{
    margin-top: 20px;
    margin-bottom: 20px;
    
}

.cancle-info-details{
    color: rgb(170,170,165);
    font-weight:bold;
    margin-bottom: 10px;
}

.reservation-conatiner-info{
    display:flex;
}
.check-in-conatiner, .check-out-conatiner{
    width: 50%;
}
.check{
    color:gray;
}
.check-time{
    font-size : X-Large;
}
.avg-person{
  margin-top : 10px;
  display:flex;
}
.person-icon{
  width: 20px;
}

.avg-person-text{
  margin-left: 10px;
  color : rgb(109, 109, 109)
}
.price{
    text-align:right;
}

.asterisk{
    color:red;
}
.reservation-info{
    border : 1px solid lightgray;
    width: 100%;
}
.reservation-info-conatiner{
    margin-left:20px;
}
.info-container{
    width:100%;

}
.user-info,.tel-info{
    color : rgb(109, 109, 109)
}
.info{
    margin-top: 20px;
     color : rgb(109, 109, 109)
}
.user-conatiner{
    width: 100%;
    margin-bottom:20px;
}
.user-input{
    margin-top:10px;
    width: 97%;
    
}
.request{
    width: 97%;
    height:100px;
    margin-top:10px;
}
.payment-info-title{
    display:flex;
}
.payment-info-text,.payment-total{
    font-size : X-Large;
    font-weight: bold;
}
.payment-total{
    color:#00aef0;
}
.payment-info-detail{
     color : rgb(109, 109, 109);
     margin-left:5px;
     padding-top:5px;
}
.payment-info{
    display:flex;
    justify-content:space-between;
    margin-top: 20px;
}
.payment-text, .payment{
    color : rgb(109, 109, 109);
}
.payment-total-conatiner{
    display:flex;
    justify-content:space-between;
}
.payment-agree-top{
    background-color: rgb(253,232,190);
    margin-top:20px;
    padding: 15px;
}
.payment-agree{
    margin-bottom:15px;
}
.payment-agree-text{
    font-size : medium;
    font-weight:bold;
    color:red;
}
.payment-agree-text-detail{
    color:rgb(124,123,119);
}
.check-conatiner{
    margin-top:20px;
}
</style>