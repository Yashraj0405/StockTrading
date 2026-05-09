package com.sts.service;

import com.grpc.stockTrading.StockRequest;
import com.grpc.stockTrading.StockResponse;
import com.grpc.stockTrading.StockTradingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build();
        return stockTradingServiceBlockingStub.getStockPrice(request);

    }
}
