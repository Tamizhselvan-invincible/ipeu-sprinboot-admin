package com.hetero.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class PushNotificationResponse {

    private int status;
    private String message;
}
