package com.example.study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class searchParam {
    private String account;
    private String email;
    private int page;

    //{ "account" : "", "email" : "", "page" = 0 }
}
