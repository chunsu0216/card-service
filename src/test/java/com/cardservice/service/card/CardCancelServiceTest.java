package com.cardservice.service.card;

import com.cardservice.dto.CancelRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
class CardCancelServiceTest {

    CancelRequestDto cancelRequestDto;
    String authorization;

    @Autowired
    private CardCancelService cardCancelService;

    @BeforeEach
    void setUp(){
        this.authorization = "keyin1";
        this.cancelRequestDto = new CancelRequestDto();
        this.cancelRequestDto.setCancelAmount(1000L);
    }

    @Test
	@DisplayName("@Lock Test")
	void decreaseAmount() throws InterruptedException{
		AtomicInteger successCount = new AtomicInteger();
		int numberOfExcute = 100;
		ExecutorService service = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(numberOfExcute);

		for(int i = 0; i < numberOfExcute; i++){
			service.execute(()->{
				try {
					cardCancelService.cancel("T8961f9cf-e670-4838-a1a3-f42dbfb015d4", cancelRequestDto, authorization);
					successCount.getAndIncrement();
					System.out.println("성공");
				}catch (Exception oe){
					System.out.println("충돌감지");
				}
				latch.countDown();
			});
		}
		latch.await();
		assertThat(successCount.get()).isEqualTo(20);
	}

}