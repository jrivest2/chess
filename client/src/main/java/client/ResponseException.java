package client;

import com.google.gson.Gson;

import java.util.HashMap;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
        AuthError,
        NotFound,
    }

    public ResponseException(Code code, String message) {
        super(message);
    }

    public static ResponseException fromJson(String json) {
        var map = new Gson().fromJson(json, HashMap.class);
        var status = Code.valueOf(map.get("status").toString());
        String message = map.get("message").toString();
        return new ResponseException(status, message);
    }

    public static Code fromHttpStatusCode(int httpStatusCode) {
        if (httpStatusCode == 404) {
            return Code.NotFound;
        }
        if (httpStatusCode >= 400 && httpStatusCode < 500) {
            return Code.ClientError;
        }
        return Code.ServerError;
    }
}