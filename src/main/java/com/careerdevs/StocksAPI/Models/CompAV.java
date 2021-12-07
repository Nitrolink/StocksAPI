package com.careerdevs.StocksAPI.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompAV {

    private String Symbol;

    private String AssetType;

    private String Name;

    private String Description;

    private String Address;

    private String MarketCapitalization;

    private String DividendDate;

    public String getSymbol() {
        return Symbol;
    }
    @JsonProperty("Symbol")
    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getAssetType() {
        return AssetType;
    }
    @JsonProperty("AssetType")
    public void setAssetType(String assetType) {
        AssetType = assetType;
    }

    public String getName() {
        return Name;
    }
    @JsonProperty("Name")
    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }
    @JsonProperty("Description")
    public void setDescription(String description) {
        Description = description;
    }

    public String getAddress() {
        return Address;
    }
    @JsonProperty("Address")
    public void setAddress(String address) {
        Address = address;
    }

    public String getMarketCapitalization() {
        return MarketCapitalization;
    }

    @JsonProperty("MarketCapitalization")
    public void setMarketCapitalization(String marketCapitalization) {
        MarketCapitalization = marketCapitalization;
    }

    public String getDividendDate() {
        return DividendDate;
    }

    @JsonProperty("DividendDate")
    public void setDividendDate(String dividendDate) {
        DividendDate = dividendDate;
    }
}
