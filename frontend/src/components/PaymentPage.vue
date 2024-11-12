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
                <h2>{{ formatDate(dataObj.userCheckIn) }}</h2>
                <div class="check-time">{{ dataObj.checkIn }} </div>
            </div>
            <div class="check-out-conatiner">
                <div class="check">체크아웃</div>
                <h2>{{ formatDate(dataObj.userCheckOut) }}</h2>
                <div class="check-time">{{ dataObj.checkOut }}</div>
            </div>
        </div>
        <div class="avg-person"> 
            <img class="person-icon" src ="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png">
            <div class="avg-person-text">예약인원 {{ dataObj.guestNum }}인</div>
        </div>
        <h2 class="price"> {{ Number(dataObj.roomPrice). toLocaleString() }}원</h2>
        <div class ="reservation-info">
            <div class="reservation-info-conatiner">
                <h3> 예약자 정보 <span class="asterisk">*</span></h3>
                <div class="info-container">
                    <div class="user-conatiner">
                        <span class="user-info"> 예약자 이름</span><span class="asterisk">*</span><br>
                        <div>
                            <input class="user-input" type="text" v-model="userName">
                        </div>
                    </div>
                    <div class="user-conatiner">
                        <span class="user-info"> 휴대폰 번호</span><span class="asterisk">*</span><br>
                        <div>
                            <input class="user-input" type="tel" v-model="userPhone">
                        </div>
                    </div>
                    <div class="user-conatiner">
                        <span class="user-info"> 요청사항</span><br>
                        
                        <div>
                            <input class="request"  type="text" v-model="request">
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

    <div v-if="isPaymentModalVisible" class="payment-modal-overlay">
        <!-- 결제 완료 모달 -->
        <div class="payment-modal">
          <h2>결제 완료되었습니다!</h2> <hr>
          <h3>결제자 정보</h3>
          <div class = "re-user-info-container">
            <div class="user-info-container">
                <div class="user-name-container">
                    <div class="label">예약자</div>
                    <div > {{userName}} </div>
                </div>
                <div class="user-name-container">
                    <div class="label">전화번호</div>
                    <div > {{phone}} </div>
                </div>
                <div class="user-name-container">
                    <div class="label">호텔명</div>
                    <div >{{ dataObj.hotelName }} </div>
                </div>
                <div class="user-name-container">
                    <div class="label">객실명</div>
                    <div >{{ dataObj.roomName }} </div>
                </div>
                <div class="user-name-container">
                    <div class="label">체크인</div>
                    <div >{{ formatDate(dataObj.userCheckIn) }} </div>
                </div>
                <div class="user-name-container">
                    <div class="label">체크아웃</div>
                    <div >{{ formatDate(dataObj.userCheckOut) }} </div>
                </div>
                <div class="user-name-container">
                    <div class="label">예약인원</div>
                    <div >{{ dataObj.guestNum }}명 </div>
                </div>
                <div class="user-name-container">
                    <div class="label">요청사항</div>
                    <div >{{ request }} </div>
                </div>
                <div class="user-name-container">
                    <div class="label">결제 금액</div>
                    <div >{{ Number(dataObj.roomPrice). toLocaleString() }}</div>
                </div>
            </div>
            <div class="move-container">
                <div class="btn-container">
                    <router-link to ="/">
                        <button class="move-btn">HOME</button>
                    </router-link>
                </div>
                <div class="btn-container">
                    <router-link to ="/my_page">
                        <button class="move-btn">MYPAGE</button>
                    </router-link>
                </div>
            </div>
    
          </div>
        
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
      request: '',
      range: { start: null, end: null },
      isPaymentModalVisible: false,
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

            // 프론트 백엔드 로컬 타입 안 맞아서 하루씩 추가해줌
            const addOneDay = (date) => {
                const newDate = new Date(date);
                newDate.setDate(newDate.getDate()); 
                return newDate.toISOString(); 
            };

            const userCheckIn = this.dataObj.userCheckIn
                ? addOneDay(this.dataObj.userCheckIn) // userCheckIn에 하루 더하기
                : addOneDay(new Date()); // 현재 날짜에 하루 더하기

            const userCheckOut = this.dataObj.userCheckOut
                ? addOneDay(this.dataObj.userCheckOut) // userCheckOut에 하루 더하기
                : addOneDay(new Date()); 

                 
            try {
                    const reservationResponse = await axios.post(`http://localhost:8081/api/auth/reservation/${imp_uid}`, { 
                        userId: userId,
                        checkIn: userCheckIn,
                        checkOut: userCheckOut,
                        totalPrice: this.dataObj.roomPrice,
                        roomId: this.dataObj.roomId,
                        request: this.request,
                        guestNum: 2,
                        imp_uid: imp_uid,
                    },
              {
                headers:{
                    Authorization: `Bearer ${token}`
                }
              }
              );
              
              
               this.showPaymentModal(); 
              console.log(reservationResponse);

 
            } catch (error) {
              console.error('서버 요청 실패:', error);
              alert('결제 정보 저장에 실패하였습니다.');
            }
          } else {
            alert(`결제에 실패하였습니다: ${rsp.error_msg}`);
          }
        },
      );
    },
    showPaymentModal() {
        this.isPaymentModalVisible = true; 
        this.$nextTick(() => {
        document.querySelector('.payment-modal-overlay').classList.add('active');
        });
    },

    formatDate(date) {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit', weekday: 'short' };
        const formattedDate = new Date(date).toLocaleDateString('ko-KR', options);
        return formattedDate.replace(/\.$/, '').replace(' ', '');


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
.payment-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999; 
  visibility: hidden; 
  opacity: 0;
  transition: visibility 0s, opacity 0.3s linear;
}

.payment-modal-overlay.active {
  visibility: visible;
  opacity: 1;
}

.payment-modal {
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  width: 300px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.user-info-container{
    border: 1px solid lightgray;
    border-radius: 5px;
    padding: 10px;
}
.user-name-container{
    display: flex;
    margin-bottom:15px;
}
.label{
    width: 100px;
    font-weight: bold;
    margin-right: 10px;
}
.move-container{
    display: flex;
    justify-content: center;
    margin-top: 10px;
}
.btn-container{
    margin-left: 20px;
    margin-right: 20px;
}
.move-btn{
    width: 100px;
    height: 30px;
    background-color: #00aef0;
    border: none;
}
</style>