package com.sts.service;

import com.grpc.stockTrading.*;
import com.sts.entity.Stock;
import com.sts.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@GrpcService
public class StockTradingServiceImpl  extends  StockTradingServiceGrpc.StockTradingServiceImplBase {

    private  final  StockRepository stockRepository;

    public StockTradingServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        String symbolName = request.getStockSymbol();
        Stock stockEntity = stockRepository.findByStockSymbol(symbolName);
        StockResponse stockResponse = StockResponse.newBuilder()
                .setStockSymbol(stockEntity.getStockSymbol())
                .setPrice(stockEntity.getPrice())
                .setTimestamp(stockEntity.getLastUpdated().toString())
                .build();

        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void subscribeStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        String symbolName = request.getStockSymbol();

        try {
            for(int i = 0 ; i < 10 ; i++){
                StockResponse stockResponse = StockResponse.newBuilder()
                        .setStockSymbol(symbolName)
                        .setPrice(new Random().nextDouble(200)) // Simulating price updates
                        .setTimestamp(Instant.now().toString())
                        .build();

                responseObserver.onNext(stockResponse);
                TimeUnit.SECONDS.sleep(1); // Simulating delay between updates
            }
                responseObserver.onCompleted();
        }catch (InterruptedException e) {
            responseObserver.onError(e);
        }
    }

    public StreamObserver<StockOrder> bulkStockOrder(StreamObserver<OrderSummary> responseObserver) {

        return  new StreamObserver<StockOrder>() {

            private int totalOrders = 0;
            private double totalAmount = 0.0;
            private int successCount = 0;

            @Override
            public void onNext(StockOrder stockOrder) {
                totalOrders++;
                totalAmount += stockOrder.getPrice() * stockOrder.getQuantity();
                successCount++;
                System.out.println("Received order: " + stockOrder);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error receiving stock orders: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                OrderSummary orderSummary = OrderSummary.newBuilder()
                        .setTotalOrders(totalOrders)
                        .setTotalAmount(totalAmount)
                        .setSuccessCount(successCount)
                        .build();
                responseObserver.onNext(orderSummary);
                responseObserver.onCompleted();
            }
        };
    }
}
