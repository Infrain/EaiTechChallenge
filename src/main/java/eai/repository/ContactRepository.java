package eai.repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eai.entity.Contact;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactRepository {


    public final String HOST = "127.0.0.1";
    public final int PORT = 9300;
    public final String index = "address";
    public final String type = "user";

    private TransportClient client = null;

    public Object getUsers(int pageSize, int page, QueryStringQueryBuilder query)
            throws UnknownHostException {//page param is not implemented
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT));

        SearchResponse response;
        if (query.queryString()=="")//check if there is valid query
            response = client.prepareSearch(index)
                    .get();
        else
            response = client.prepareSearch(index)
                    .setQuery(query)
                    .get();

        SearchHits hits = response.getHits();

        int s = 0;
        for (SearchHit searchHit:hits) {
            s++;
            if (s>pageSize)
                break;
            Map<String, Object> map = searchHit.getSource();
            list.add(map);
        }
        return list;
    }

    /**
     * Get the user contact with a unique name is given
     * @param name
     * @return
     */
    public JsonElement getUser (String name)throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT));

        GetResponse response2 = client.prepareGet(index, type, name).get();
        JsonElement re;
        if (!response2.isExists()) {
            re = new JsonParser().parse("{'Response':fail }");
        }
        else {
            re = new JsonParser().parse(response2.getSourceAsString());
        }
        if (client != null)
            client.close();
        return re;

    }


    public JsonElement addUser(Contact contact) throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT));

        //build json object with contact
        IndexResponse response = client.prepareIndex(index, type, contact.getName())
                .setSource(XContentFactory.jsonBuilder()
                    .startObject().field("name", contact.getName())
                    .field("address", contact.getAddress())
                    .field("phone", contact.getPhone())
                    .endObject()).get();

        JsonElement re;
        if (response.getResult().equals(DocWriteResponse.Result.CREATED)){
            re = new JsonParser().parse("{'Response':'success'}");
        }
        else{
            re = new JsonParser().parse("{'Response':'fail'}");
        }
        return re;
    }

    public JsonElement updUser(String name, Contact contact) throws Exception {
        client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT));

        UpdateResponse response = client.prepareUpdate(index, type, name)
                .setDoc(XContentFactory.jsonBuilder()
                        .startObject().field("name", contact.getName())
                        .field("address", contact.getAddress())
                        .field("phone", contact.getPhone())
                        .endObject()).get();

        JsonElement re;
        if (response.getResult().equals(DocWriteResponse.Result.UPDATED)){
            re = new JsonParser().parse("{'Response':'success'}");
        }
        else{
            re = new JsonParser().parse("{'Response':'fail'}");
        }
        return re;

    }

    public JsonElement delUser(String name) throws Exception{
        client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName(HOST),PORT));

        DeleteResponse response = client.prepareDelete(index, type, name).get();

        JsonElement re;
        if (response.getResult().equals(DocWriteResponse.Result.DELETED)){
            re = new JsonParser().parse("{'Response':'success'}");
        }
        else{
            re = new JsonParser().parse("{'Response':'fail'}");
        }
        return re;
    }



}
