package com.sts.service;

import com.grpc.stockTrading.*;
import io.grpc.stub.StreamObserver;
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

        public void placeBulkStockOrders() {
            StreamObserver<OrderSummary> responseObserver = new StreamObserver<OrderSummary>() {
                @Override
                public void onNext(OrderSummary orderSummary) {
                    System.out.println("Received Order Summary: ");
                    System.out.println("Total Orders: " + orderSummary.getTotalOrders());
                    System.out.println("Total Amount: " + orderSummary.getTotalAmount());
                    System.out.println("Successful Orders: " + orderSummary.getSuccessCount());
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("Error placing bulk stock orders: " + t.getMessage());
                }

                @Override
                public void onCompleted() {
                    System.out.println("Completed placing bulk stock orders.");
                }
            };

            StreamObserver<StockOrder> requestObserver =  stockTradingServiceStub.bulkStockOrder(responseObserver);

            // Send Multiple Stock Orders
            try{
                requestObserver.onNext(StockOrder.newBuilder().setStockSymbol("AAPL").setQuantity(10).setPrice(150.0).build());
                requestObserver.onNext(StockOrder.newBuilder().setStockSymbol("GOOGL").setQuantity(5).setPrice(2800.0).build());
                requestObserver.onNext(StockOrder.newBuilder().setStockSymbol("AMZN").setQuantity(2).setPrice(3400.0).build());
                requestObserver.onNext(StockOrder.newBuilder().setStockSymbol("MSFT").setQuantity(8).setPrice(300.0).build());

                requestObserver.onCompleted();

            }catch (Exception e){
                requestObserver.onError(e);
            }

        }
}
