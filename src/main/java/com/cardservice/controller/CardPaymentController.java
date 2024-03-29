package com.cardservice.controller;

import com.cardservice.dto.CardRequestDto;
import com.cardservice.service.card.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardPaymentController {

    private final CardService cardService;

    @PostMapping("/old-certification")
    public Object keyIn(@RequestBody @Validated CardRequestDto cardKeyInRequestDto, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getFieldError().getDefaultMessage());
        }

        cardService.keyIn(cardKeyInRequestDto, request.getHeader("Authorization"), "keyIn");

        return "sex-king";
    }
}
