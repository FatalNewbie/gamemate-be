package com.example.gamemate.domain.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class GameApiResponse {

    @JsonProperty("result")
    private Result result;

    @Data
    public static class Result {
        @JacksonXmlProperty(localName = "tcount")
        private int totalCount;

        @JacksonXmlProperty(localName = "pageno")
        private int pageNo;

        @JacksonXmlProperty(localName = "res_date")
        private String responseDate;

        @JsonProperty("item")
        private List<GameItem> items;
    }

    @Data
    public static class GameItem {
        private String rateno;
        private String rateddate;
        private String gametitle;
        private String orgname;
        private String entname;
        private String summary;
        private String givenrate;
        private String genre;
        private String platform;
        private String discriptors;
        private boolean cancelstatus;
        private String canceleddate;
    }
}
