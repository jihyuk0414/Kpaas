package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentsRes {
    private Boolean charge ;
    private int point ;
    private String message ;

    @Builder
    public PaymentsRes(Boolean charge, int point, String message)
    {
        this.charge = charge;
        this.point = point;
        this.message = message ;
    }
}
