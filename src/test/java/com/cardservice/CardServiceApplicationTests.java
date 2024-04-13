package com.cardservice;

import com.cardservice.client.CommonServiceClient;
import com.cardservice.dto.CardRequestDto;
import com.cardservice.message.ApiResponseMessage;
import com.cardservice.repository.CardRequestRepository;
import com.cardservice.service.card.CardRequestService;
import com.cardservice.service.card.CardService;
import com.cardservice.service.card.CardServiceImpl;
import com.cardservice.util.ValidationUtil;
import feign.FeignException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
class CardServiceApplicationTests {

	@Autowired
	private CardServiceImpl cardService;
	@Autowired
	private CardRequestRepository cardRequestRepository;
	@Autowired
	private CommonServiceClient commonServiceClient;
	@Autowired
	private CardRequestService cardRequestService;

	CardRequestDto cardRequestDto;
	String authorization;
	String method;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	void setup() {
		this.authorization = "keyin1";
		this.method = "keyIn";
		this.cardRequestDto = new CardRequestDto();
		cardRequestDto.setOrderId("order1");
		cardRequestDto.setOrderName("orderName1");
		cardRequestDto.setProductName("productName1");
		cardRequestDto.setAmount(1000L);
		cardRequestDto.setCardNumber("4890168119959906");
		cardRequestDto.setExpireDate("2705");
		cardRequestDto.setPassword("11");
		cardRequestDto.setInstallment("0");
		cardRequestDto.setUserInfo("940216");
	}

	@Test
	@DisplayName("카드 유효기간 검증")
	void validExpireDate() {
		cardRequestDto.setExpireDate("2301");
		assertThatThrownBy(() -> cardService.keyIn(cardRequestDto, authorization, method))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(ApiResponseMessage.INVALID_EXPIRE_DATE.message());
	}
	@Test
	@DisplayName("생년월일 검증")
	void validUserInfo() {
		cardRequestDto.setUserInfo("940230");
		assertThatThrownBy(() -> cardService.keyIn(cardRequestDto, authorization, method))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(ApiResponseMessage.INVALID_BIRTHDAY.message());
	}

	@Test
	@DisplayName("비밀번호 자릿수 검증")
	void validPassword() {
		cardRequestDto.setPassword("123");
		assertThatThrownBy(() -> cardService.keyIn(cardRequestDto, authorization, method))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(ApiResponseMessage.INVALID_PASSWORD.message());
	}

	@Test
	@DisplayName("공통 서비스 호출 이후 에러 응답 시 exception 처리")
	void callCommonService() {
		// given 존재하지않은 authorization 키 셋팅
		authorization = "123";
		assertThatThrownBy(() -> cardService.keyIn(cardRequestDto, authorization, method))
				.isInstanceOf(FeignException.Unauthorized.class);
	}

	@Test
	@DisplayName("중복 거래 검증")
	void isDuplicateOrderId() {
		cardRequestDto.setOrderId("a183");
		assertThatThrownBy(() -> cardService.keyIn(cardRequestDto, authorization, method))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage(ApiResponseMessage.DUPLICATION_ORDER_ID.message());
	}
}
