package com.sts;

import com.sts.service.StockClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockTradingClientApplication  implements CommandLineRunner {

    private StockClientService stockClientService;

    public StockTradingClientApplication(StockClientService stockClientService) {
        this.stockClientService = stockClientService;
    }

	public static void main(String[] args) {
		SpringApplication.run(StockTradingClientApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        String stockSymbol = "INFY";
        System.out.println("Requesting stock price for: " + stockSymbol);
        var response = stockClientService.getStockPrice(stockSymbol);
        System.out.println("Received stock price: " + response.getPrice());
        System.out.println("Received stock at: " + response.getTimestamp());
    }
}
