package eai.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import eai.entity.Contact;
import eai.service.LogicService;
import eai.service.UserService;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

import static spark.Spark.*;

public class Handlers {
    private LogicService logicService;

    public Handlers(LogicService logicService){
        this.logicService = new LogicService(new UserService());
        setupEndPoints();
    }

    public void setupEndPoints(){

        post("/contact", (request, response) -> { //insert contact, request body should be in Json format (easier for testing through http)
            Contact contact = new Gson().fromJson(
                    new JsonParser().parse(request.body()), Contact.class);
            return(logicService.insertWithName(contact));
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
            return(logicService.getUsers(pageSize,page,queryBuilder));
        });


        get("/contact/:name", (request, response) -> { //find contact with given name
            return(logicService.getWithName(request.params(":name")));

        });


        put("/contact/:name", (request, response) -> { //update contact, request body should be in Json format (easier for testing through http)
            Contact contact = new Gson().fromJson(
                    new JsonParser().parse(request.body()), Contact.class);
            return(logicService.updateWithName(request.params(":name"),contact));

        });


        delete("/contact/:name", (request, response) -> {
            return(logicService.deleteWithName(request.params(":name")));
        });
    }


}
