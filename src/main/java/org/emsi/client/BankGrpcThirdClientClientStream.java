package org.emsi.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.emsi.stubs.Bank;
import org.emsi.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BankGrpcThirdClientClientStream {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",5555)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("Euro")
                .setAmount(154)
                .build();
        StreamObserver<Bank.ConvertCurrencyRequest> performStream = asyncStub.performStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("***********************");
                System.out.println(convertCurrencyResponse);
                System.out.println("***********************");

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("End ... ");
            }
        });

        Timer timer =  new Timer();
        timer.schedule(new TimerTask() {

                           int counter = 0;

                           @Override
                           public void run() {
                               Bank.ConvertCurrencyRequest currencyRequest = Bank.ConvertCurrencyRequest.newBuilder()
                                       .setAmount(Math.random() * 6000)
                                       .build();
                               performStream.onNext(currencyRequest);
                               System.out.println("counter= " + counter +1);
                               ++counter;
                               if (counter == 20) {
                                   performStream.onCompleted();
                                   timer.cancel();
                               }
                           }
                       }, 1000, 1000);
        System.out.println(".....................");
        System.in.read();
    }
}