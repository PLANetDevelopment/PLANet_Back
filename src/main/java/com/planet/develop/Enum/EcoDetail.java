package com.planet.develop.Enum;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EcoDetail {
    @JsonProperty("친환경 제품 구매") ecoProducts,
    @JsonProperty("비건식당 방문") vegan,
    @JsonProperty("다회용기 사용") multiUse,
    @JsonProperty("장바구니/개인가방 사용") personalBag,
    @JsonProperty("중고거래/나눔/기부") sharing,
    @JsonProperty("일회용품 사용") disposable,
    @JsonProperty("비닐봉투 소비") plasticBag,
    @JsonProperty("식자재 낭비") wasteFood,
    @JsonProperty("기타") etc,
    @JsonProperty("사용자 추가") userAdd
}
