package com.example.backend.util;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserDeactivationScheduler {

    private final UserRepository userRepository;

    public UserDeactivationScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 3 * * ?") // 매일 새벽 3시에 실행
    public void deactivateInactiveUsers() {
        LocalDateTime thresholdDate = LocalDateTime.now().minusMonths(6); // 6개월 동안 비활성 상태인 계정들 찾기
        List<User> inactiveUsers = userRepository.findByLastLoginTimeBeforeAndIsActiveTrue(thresholdDate);

        for (User user : inactiveUsers) {
            user.setIsActive(false); // 비활성화
            userRepository.save(user);
        }

        System.out.println(inactiveUsers.size() + "명의 계정이 비활성화되었습니다.");
    }
}
