package eai.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eai.entity.Contact;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class LogicServiceTest {
    private TransportClient client = null;
    public final String HOST = "127.0.0.1";
    public final int PORT = 9300;
    public UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserService();
    }


    @Test
    public void getUsers() throws Exception {
        Object result = userService.getUsers(100,50,new QueryStringQueryBuilder(""));
        assertNotEquals(null,result);

    }

    @Test
    public void getAndGetWithName() throws Exception {
        Contact c = new Contact("test1","asd","asd");
        userService.addUser(c);
        JsonElement res = userService.getUser("test1");
        JsonObject j = res.getAsJsonObject();
        JsonElement resExpected = new JsonParser().parse("{'name':'test1','address':'asd','phone':'asd'}");
        assertEquals(resExpected, j);
    }

    @Test
    public void updateWithName() throws Exception {
        Contact c = new Contact("test1","123","123");
        userService.updUser("test1",c);
        JsonElement res = userService.getUser("test1");
        JsonObject j = res.getAsJsonObject();
        JsonElement resExpected = new JsonParser().parse("{'name':'test1','address':'123','phone':'123'}");
        assertEquals(resExpected, j);
    }

    @Test
    public void deleteWithName() throws Exception {
        Contact c = new Contact("test1","asd","asd");
        userService.addUser(c);
        userService.delUser("test1");
        JsonElement res = userService.getUser("test1");
        JsonObject j = res.getAsJsonObject();
        JsonElement resExpected = new JsonParser().parse("{'Response':'fail'}");
        assertEquals(resExpected, j);
    }
}