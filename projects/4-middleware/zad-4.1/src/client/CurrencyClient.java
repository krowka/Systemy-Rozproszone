package client;

import grpc.exchange.ExchangeRateGrpc;
import grpc.exchange.ExchangeRateOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;


import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class CurrencyClient {
    private static final Logger logger = Logger.getLogger(CurrencyClient.class.getName());

    private final ExchangeRateGrpc.ExchangeRateBlockingStub blockingStub;
    private final ExchangeRateGrpc.ExchangeRateStub asyncStub;
    private final ManagedChannel channel;

    public CurrencyClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
                .usePlaintext(/*true*/)
                .build();
        blockingStub = ExchangeRateGrpc.newBlockingStub(channel);
        asyncStub = ExchangeRateGrpc.newStub(channel);
    }


    public static void main(String[] args) throws InterruptedException {
        CurrencyClient client = new CurrencyClient("localhost", 50051);
        client.run();
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private void run() throws InterruptedException {
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            try {
                ArrayList<ExchangeRateOuterClass.CurrencyCode> currencyCodes = new ArrayList<>(Arrays.asList(ExchangeRateOuterClass.CurrencyCode.values()));
                currencyCodes.remove(ExchangeRateOuterClass.CurrencyCode.UNRECOGNIZED);
                System.out.println("Supported currencies: " + currencyCodes);
                System.out.print("Enter currency \n==> ");
                System.out.flush();
                String currency = in.readLine();
                System.out.print("Enter value \n==> ");
                System.out.flush();
                float value = Float.parseFloat(in.readLine());
                System.out.print("Enter condition: LT (less than) or GT (greater than) \n==> ");
                System.out.flush();
                String relation = in.readLine();
                subscribe(relation, currency, value);
            } catch (java.io.IOException ex) {
                System.err.println(ex);
            }
        } finally {
            shutdown();
        }
    }


    public void subscribe(String relation, String currency, float value) {
        System.out.printf("[INFO] subscribe, condition: %s, currency : %s, value: %f\n\n", relation, currency, value);
        ExchangeRateOuterClass.Condition condition = makeCondition(relation, currency, value);

        Iterator<ExchangeRateOuterClass.CurrencyNotification> notifications;
        try {
            notifications = blockingStub.subscribe(condition);
            while (notifications.hasNext()) {
                printNotification(notifications.next());
            }
        } catch (StatusRuntimeException e) {
            logger.warning("RPC failed: " + e.getStatus());
        }

    }

    private void printNotification(ExchangeRateOuterClass.CurrencyNotification notification) {
        List<ExchangeRateOuterClass.CurrencyRate> currencyRateList = notification.getCurrencyRateList();
        System.out.println("====================================");
        if (currencyRateList.size() == 2) {
            System.out.println("PREVIOUS RATE: " + " " + currencyRateToString(currencyRateList.get(1)));
        }
        System.out.println("CURRENT RATE: " + " " + currencyRateToString(currencyRateList.get(0)));
        System.out.println("MESSAGE: " + notification.getMessage());
        System.out.println("====================================\n");
    }

    private String currencyRateToString(ExchangeRateOuterClass.CurrencyRate currencyRate) {
        return currencyRate.getCurrencyCode() + " " + currencyRate.getValue();
    }

    private ExchangeRateOuterClass.Condition makeCondition(String relation, String currency, float value) {
        ExchangeRateOuterClass.RelationOp relationOp = ExchangeRateOuterClass.RelationOp.valueOf(relation);
        ExchangeRateOuterClass.CurrencyRate currencyRate = makeCurrencyRate(currency, value);
        ExchangeRateOuterClass.Condition condition = ExchangeRateOuterClass.Condition.newBuilder()
                .setRelationOp(relationOp)
                .setCurrencyRate(currencyRate)
                .build();
        return condition;
    }

    private ExchangeRateOuterClass.CurrencyRate makeCurrencyRate(String currency, float value) {
        ExchangeRateOuterClass.CurrencyCode currencyCode = ExchangeRateOuterClass.CurrencyCode.valueOf(currency);
        ExchangeRateOuterClass.CurrencyRate currencyRate = ExchangeRateOuterClass.CurrencyRate.newBuilder()
                .setCurrencyCode(currencyCode)
                .setValue(value)
                .build();
        return currencyRate;
    }
}
