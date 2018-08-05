package eai.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eai.entity.Contact;
import eai.repository.ContactRepository;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactServiceTest {
    private TransportClient client = null;
    public final String HOST = "127.0.0.1";
    public final int PORT = 9300;
    public ContactRepository contactRepository;

    @Before
    public void setUp() throws Exception {
        contactRepository = new ContactRepository();
    }


    @Test
    public void getUsers() throws Exception {
        Object result = contactRepository.getUsers(100,50,new QueryStringQueryBuilder(""));
        assertNotEquals(null,result);

    }

    @Test
    public void getAndGetWithName() throws Exception {
        Contact c = new Contact("test1","asd","asd");
        contactRepository.addUser(c);
        JsonElement res = contactRepository.getUser("test1");
        JsonObject j = res.getAsJsonObject();
        JsonElement resExpected = new JsonParser().parse("{'name':'test1','address':'asd','phone':'asd'}");
        assertEquals(resExpected, j);
    }

    @Test
    public void updateWithName() throws Exception {
        Contact c = new Contact("test1","123","123");
        contactRepository.updUser("test1",c);
        JsonElement res = contactRepository.getUser("test1");
        JsonObject j = res.getAsJsonObject();
        JsonElement resExpected = new JsonParser().parse("{'name':'test1','address':'123','phone':'123'}");
        assertEquals(resExpected, j);
    }

    @Test
    public void deleteWithName() throws Exception {
        Contact c = new Contact("test1","asd","asd");
        contactRepository.addUser(c);
        contactRepository.delUser("test1");
        JsonElement res = contactRepository.getUser("test1");
        JsonObject j = res.getAsJsonObject();
        JsonElement resExpected = new JsonParser().parse("{'Response':'fail'}");
        assertEquals(resExpected, j);
    }
}