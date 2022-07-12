package com.planet.develop.Enum;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum money_Type {
    @JsonProperty("급여") salary,
    @JsonProperty("용돈") allowance,
    @JsonProperty("기타") etc,
    @JsonProperty("식비") food,
    @JsonProperty("교통") traffic,
    @JsonProperty("문화") culture,
    @JsonProperty("생필품") daily_necessity,
    @JsonProperty("마트") market,
    @JsonProperty("교육") study,
    @JsonProperty("통신") communication,
    @JsonProperty("의료/건강") medical_treatment,
    @JsonProperty("경조사/회비") congratulations,
    @JsonProperty("가전") home_appliances,
    @JsonProperty("공과금") dues
}