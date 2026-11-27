package client;

import dataaccess.DataAccessException;
import model.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static UserService userService;
    private static ServerFacade serverFacade;
    @BeforeAll
    public static void init() {
        server = new Server();
        try {
            userService = new UserService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var port = server.run(0);
        String serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup() {
        userService.clearDB();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void facadeTest() {
        String[] goodParams = {"testUser", "testPass", "testEmail"};
        String[] badParams = {"", "", ""};
        RegisterResult result = new RegisterResult("","");
        try {
            //Test register
            result = serverFacade.register(goodParams);
            Assertions.assertEquals("testUser", result.username());
            Assertions.assertNotEquals("", result.authToken());
            assertTrue(serverFacade.isSignedIn());
            Assertions.assertThrows(ResponseException.class,
                    () -> {
                        serverFacade.register(goodParams);});
            Assertions.assertThrows(ResponseException.class,
                    () -> {
                        serverFacade.register(badParams);});

            //Test create successful
            serverFacade.create(goodParams);
            //Test list successful
            assertDoesNotThrow(() -> {
                serverFacade.list();});

            //Test join/play
            var response = serverFacade.list();
            int id = response.games().getFirst().gameID();
            assertDoesNotThrow(() -> {
                serverFacade.join("white",id);});
            assertDoesNotThrow(() -> {
                serverFacade.join("black",id);});
            assertThrows(ResponseException.class, () -> {
                serverFacade.join("white",1);});
            assertThrows(ResponseException.class, () -> {
                serverFacade.join("black",1);});

            //Test logout
            serverFacade.logout();
            Assertions.assertFalse(serverFacade.isSignedIn());

            //Test create fail
            assertThrows(ResponseException.class, () -> {
                serverFacade.create(goodParams);});

            //Test list fail
            assertThrows(ResponseException.class, serverFacade::list);

            //Test login
            assertThrows(ResponseException.class, () -> {
                serverFacade.login(badParams);});
            serverFacade.login(goodParams);
            assertTrue(serverFacade.isSignedIn());

        } catch (ResponseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
