package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import grpc.exchange.ExchangeRateGrpc;
import grpc.exchange.ExchangeRateOuterClass;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class CurrencyServer {
    private static final Logger logger = Logger.getLogger(CurrencyServer.class.getName());
    private final int port;
    private final Server server;


    public CurrencyServer(int port) {
        this.port = port;
        server = ServerBuilder.forPort(port).addService(new ExchangeRateService()).build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    CurrencyServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    public static void main(String[] args) throws Exception {
        CurrencyServer server = new CurrencyServer(50051);
        server.start();
        server.blockUntilShutdown();
    }

    private static class ExchangeRateService extends ExchangeRateGrpc.ExchangeRateImplBase {
        @Override
        public void subscribe(ExchangeRateOuterClass.Condition request, StreamObserver<ExchangeRateOuterClass.CurrencyNotification> responseObserver) {
            List<ExchangeRateOuterClass.CurrencyRate> currencyRateList;
            float desiredValue = request.getCurrencyRate().getValue();
            ExchangeRateOuterClass.CurrencyCode currencyCode = request.getCurrencyRate().getCurrencyCode();
            ExchangeRateOuterClass.RelationOp relationOp = request.getRelationOp();
            String relationOpString = relationOp == ExchangeRateOuterClass.RelationOp.GT ? "greater than" : "less than";
            String info = String.format("Got subscription for condition: %s %s %f", currencyCode, relationOpString, desiredValue);
            logger.info(info);

            float currentValue = getCurrencyRate(currencyCode);
            Predicate<Float> predicate = makePredicate(relationOp, desiredValue);

            ExchangeRateOuterClass.CurrencyRate prevCurrencyRate = null;

            while (!predicate.test(currentValue)) {
                currencyRateList = new ArrayList<>();

                ExchangeRateOuterClass.CurrencyRate currencyRate = makeCurrencyRate(currencyCode, currentValue);
                currencyRateList.add(currencyRate);

                if (prevCurrencyRate != null)
                    currencyRateList.add(prevCurrencyRate);

                String message = "Condition not fulfilled.";
                ExchangeRateOuterClass.CurrencyNotification notification = makeNotification(currencyRateList, message);
                responseObserver.onNext(notification);

                prevCurrencyRate = currencyRate;

                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            currencyRateList = new ArrayList<>();
            ExchangeRateOuterClass.CurrencyRate currencyRate = makeCurrencyRate(currencyCode, currentValue);
            currencyRateList.add(currencyRate);
            if (prevCurrencyRate != null)
                currencyRateList.add(prevCurrencyRate);


            String message = "Condition fulfilled! \nGo and make money!";
            ExchangeRateOuterClass.CurrencyNotification currencyNotification = makeNotification(currencyRateList, message);
            responseObserver.onNext(currencyNotification);
            responseObserver.onCompleted();

        }

        private Predicate<Float> makePredicate(ExchangeRateOuterClass.RelationOp relationOp, float desiredValue) {
            return relationOp == ExchangeRateOuterClass.RelationOp.GT ? x -> x > desiredValue : x -> x < desiredValue;
        }

        private ExchangeRateOuterClass.CurrencyNotification makeNotification(List<ExchangeRateOuterClass.CurrencyRate> currencyRateList, String message) {
            ExchangeRateOuterClass.CurrencyNotification result = ExchangeRateOuterClass.CurrencyNotification
                    .newBuilder()
                    .addAllCurrencyRate(currencyRateList)
                    .setMessage(message)
                    .build();
            return result;
        }

        private ExchangeRateOuterClass.CurrencyRate makeCurrencyRate(ExchangeRateOuterClass.CurrencyCode currencyCode, float currentValue) {
            ExchangeRateOuterClass.CurrencyRate result = ExchangeRateOuterClass.CurrencyRate
                    .newBuilder()
                    .setCurrencyCode(currencyCode)
                    .setValue(currentValue)
                    .build();
            return result;
        }

        private float getCurrencyRate(ExchangeRateOuterClass.CurrencyCode currencyCode) {
//            try {
//                String sURL = "http://api.nbp.pl/api/exchangerates/rates/a/" + currencyCode;
//                JsonElement root = getRootJsonFromUrl(sURL);
//                JsonObject rootObj = root.getAsJsonObject();
//                JsonObject rateJson = rootObj.getAsJsonArray("rates").get(0).getAsJsonObject();
//                String value = rateJson.get("mid").getAsString();
//                return Float.parseFloat(value);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return -1;


            try {
                String d = "latest";
                String sURL = "https://api.exchangeratesapi.io/" + d + "?base=" + currencyCode.toString().toUpperCase() + "&symbols=PLN";
                JsonElement root = getRootJsonFromUrl(sURL);
                JsonObject rootobj = root.getAsJsonObject();
                JsonObject rateJson = rootobj.getAsJsonObject("rates").getAsJsonObject();
                String value = rateJson.get("PLN").getAsString();
                return Float.parseFloat(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        private JsonObject getRootJsonFromUrl(String stringUrl) throws IOException {
            URL url = new URL(stringUrl);
            URLConnection request = url.openConnection();
            request.connect();
            JsonParser jp = new com.google.gson.JsonParser();
            return jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
        }
    }
}