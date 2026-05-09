package com.sts.service;

import com.grpc.stockTrading.StockRequest;
import com.grpc.stockTrading.StockResponse;
import com.grpc.stockTrading.StockTradingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

//    @GrpcClient("stockService")
//    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;
//
//    public StockResponse getStockPrice(String stockSymbol) {
//        StockRequest request = StockRequest.newBuilder()
//                .setStockSymbol(stockSymbol)
//                .build();
//        return stockTradingServiceBlockingStub.getStockPrice(request);
//
//    }

        @GrpcClient("stockService")
        private StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;

        public void subscribeStockPrice(String stockSymbol) {
            StockRequest request = StockRequest.newBuilder()
                    .setStockSymbol(stockSymbol)
                    .build();

            stockTradingServiceStub.subscribeStockPrice(request, new io.grpc.stub.StreamObserver<StockResponse>() {
                @Override
                public void onNext(StockResponse stockResponse) {
                    System.out.println("Stock Price Update : " +stockResponse.getStockSymbol() + " : Price - " + + stockResponse.getPrice() + " Time :  " + stockResponse.getTimestamp());
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("Error receiving stock price updates: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Completed receiving stock price updates.");
                }
            });
        }
}
