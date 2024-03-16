package utility;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import spark.Request;
import spark.Response;

public class Logging {
    public static String getMessage(Request req, Response res) {
        String message = String.format("Request| method: %s, " +
            "Uri: %s, queryParams: %s, Body: %s ||| " +
            "Response| status: %s, body: %s",
            req.requestMethod(),
            req.uri(),
            blurSecrets(req.queryMap().toMap()),
            req.body(),
            res.status(),
            res.body());
        
        return message;
    }

    private static String blurSecrets(Map<String, String[]> params) {
        String[] secrets = {"password"};
        String toReturn = "";

        for(String secret: secrets) {
            if(params.containsKey(secret)) {
                params.replace(secret, new String[]{"********"});
            }
        }

        for(String key: params.keySet()) {
            toReturn += key + ": " + Arrays.toString(params.get(key)) + ", ";
        }

        return toReturn;
     }

    public static String getException(Exception e) {
        return e.getClass() + ": " + e.getMessage();
    }
}
