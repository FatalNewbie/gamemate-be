package com.example.gamemate.domain.game.service;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class GameApiClient {

    private final WebClient webClient;

    public GameApiClient(WebClient webClient) {
        this.webClient = webClient.mutate()
                .baseUrl("https://www.grac.or.kr/WebService/GameSearchSvc.asmx")
                .build();
    }

    public String fetchGames(String gametitle, String entname, String rateno, String startdate, String enddate, int display, int pageno) {
        try {
            String responseXml = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/game")
                            .queryParam("op", "game")
                            .queryParam("gametitle", gametitle)
                            .queryParam("entname", entname)
                            .queryParam("rateno", rateno)
                            .queryParam("startdate", startdate)
                            .queryParam("enddate", enddate)
                            .queryParam("display", display)
                            .queryParam("pageno", pageno)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        WebClientResponseException exception = new WebClientResponseException(
                                                "4xx Client Error", clientResponse.statusCode().value(),
                                                clientResponse.statusCode().toString(),
                                                clientResponse.headers().asHttpHeaders(),
                                                errorBody.getBytes(), null);
                                        return Mono.error(exception);
                                    })
                    )
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        WebClientResponseException exception = new WebClientResponseException(
                                                "5xx Server Error", clientResponse.statusCode().value(),
                                                clientResponse.statusCode().toString(),
                                                clientResponse.headers().asHttpHeaders(),
                                                errorBody.getBytes(), null);
                                        return Mono.error(exception);
                                    })
                    )
                    .bodyToMono(String.class)
                    .block();

            JSONObject xmlJSONObj = XML.toJSONObject(responseXml);
            return xmlJSONObj.toString(4);

        } catch (WebClientResponseException e) {
            // 에러 처리
            e.printStackTrace();
            return null;
        }
    }
}
