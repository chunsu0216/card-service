package com.cardservice.controller;

import com.cardservice.dto.ApiResponse;
import com.cardservice.dto.CardRequestDto;
import com.cardservice.service.card.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardPaymentController {

    private final CardService cardService;

    @PostMapping("/old-certification")
    public ResponseEntity<?> keyIn(@RequestBody @Validated CardRequestDto cardKeyInRequestDto, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        return (ResponseEntity<?>) cardService.keyIn(cardKeyInRequestDto, request.getHeader("Authorization"), "keyIn");
    }
}
