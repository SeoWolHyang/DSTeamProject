package com.SecondHand.item;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jdk.jfr.Timestamp;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 고유ID
    private String title;   // 상품명
    private Integer price;  // 가격
    private String imgURL;  // 이미지
    private String desc;    // 설명

    private String category;    // 카테고리

    @CreationTimestamp
    private LocalDateTime uploadDate;   //등록 날짜




}
