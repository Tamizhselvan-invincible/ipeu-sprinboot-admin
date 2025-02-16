package com.hetero.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class PushNotificationRequest {
    private String title;
    private String body;
    private String topic;
    private String token;
}