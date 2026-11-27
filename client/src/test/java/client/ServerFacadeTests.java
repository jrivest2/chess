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
    private static final String SERVER_URL = "http://localhost:0";
    private static final ServerFacade SERVER_FACADE = new ServerFacade(SERVER_URL);

    @BeforeAll
    public static void init() {
        server = new Server();
        try {
            userService = new UserService();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var port = server.run(0);
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
            result = SERVER_FACADE.register(goodParams);
            Assertions.assertEquals("testUser", result.username());
            Assertions.assertNotEquals("", result.authToken());
            assertTrue(SERVER_FACADE.isSignedIn());
            Assertions.assertThrows(ResponseException.class,
                    () -> {
                        SERVER_FACADE.register(goodParams);});
            Assertions.assertThrows(ResponseException.class,
                    () -> {
                        SERVER_FACADE.register(badParams);});

            //Test create successful
            SERVER_FACADE.create(goodParams);
            //Test list successful
            assertDoesNotThrow(() -> {
                SERVER_FACADE.list();});

            //Test join/play
            var response = SERVER_FACADE.list();
            int id = response.games().getFirst().gameID();
            assertDoesNotThrow(() -> {
                SERVER_FACADE.join("white",id);});
            assertDoesNotThrow(() -> {
                SERVER_FACADE.join("black",id);});
            assertThrows(ResponseException.class, () -> {
                SERVER_FACADE.join("white",1);});
            assertThrows(ResponseException.class, () -> {
                SERVER_FACADE.join("black",1);});

            //Test logout
            SERVER_FACADE.logout();
            Assertions.assertFalse(SERVER_FACADE.isSignedIn());

            //Test create fail
            assertThrows(ResponseException.class, () -> {
                SERVER_FACADE.create(goodParams);});

            //Test list fail
            assertThrows(ResponseException.class, SERVER_FACADE::list);

            //Test login
            assertThrows(ResponseException.class, () -> {
                SERVER_FACADE.login(badParams);});
            SERVER_FACADE.login(goodParams);
            assertTrue(SERVER_FACADE.isSignedIn());

            //Trigger the catch statement
            SERVER_FACADE.register(badParams);
        } catch (ResponseException e) {
            assertTrue(e.getMessage().contains("403")
                    || e.getMessage().contains("400")
                    || e.getMessage().contains("401")
                    || e.getMessage().contains("500"));
        }
    }

}
