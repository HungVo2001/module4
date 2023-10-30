package com.example.banking_money.service.customer.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomerSaveRequest {
    private String fullName;
    private String phone;
    private String email;
    private String balance;
}
