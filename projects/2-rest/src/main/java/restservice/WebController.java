package restservice;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WebController {


    @GetMapping("/exchange")
    public String getMean(@RequestParam String currency, @RequestParam(defaultValue = "") String date) {
        List<Double> values = new ArrayList<>();
        values.add(getValueFromNbp(currency, date));
        values.add(getValueFromOpenRates(currency, date));
        values.add(getValueFromRatesApi(currency, date));
        values.add(getValueFromFrankfurter(currency, date));
        if (date.equals(""))
            values.add(getValueFromAmdoren(currency));

        List<Double> resultList = values.stream().filter(value -> value > 0.0).collect(Collectors.toList());
        double sum = resultList.stream().mapToDouble(Double::doubleValue).sum();
        double mean = sum / resultList.size();

        String dateString = date.equals("") ? " dzisiaj" : " w dniu " + date;
        return "Sredni kurs " + currency.toUpperCase() + dateString + " = " + mean + " PLN";
    }

    @GetMapping("/joke")
    public String getJoke(@RequestParam String category) throws IOException {
        String sURL = "https://sv443.net/jokeapi/category/" + category;
        JsonElement root = getRootJsonFromUrl(sURL);
        JsonObject rootobj = root.getAsJsonObject();
        System.out.println(rootobj);
        String type = rootobj.get("type").getAsString();
        String joke = "";
        if (type.equals("twopart")) {
            joke = rootobj.get("setup").getAsString() + " -\n- " + rootobj.get("delivery").getAsString();
        } else {
            joke = rootobj.get("joke").getAsString();
        }
        return joke;

    }

    private double getValueFromNbp(String currency, String date) {
        try {
            String sURL = "http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/" + date;
            JsonElement root = getRootJsonFromUrl(sURL);
            JsonObject rootobj = root.getAsJsonObject();
            JsonObject rateJson = rootobj.getAsJsonArray("rates").get(0).getAsJsonObject();
            String value = rateJson.get("mid").getAsString();
            return Double.parseDouble(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private double getValueFromOpenRates(String currency, String date) {
        try {
            String d = date.equals("") ? "latest" : date;
            String sURL = "https://api.exchangeratesapi.io/" + d + "?base=" + currency.toUpperCase() + "&symbols=PLN";
            JsonElement root = getRootJsonFromUrl(sURL);
            JsonObject rootobj = root.getAsJsonObject();
            JsonObject rateJson = rootobj.getAsJsonObject("rates").getAsJsonObject();
            String value = rateJson.get("PLN").getAsString();
            return Double.parseDouble(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private double getValueFromRatesApi(String currency, String date) {
        try {
            String d = date.equals("") ? "latest" : date;
            String sURL = "https://api.ratesapi.io/api/" + d + "?base=" + currency.toUpperCase() + "&symbols=PLN";
            JsonElement root = getRootJsonFromUrl(sURL);
            JsonObject rootobj = root.getAsJsonObject();
            JsonObject rateJson = rootobj.getAsJsonObject("rates").getAsJsonObject();
            String value = rateJson.get("PLN").getAsString();
            return Double.parseDouble(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private double getValueFromFrankfurter(String currency, String date) {
        try {
            String d = date.equals("") ? "latest" : date;
            String sURL = "https://api.frankfurter.app/" + d + "?from=" + currency + "&to=pln";
            JsonElement root = getRootJsonFromUrl(sURL);
            JsonObject rootobj = root.getAsJsonObject();
            JsonObject rateJson = rootobj.getAsJsonObject("rates").getAsJsonObject();
            String value = rateJson.get("PLN").getAsString();
            return Double.parseDouble(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }


    private double getValueFromAmdoren(String currency) {

        try {
            String sURL = "https://www.amdoren.com/api/currency.php?api_key=z4fVCiV3jJbCL8k6FhkMP9BCm9q8Qq&from="
                    + currency.toUpperCase() + "&to=PLN";
            JsonElement root = getRootJsonFromUrl(sURL);
            JsonObject rootobj = root.getAsJsonObject();
            System.out.println(rootobj);
            String value = rootobj.get("amount").getAsString();
            return Double.parseDouble(value);
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