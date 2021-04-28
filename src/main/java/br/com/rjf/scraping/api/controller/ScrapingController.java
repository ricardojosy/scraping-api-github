package br.com.rjf.scraping.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rjf.scraping.api.exception.StandardError;
import br.com.rjf.scraping.api.service.ScrapingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("API to scraping public repository of Github")
@RestController
@RequestMapping("/api/v1/scraping")
public class ScrapingController {

	@Autowired
	private ScrapingService scrapingService;

	@ApiOperation(value = "Scraping public repository of Github responses json")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class),
			@ApiResponse(code = 400, message = "Bad request", response = StandardError.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
			@ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
			@ApiResponse(code = 404, message = "Not Found", response = StandardError.class),
			@ApiResponse(code = 500, message = "Internal server error", response = StandardError.class) })
	@GetMapping("/{username}/{repository}")
	public ResponseEntity<String> scraping(@PathVariable("username")  String usuariogit, @PathVariable("repository")  String repositorio) {
		return ResponseEntity.ok().body(scrapingService.scraping(usuariogit, repositorio));
	}

}
