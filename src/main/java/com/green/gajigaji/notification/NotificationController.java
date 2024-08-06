package com.green.gajigaji.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor

public class NotificationController {
//    private final NotificationService notificationservice;
//    public static Map<Long, SseEmitter> sseEmiters = new ConcurrentHashMap<>();
//
//    @GetMapping("api/notification/subscribe")
//    public SseEmitter subscribe(@AuthenticationPrincipal UserDetails userDetails) {
//        Long userId = userDetails.getUsername().
//    }

}
