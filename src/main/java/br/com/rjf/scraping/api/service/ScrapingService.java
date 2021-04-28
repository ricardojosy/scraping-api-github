package br.com.rjf.scraping.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.rjf.scraping.api.dto.Resumo;
import br.com.rjf.scraping.api.exception.NotFoundException;

@Service
public class ScrapingService {

	private static final String BASE_URL_GITHUB = "https://github.com";
	private static final String IDENTIFICADOR_DE_PASTA = "svg[class=octicon octicon-file-directory hx_color-icon-directory]";

	Map<String, Resumo> mapa = new HashMap<>();

	public String scraping(String usuariogit, String repositorio) {
		String url = BASE_URL_GITHUB + "/" + usuariogit + "/" + repositorio;

		Document document = null;

		try {
			document = Jsoup.connect(url).get();

			if (document == null) {
				throw new NotFoundException("Repository not found to given url: " + url);
			}

			trataElementos(document);

			List<Resumo> lista = new ArrayList<>();
			for (String s : mapa.keySet()) {
				Resumo resumo = mapa.get(s);
				lista.add(resumo);
			}

			Gson gson = new Gson();

			String jsonSaida = gson.toJson(lista);

			return jsonSaida;

		} catch (IOException e) {
			throw new NotFoundException("Repository not found to given url: " + url);
		}

	}

	private void trataElementos(Document document) throws IOException {
		Elements elementos = document
				.select("div[class=Box-row Box-row--focus-gray py-2 d-flex position-relative js-navigation-item]");
		for (Element elemento : elementos) {
			if (elemento.select(IDENTIFICADOR_DE_PASTA).attr("aria-label").isEmpty()) {
				computaArquivo(elemento.select("a[class=js-navigation-open Link--primary]").attr("href"));
			} else {
				String urlPasta = elemento.select("a[class=js-navigation-open Link--primary]").attr("href");
//				LOGGER.info("URL DA PASTA: {}", urlPasta);
				Document doc = Jsoup.connect(BASE_URL_GITHUB + urlPasta).get();
				trataElementos(doc);
			}
		}
	}

	private void computaArquivo(String url) throws IOException {
		Document doc = Jsoup.connect(BASE_URL_GITHUB + url).get();

		String[] partesUrl = url.substring(1).split("/");
		String ulltimaParte = partesUrl[partesUrl.length - 1];
		String[] partesNome = ulltimaParte.split("\\.");
		String extensao = partesNome[partesNome.length - 1];

		String trechoComQtdLinhas = doc.select("div[class=text-mono f6 flex-auto pr-3 flex-order-2 flex-md-order-1]")
				.text().replaceAll("executable file", "").replaceAll("Executable file", "");

		int linhas = 0;
		try {
			linhas = Integer.parseInt((trechoComQtdLinhas.split("lines")[0]).trim());
		} catch (NumberFormatException e) {
		}

		String tamanhoStr = trechoComQtdLinhas.split("\\)")[1].trim();
		String numeralDoTamanhoStr = tamanhoStr.split(" ")[0].trim();
		String unidadeDoTamanho = tamanhoStr.split(" ")[1].trim();

		Integer tamanhoInteiroEmBytes = 0;
		if (unidadeDoTamanho.equalsIgnoreCase("KB")) {
			double d = Double.parseDouble(numeralDoTamanhoStr);
			d = d * 1024;
			tamanhoInteiroEmBytes = (int) d;
		} else {
			tamanhoInteiroEmBytes = Integer.parseInt(numeralDoTamanhoStr);
		}

		Resumo resumo = new Resumo();

		if (mapa.containsKey(extensao)) {
			resumo = mapa.get(extensao);
			resumo.incrementa(linhas, tamanhoInteiroEmBytes);
			mapa.replace(extensao, resumo);
		} else {
			mapa.put(extensao, new Resumo(extensao, 1, linhas, tamanhoInteiroEmBytes));
		}
	}

}
