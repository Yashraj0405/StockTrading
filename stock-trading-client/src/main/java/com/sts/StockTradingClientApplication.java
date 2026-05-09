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

        //gRPC Unary Call
//        System.out.println("Requesting stock price for: " + stockSymbol);
//        var response = stockClientService.getStockPrice(stockSymbol);
//        System.out.println("Received stock price: " + response.getPrice());
//        System.out.println("Received stock at: " + response.getTimestamp());

        //gRPC Server Streaming
//        System.out.println("Subscribing to stock price updates for: " + stockSymbol);
//        stockClientService.subscribeStockPrice(stockSymbol);

        //gRPC Client Streaming
//        System.out.println("Placing bulk stock orders...");
//        stockClientService.placeBulkStockOrders();

        //gRPC Bi-Directional Streaming
        System.out.println("Starting bi-directional streaming for stock orders...");
        stockClientService.startTrading();
    }
}
