package com.sts.service;

import com.grpc.stockTrading.StockRequest;
import com.grpc.stockTrading.StockResponse;
import com.grpc.stockTrading.StockTradingServiceGrpc;
import com.sts.entity.Stock;
import com.sts.repository.StockRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

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
}
