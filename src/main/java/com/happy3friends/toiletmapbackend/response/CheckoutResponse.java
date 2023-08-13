package com.happy3friends.toiletmapbackend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckoutResponse {
    private List<String> listUserCheckout;
    private int numberOfUserPoop;
    private int numberOfUserBath;
}
