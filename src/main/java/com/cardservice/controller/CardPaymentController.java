package com.cardservice.controller;

import com.cardservice.dto.ApiResponse;
import com.cardservice.dto.CancelRequestDto;
import com.cardservice.dto.CardRequestDto;
import com.cardservice.service.card.CardApproveService;
import com.cardservice.service.card.CardCancelService;
import com.cardservice.service.card.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardPaymentController {

    private final CardApproveService cardApproveService;
    private final CardCancelService cardCancelService;

    @PostMapping("/old-certification")
    public ResponseEntity<?> keyIn(@RequestBody @Validated CardRequestDto cardKeyInRequestDto, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return (ResponseEntity<?>) cardApproveService.keyIn(cardKeyInRequestDto, request.getHeader("Authorization"), "keyIn");
    }

    @PostMapping("/{transactionId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable(name = "transactionId") String transactionId,
                                    @RequestBody CancelRequestDto cancelRequestDto,
                                    HttpServletRequest request){

        return (ResponseEntity<?>) cardCancelService.cancel(transactionId, cancelRequestDto, request.getHeader("Authorization"));
    }
}
