package com.careerdevs.StocksAPI.Controllers;

import com.careerdevs.StocksAPI.Models.*;
import com.careerdevs.StocksAPI.Parsers.CSVParser;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final String URL = "";

    @Autowired
    private Environment env;

    @GetMapping("/feature1")
    public List<CompCSV> sortName(RestTemplate restTemplate) {
        List<CompCSV> newList = CSVParser.readCSV("src/dataset_0.csv");
        assert newList != null;
        newList.sort(Comparator.comparing(CompCSV::getName));
        return newList;
    }
    @GetMapping("/feature2")
    public List<CompCSV> sortIPO(RestTemplate restTemplate) {
        List<CompCSV> newList = CSVParser.readCSV("src/dataset_0.csv");
        assert newList != null;
        newList.sort(Comparator.comparing(CompCSV::getIpoDate).thenComparing(CompCSV::getName));
        return newList;
    }
    @GetMapping("/feature3")
    public List<CompCSV> getNASDAQ(RestTemplate restTemplate) {
        List<CompCSV> newList = CSVParser.readCSV("src/dataset_0.csv");
        assert newList != null;
        newList.removeIf(csv -> !Objects.equals(csv.getExchange(), "NASDAQ"));
        newList.sort(Comparator.comparing(CompCSV::getName));
        return newList;
    }
    @GetMapping("/feature4")
    public List<CompCSV> getNYSE(RestTemplate restTemplate) {
        List<CompCSV> newList = CSVParser.readCSV("src/dataset_0.csv");
        assert newList != null;
        newList.removeIf(csv -> !Objects.equals(csv.getExchange(), "NYSE"));
        newList.sort(Comparator.comparing(CompCSV::getName));
        return newList;
    }

    @GetMapping("/feature5")
    public ArrayList<CompAV> noSort(RestTemplate restTemplate) {
        ArrayList<CompAV> endList = new ArrayList<>();
        try {
            ArrayList<CompAV> newList = getShit(restTemplate);
            for(CompAV av : newList){
                CompAV temp = new CompAV();
                temp.setSymbol(av.getSymbol());
                temp.setName(av.getName());
                temp.setDescription(av.getDescription());
                temp.setAddress(av.getAddress());
                endList.add(temp);
            }
            return endList;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return endList;
        }

    }



    @GetMapping("/feature6")
    public ArrayList<CompAV> sortMarket(RestTemplate restTemplate){
        ArrayList<CompAV> endList = new ArrayList<>();
        try {

            ArrayList<CompAV> newList = getShit(restTemplate);
            for(CompAV av : newList){
                CompAV temp = new CompAV();
                temp.setName(av.getName());
                temp.setSymbol(av.getSymbol());
                temp.setMarketCapitalization(av.getMarketCapitalization());
                endList.add(temp);
            }

            endList.sort(Comparator.comparing(CompAV::getMarketCapitalization, Comparator.comparingLong(Long::parseLong)));
            Collections.reverse(endList);
            for (CompAV av : endList){
                String[] pre = av.getMarketCapitalization().split("");
                int counter = 0;
                for (int i = pre.length - 1; i >= 0; i--) {
                    if(counter == 3){
                        pre[i] += ",";
                        counter = 0;
                    }
                    counter++;
                }
                String post = String.join("",pre);
                av.setMarketCapitalization(post);
            }
            return endList;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return endList;
        }

    }

    @GetMapping("/feature7")
    public ArrayList<CompAV> sortDivided(RestTemplate restTemplate){
        ArrayList<CompAV> endList = new ArrayList<>();
        try {
            ArrayList<CompAV> newList = getShit(restTemplate);
            newList.removeIf(av -> Objects.equals(av.getDividendDate(), "None"));

            for(CompAV av : newList){
                CompAV temp = new CompAV();
                temp.setName(av.getName());
                temp.setSymbol(av.getSymbol());
                temp.setDividendDate(av.getDividendDate());
                endList.add(temp);
            }
            endList.sort(Comparator.comparing(CompAV::getDividendDate, Comparator.comparingInt(this::diffinDays)));
            return endList;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return endList;

        }

    }


    public ArrayList<CompAV> getShit(RestTemplate restTemplate){
        List<CompCSV> newList = CSVParser.readCSV("src/dataset_2.csv");
        assert newList != null;
        newList.sort(Comparator.comparing(CompCSV::getName));
        //return newList;
        ArrayList<CompAV> endStocks = new ArrayList<>();
        for(CompCSV comp:newList){
            endStocks.add(getOverview(restTemplate,comp.getSymbol()));
        }
        return endStocks;
    }

    public int diffinDays(String date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1Full = LocalDate.parse(date, dtf);
        LocalDate date2Full = LocalDate.now();
        int lastInt = (int) ChronoUnit.DAYS.between(date2Full, date1Full);
        if(lastInt < 0){
            lastInt += 365;
        }
        return lastInt;
    }
    

    @GetMapping("/overview/{symbol}")
    public CompAV getOverview(RestTemplate restTemplate, @PathVariable String symbol){
        String URL_O = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + symbol.toUpperCase() +  "&apikey=" + env.getProperty("api.key");
        return restTemplate.getForObject(URL_O, CompAV.class);
    }
}
