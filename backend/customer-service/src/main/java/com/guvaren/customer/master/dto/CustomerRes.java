package com.guvaren.customer.master.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRes {

    private String id;
    private String name;
    private String email;
    private String address;

}
