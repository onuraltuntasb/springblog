package com.onuraltuntas.springblog.model.dto;

import lombok.Data;

@Data
public class RackDTO {
    private Long id;
    private int number;
    private String rackName;
    private String locationIdentifier;
}
