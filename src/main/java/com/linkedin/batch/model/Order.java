package com.linkedin.batch.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    
    private Long orderId;

    private String firstName;

    private String lastName;

    private String email;

    private BigDecimal cost;

    private String itemId;

    private String itemName;

    private Date shipDate;
}
