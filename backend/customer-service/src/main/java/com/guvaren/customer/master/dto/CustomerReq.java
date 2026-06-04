package com.guvaren.customer.master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerReq {

    private String name;
    private String email;
    private String address;

}
