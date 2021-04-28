
package br.com.rjf.scraping.api.exception;

import java.io.Serializable;
import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardError implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	public StandardError(HttpStatus httpStatus, String message, String path) {
		super();
		this.timestamp = new Date().getTime();
		this.status = httpStatus.value();
		this.error = httpStatus.name();
		this.message = message;
		this.path = path;
	}
	
}
