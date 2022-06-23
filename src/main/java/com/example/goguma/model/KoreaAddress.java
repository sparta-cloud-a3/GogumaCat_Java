package com.example.goguma.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class KoreaAddress {

    @Id
    @GeneratedValue
    @Column(name = "korea_address_id")
    private Long id;

    @Column(nullable = false)
    private String si;

    private String gu;

    @Column(nullable = false)
    private String dong;

    public KoreaAddress(String si, String gu, String dong) {
        this.si = si;
        this.gu = gu;
        this.dong = dong;
    }
}
