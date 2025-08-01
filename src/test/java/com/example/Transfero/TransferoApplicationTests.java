package com.example.Transfero;

import com.example.Transfero.service.GoogleSheetsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.main.lazy-initialization=true"
})
class TransferoApplicationTests {

	@MockBean
	private GoogleSheetsService googleSheetsService;

	@Test
	void contextLoads() {
	}
}
