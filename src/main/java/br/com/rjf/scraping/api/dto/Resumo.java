package br.com.rjf.scraping.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resumo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String extension;
	private Integer count;
	private Integer lines;
	private Integer bytes;

	public void incrementa(Integer linhas, Integer bytes) {
		this.count++;
		this.lines += linhas;
		this.bytes += bytes;
	}

}
