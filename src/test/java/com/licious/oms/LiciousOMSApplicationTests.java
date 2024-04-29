package com.licious.oms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"unit-test"})
class LiciousOMSApplicationTests {

	@Test
	void contextLoads() {
	}

}
