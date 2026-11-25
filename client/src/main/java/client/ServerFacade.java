package client;

import com.google.gson.Gson;
import model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private final Gson gson = new Gson();
    private String authToken = null;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    private HttpRequest buildRequest(String method, String path, String body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null && !authToken.isEmpty()) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(String request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(request);
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError,ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            throw errorFrom(response);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private ResponseException errorFrom(HttpResponse<String> response) {
        var status = response.statusCode();
        try {
            return ResponseException.fromJson(response.body());
        } catch (RuntimeException ignored) {
            return new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public RegisterResult register(String[] params) throws ResponseException {
        UserData input = new UserData(params[0],params[1],params[2]);
        var request = buildRequest("POST", "/user", gson.toJson(input));
        var response = sendRequest(request);
        RegisterResult result = handleResponse(response, RegisterResult.class);
        this.authToken = result.authToken();
        return result;
    }

    public LoginResult login(String[] params) throws ResponseException {
        LoginRequest input = new LoginRequest(params[0],params[1]);
        var request = buildRequest("POST", "/session", gson.toJson(input));
        var response = sendRequest(request);
        LoginResult result = handleResponse(response, LoginResult.class);
        this.authToken = result.authToken();
        return result;
    }
    public ListGameResult list() throws ResponseException {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, ListGameResult.class);
    }

    public void logout() throws ResponseException {
        var request = buildRequest("DELETE", "/session", null);
        var response = sendRequest(request);
        handleResponse(response, null);
        this.authToken = null;
    }

    public CreateGameResult create(String[] params) throws ResponseException {
        CreateGameRequest input = new CreateGameRequest(authToken,params[0]);
        var request = buildRequest("POST", "/game", gson.toJson(input));
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class);

    }

    public boolean isSignedIn() {
        return authToken != null;
    }
}
