package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.entity.Reservation;
import com.example.backend.entity.User;
import com.example.backend.entity.Payments;
import com.example.backend.repository.PaymentRepository;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    // 생성자를 통한 주입 방식
    @Autowired
    public PaymentService(
            @Value("${import.api-key}") String serviceKey,
            @Value("${import.secret-key}") String secretKey,
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository,
            UserRepository userRepository) {

        this.iamportClient = new IamportClient(serviceKey, secretKey);
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void verifyPayment(Long userId, String imp_uid, ReservationDTO reservationDTO) throws IOException {


        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid);

        if (iamportResponse == null || iamportResponse.getResponse() == null) {
            throw new RuntimeException("결제 정보를 가져오는 데 실패했습니다. imp_uid: " + imp_uid);
        }

        BigDecimal amount = iamportResponse.getResponse().getAmount();
        String status = iamportResponse.getResponse().getStatus();
        String paymentMethod = iamportResponse.getResponse().getPgProvider();
        Date date = new Date();
        if ("paid".equals(status)) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));
            if (paymentRepository.countByTransactionIdContainsIgnoreCase(imp_uid) == 0) {
                Payments payments = Payments.builder()
                        .transactionId(imp_uid)
                        .amount(amount)
                        .date(date)
                        .status("결제완료")
                        .method(paymentMethod)
                        .build();

                payments = paymentRepository.save(payments);


                Reservation reservation = Reservation.builder()
                        .user(user)
                        .payment(payments)
                        .status("예약 완료")
                        .checkIn(reservationDTO.getCheckIn())
                        .checkOut(reservationDTO.getCheckOut())
                        .guestNum(reservationDTO.getGuestNum())
                        .build();

                reservationRepository.save(reservation);

            } else {
                throw new RuntimeException("이미 결제 되었습니다.");
            }
        } else {
            throw new RuntimeException("결제 오류: 결제 상태가 " + status + "입니다.");
        }
    }

}
