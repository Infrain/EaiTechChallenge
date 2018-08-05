package eai.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import eai.entity.Contact;
import eai.service.ContactService;
import eai.repository.ContactRepository;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

import static spark.Spark.*;

public class Handlers {
    private ContactService contactService;

    public Handlers(ContactService contactService){
        this.contactService = new ContactService(new ContactRepository());
        setupEndPoints();
    }

    public void setupEndPoints(){

        post("/contact", (request, response) -> { //insert contact, request body should be in Json format (easier for testing through http)
            Contact contact = new Gson().fromJson(
                    new JsonParser().parse(request.body()), Contact.class);
            return(contactService.insertWithName(contact));
        });


        get("/contact", (request, response) -> {
            int pageSize = 100;
            int page = 50;
            String query ="";
            if (request.queryParams("pageSize")!=null)
                pageSize = Integer.parseInt(request.queryParams("pageSize"));
            if (request.queryParams("page")!=null)
                page = Integer.parseInt(request.queryParams("page"));
            if (request.queryParams("query")!=null)
                query = request.queryParams("query");

            QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(query);
            return(contactService.getUsers(pageSize,page,queryBuilder));
        });


        get("/contact/:name", (request, response) -> { //find contact with given name
            return(contactService.getWithName(request.params(":name")));

        });


        put("/contact/:name", (request, response) -> { //update contact, request body should be in Json format (easier for testing through http)
            Contact contact = new Gson().fromJson(
                    new JsonParser().parse(request.body()), Contact.class);
            return(contactService.updateWithName(request.params(":name"),contact));

        });


        delete("/contact/:name", (request, response) -> {
            return(contactService.deleteWithName(request.params(":name")));
        });
    }


}
