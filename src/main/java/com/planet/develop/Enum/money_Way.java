package com.planet.develop.Enum;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum money_Way {
    @JsonProperty("카드") card,
    @JsonProperty("은행") bank,
    @JsonProperty("현금") cash
}