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
        <h2 class = "hotel-name"> 호텔명 </h2>
        <h2 class = "hotel-roomname"> 객실명 </h2>
        <div class="reservation-conatiner-info">
            <div class="check-in-conatiner">
                <div class="check">체크인</div>
                <h2>2024.10.26</h2>
                <div class="check-time">14:00</div>
            </div>
            <div class="check-out-conatiner">
                <div class="check">체크아웃</div>
                <h2>2024.10.26</h2>
                <div class="check-time">14:00</div>
            </div>
        </div>
        <div class="avg-person"> 
            <img class="person-icon" src ="https://yaimg.yanolja.com/stay/static/images/v3/icon_my.png">
            <div class="avg-person-text">기준인원 2인</div>
        </div>
        <h2 class="price">47,000원</h2>
        <div class ="reservation-info">
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
                <div class="tel-info"> 입력하신 번호는 숙소로 전달됩니다. </div>
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
            <div class ="payment">47,000원</div>
        </div>
        <div class="payment-info">
            <div class ="payment-text">마일리지</div>
            <div class ="payment">10마일리지</div>
        </div>
        <hr class="dot-line"/>
        <div class="payment-total-conatiner">
            <div class="payment-info-text">총 결제 금액</div>
            <div class ="payment-total">47,000원</div>
        </div>
    </div>
    <div class="payment-method-conatiner">
        <h3>결제 수단</h3>
        <div> <button @click="submitReservation" class="submit-button">결제</button></div>
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
        <button class="payment-btn"> 47,000원 결제하기</button>
        <div class="info"> (주) OO플랫폼은 통신판매 중개자로서 통신판매의 당사자가 아니며 상품 예약, 이용 및 환불과는 관련한 의무와 책임은 각 판매자에게 있습니다. </div>
    </div>
    
</div>

</template>


<script>

export default {
  data() {
    return {
      userName: '',
      userPhone: '',
      totalPrice: 100, // 예시 결제 금액
    };
  },
  methods: {
    submitReservation() {
      const IMP = window.IMP; // 아임포트 객체
      IMP.init('imp45605876'); // 가맹점 식별코드 (아임포트에서 발급받은 가맹점 코드)

      IMP.request_pay(
        {
          pg: 'html5_inicis', // PG사 선택 (예: 'kakao', 'html5_inicis' 등)
          pay_method: 'card', // 결제 수단
          merchant_uid: `mid_${new Date().getTime()}`, // 고유 주문번호
          name: '숙소 예약 결제',
          amount: this.totalPrice,
          buyer_name: this.userName,
          buyer_tel: this.userPhone,
        },
        function (rsp) {
          if (rsp.success) {
            // 결제 성공 시 처리 로직
            alert('결제가 완료되었습니다.');
            // 결제 정보를 서버로 전송하여 추가 처리
          } else {
            // 결제 실패 시 처리 로직
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
.user-info,.tel-info{
    color : rgb(109, 109, 109)
}
.info{
    margin-top: 20px;
     color : rgb(109, 109, 109)
}
.user-conatiner{
    margin-top:10px;
    margin-bottom:20px;
}
.user-input{
    margin-top:10px;
    width: 100%;
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