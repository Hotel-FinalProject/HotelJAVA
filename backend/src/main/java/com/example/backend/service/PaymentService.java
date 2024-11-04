package com.example.backend.service;

import com.example.backend.entity.Payment;
import com.example.backend.entity.Reservation;
import com.example.backend.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import org.springframework.http.HttpHeaders;
import java.util.Date;
import java.util.Map;

@Service
public class PaymentService {

    private final String IMP_KEY = ""; // 아임포트 가맹점 키
    private final String IMP_SECRET = ""; // 아임포트 가맹점 비밀 키

    public String getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/users/getToken";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("imp_key", IMP_KEY);
        requestBody.put("imp_secret", IMP_SECRET);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getJSONObject("response").getString("access_token");
        } else {
            throw new RuntimeException("Failed to get Iamport token");
        }
    }

    public Map<String, Object> verifyPayment(String impUid, String token) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getJSONObject("response").toMap();
        } else {
            throw new RuntimeException("Failed to verify payment");
        }
    }



}
