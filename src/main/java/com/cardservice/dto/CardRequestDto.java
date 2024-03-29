package com.cardservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

@Data
public class CardRequestDto {

    @NotNull(message = "주문 ID는 필수 값입니다.")
    @NotEmpty(message = "주문 ID는 필수 값입니다.")
    private String orderId;

    @NotNull(message = "주문자명은 필수 값입니다.")
    @NotEmpty(message = "주문자명은 필수 값입니다.")
    private String orderName;

    @NotNull(message = "상품명은 필수 값입니다.")
    @NotEmpty(message = "상품명은 필수 값입니다.")
    private String productName;

    @NotNull(message = "금액은 필수 값입니다.")
    @PositiveOrZero(message = "금액은 양수 값만 가능합니다.")
    @Min(value = 100, message = "금액은 100원 이상만 가능합니다.")
    private Long amount;

    @NotNull(message = "카드번호는 필수 값입니다.")
    @NotEmpty(message = "카드번호는 필수 값입니다.")
    @CreditCardNumber
    private String cardNumber;

    @NotNull(message = "유효기간은 필수 값입니다.")
    @NotEmpty(message = "유효기간은 필수 값입니다.")
    @Size(min = 4, max = 4, message = "유효기간은 4자리입니다 YYMM 형식을 맞춰주세요")
    @Pattern(regexp = "^[0-9]*$", message = "유효기간은 숫자만 입력 가능합니다.")
    private String expireDate;

    private String password;

    @NotNull(message = "할부기간은 필수 값입니다.")
    @NotEmpty(message = "할부기간은 필수 값입니다.")
    private String installment;

    private String userInfo;

}
