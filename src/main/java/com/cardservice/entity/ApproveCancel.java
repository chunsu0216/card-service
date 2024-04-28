package com.cardservice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;


@Entity
@Table(name = "APPROVE_CANCEL")
@Getter
public class ApproveCancel {

    @Id
    private Long idx;
    private String cancelTransactionId;
    private String terminalId;
    private String merchantId;
    private String status;
    private Long amount;
    private String rootTransactionId;
    private String van;
    private String vanId;
    private String vanTrxId;
    private String vanResultCode;
    private String vanResultMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPROVE_IDX")
    private Approve approve;


    public ApproveCancel() {
    }

}
