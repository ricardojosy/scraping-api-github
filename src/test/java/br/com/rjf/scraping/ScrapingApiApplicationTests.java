package br.com.rjf.scraping;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.rjf.scraping.api.exception.NotFoundException;
import br.com.rjf.scraping.api.service.ScrapingService;

import org.junit.jupiter.api.Assertions;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ScrapingApiApplicationTests {

	@Autowired
	private ScrapingService scrapingService;

	@Test
	void repositorioNaoExiste() {
		Assertions.assertThrows(NotFoundException.class, () -> {scrapingService.scraping("ricardojosy", "XXXX");});		
	}

}
