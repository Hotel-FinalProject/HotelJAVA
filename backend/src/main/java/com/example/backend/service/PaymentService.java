package com.example.backend.service;

import com.example.backend.dto.ReservationDTO;
import com.example.backend.dto.RoomCountDTO;
import com.example.backend.dto.RoomDTO;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class PaymentService {

    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    private final  RoomCountRepository roomCountRepository;



    // 생성자를 통한 주입 방식
    @Autowired
    public PaymentService(
            @Value("${import.api-key}") String serviceKey,
            @Value("${import.secret-key}") String secretKey,
            PaymentRepository paymentRepository,
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            RoomRepository roomRepository,
            RoomCountRepository roomCountRepository) {

        this.iamportClient = new IamportClient(serviceKey, secretKey);
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.roomCountRepository = roomCountRepository;
    }

    @Transactional
    public void verifyPayment(Long userId, String imp_uid, ReservationDTO reservationDTO, RoomCountDTO roomCountDTO) throws IOException {


        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid);

        if (iamportResponse == null || iamportResponse.getResponse() == null) {
            throw new RuntimeException("결제 정보를 가져오는 데 실패했습니다. imp_uid: " + imp_uid);
        }

        BigDecimal amount = iamportResponse.getResponse().getAmount();
        String status = iamportResponse.getResponse().getStatus();
        String paymentMethod = iamportResponse.getResponse().getPgProvider();
        LocalDateTime date= LocalDateTime.now();
        if ("paid".equals(status)) {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));

            Room room = roomRepository.findById(reservationDTO.getRoomId())
                    .orElseThrow(() -> new RuntimeException("해당 방을 찾을 수 없습니다. roomId: " + reservationDTO.getRoomId()));

            if (paymentRepository.countByTransactionIdContainsIgnoreCase(imp_uid) == 0) {
                Payments payments = Payments.builder()
                        .transactionId(imp_uid)
                        .amount(amount)
                        .date(date)
                        .status("결제완료")
                        .method(paymentMethod)
                        .build();

                payments = paymentRepository.save(payments);

                updateRoomCountForReservation(reservationDTO.getCheckIn(), reservationDTO.getCheckOut(), room);



                Reservation reservation = Reservation.builder()
                        .user(user)
                        .payment(payments)
                        .status("예약 완료")
                        .checkIn(reservationDTO.getCheckIn())
                        .checkOut(reservationDTO.getCheckOut())
                        .guestNum(reservationDTO.getGuestNum())
                        .createDate(LocalDateTime.now())
                        .updateDate(LocalDateTime.now())
                        .request(reservationDTO.getRequest())
                        .rooms(room)
                        .build();



                reservationRepository.save(reservation);

            } else {
                throw new RuntimeException("이미 결제 되었습니다.");
            }
        } else {
            throw new RuntimeException("결제 오류: 결제 상태가 " + status + "입니다.");
        }
    }

    // 예약 결제 시 남은 객실 감소 시키는 메소드
    private void updateRoomCountForReservation(LocalDate checkIn, LocalDate checkOut, Room room) {

        // 체크인 날짜부터 체크아웃 전날까지 처리
        LocalDate checkInDate = checkIn;
        LocalDate checkOutDate = checkOut.minusDays(1);

        while (!checkInDate.isAfter(checkOutDate)) {

            final LocalDate currentCheckInDate = checkInDate;

            // 해당 날짜에 카운트 찾아서 -1 없으면 새로 생생해서 -1 해줌
            RoomCount roomCount = roomCountRepository.findRoomCountByRoomRoomIdAndDate(room.getRoomId(), checkInDate)
                    .orElseGet(() -> createNewRoomCount(room, currentCheckInDate));

            roomCount.setRoomCount(roomCount.getRoomCount() - 1);
            roomCountRepository.save(roomCount);

            checkInDate = checkInDate.plusDays(1);
        }
    }

    // 결제 후 해당 날짜가 없는 경우 새로 생성 후 roomcount 감소시킴
    private RoomCount createNewRoomCount(Room room, LocalDate date) {
        return RoomCount.builder()
                .room(room)
                .date(date)
                .roomCount(10)
                .build();
    }

    public void cancel(Long userId, String imp_uid, Long roomId) throws IOException {
        IamportResponse<Payment>  iamportResponse = iamportClient.paymentByImpUid(imp_uid);

        if (iamportResponse == null || iamportResponse.getResponse() == null) {
            throw new RuntimeException("결제 정보를 가져오는 데 실패했습니다. imp_uid: " + imp_uid);
        }

        // 결제 상태와 금액 확인
        String status = iamportResponse.getResponse().getStatus();
        if (!"paid".equals(status)) {
            throw new RuntimeException("결제 상태가 유효하지 않습니다. 현재 상태: " + status);
        }

        // 결제 취소 요청
        CancelData cancelData = new CancelData(imp_uid, true); // 타입 변환
        IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

        if (cancelResponse == null || cancelResponse.getResponse() == null) {
            throw new RuntimeException("결제 취소 요청 실패. imp_uid: " + imp_uid);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. userId: " + userId));


        Payments payment = paymentRepository.findByTransactionId(imp_uid)
                .orElseThrow(() -> new RuntimeException("해당 결제 정보를 찾을 수 없습니다. imp_uid: " + imp_uid));
        payment.setStatus("결제 취소");
        payment.setDate(LocalDateTime.now());
        paymentRepository.save(payment);

        Reservation reservation = (Reservation) reservationRepository.findByRoomsRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("해당 방의 예약 정보를 찾을 수 없습니다. roomId: " + roomId));


        reservation.setStatus("예약취소");
        reservation.setUpdateDate(LocalDateTime.now());

        reservationRepository.save(reservation);

        updateRoomCountIncrement(reservation.getCheckIn(), reservation.getCheckOut(), reservation.getRooms());

    }

    // 객실 카운트 복원
    private void updateRoomCountIncrement(LocalDate checkIn, LocalDate checkOut, Room room) {

        LocalDate checkInDate = checkIn;
        LocalDate checkOutDate = checkOut.minusDays(1);

        while (!checkInDate.isAfter(checkOutDate)) {

            final LocalDate currentCheckInDate = checkInDate;

            RoomCount roomCount = roomCountRepository.findRoomCountByRoomRoomIdAndDate(room.getRoomId(), checkInDate)
                    .orElseThrow(() -> new RuntimeException("해당 날짜에 대한 객실 수 정보를 찾을 수 없습니다."));

            roomCount.setRoomCount(roomCount.getRoomCount() +1);
            roomCountRepository.save(roomCount);

            checkInDate = checkInDate.plusDays(1);
        }
    }
}