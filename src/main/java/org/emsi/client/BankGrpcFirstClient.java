package org.emsi.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.emsi.stubs.Bank;
import org.emsi.stubs.BankServiceGrpc;

public class BankGrpcFirstClient {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",5555)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("Euro")
                .setAmount(15425)
                .build();
        Bank.ConvertCurrencyResponse response =  blockingStub.convert(request);
        System.out.println(response);
    }
}