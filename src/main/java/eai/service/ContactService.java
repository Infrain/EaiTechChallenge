package eai.service;

import com.google.gson.*;
import eai.entity.Contact;
import eai.entity.StandardResponse;
import eai.entity.StatusResponse;
import eai.repository.ContactRepository;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

public class ContactService {

    private ContactRepository contactRepository;

    public ContactService(){

    }

    public ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }


    /**
     * return all the records under "address" and "user"
     * @param pageSize
     * @param page
     * @param query
     * @return
     * @throws Exception
     */
    public Object getUsers(int pageSize, int page, QueryStringQueryBuilder query) throws Exception {
        Object res = contactRepository.getUsers(pageSize,page,query);
        JsonArray j = new JsonParser().parse(res.toString()).getAsJsonArray();
        return j;
    }

    /**
     * Get the contact with a given name
     * @param name
     * @return Contact if found, ERROR Status code with "name not found" if not found.
     * @throws Exception
     */
    public Object getWithName(String name) throws Exception {
        JsonElement res = contactRepository.getUser(name);
        JsonObject j = res.getAsJsonObject();
        if (j.get("name")==null) {
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"Name not found"));
        }
        return new Contact(j);
    }

    /**
     * insert a contact with a unique name
     * @param contact
     * @return
     * @throws Exception
     */
    public Object insertWithName(Contact contact) throws Exception {
        if (contact.getPhone().length()>20)
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"Too many digits in phone number"));

        if (contact.getAddress().length()>50)
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"The address is too long"));

        JsonElement res = contactRepository.getUser(contact.getName());
        JsonObject j = res.getAsJsonObject();
        if (j.get("name")!=null) {
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"Name already exists, use put if needed"));
        }

        res = contactRepository.addUser(contact);
        j = res.getAsJsonObject();

        if (j.get("Response").getAsString().equals("success")){
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS));
        }
        return new Gson()
                .toJson(new StandardResponse(StatusResponse.ERROR));
    }


    /**
     * update the contact with the given name and contact
     * @param name
     * @param contact
     * @return
     * @throws Exception
     */
    public Object updateWithName(String name, Contact contact) throws Exception {
        if (contact.getPhone().length()>20)
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"Too many digits in phone number"));

        if (contact.getAddress().length()>50)
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"The address is too long"));

        JsonElement res = contactRepository.getUser(name);
        JsonObject j = res.getAsJsonObject();
        if (j.get("name")==null) {
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"Name not found"));
        }
        res = contactRepository.updUser(name, contact);
        j = res.getAsJsonObject();
        if (j.get("Response").getAsString().equals("success")){
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS));
        }
        return new Gson()
                .toJson(new StandardResponse(StatusResponse.ERROR,"Identical to the original contact"));

    }


    /**
     * delete contact with given name
     * @param name
     * @return
     * @throws Exception
     */
    public Object deleteWithName(String name) throws Exception {
        JsonElement res = contactRepository.getUser(name);
        JsonObject j = res.getAsJsonObject();
        if (j.get("name")==null) {
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.ERROR,"Name not found"));
        }
        res = contactRepository.delUser(name);
        j = res.getAsJsonObject();
        if (j.get("Response").getAsString().equals("success")){
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS));
        }
        return new Gson()
                .toJson(new StandardResponse(StatusResponse.ERROR));
    }

    public ContactRepository getContactRepository(){
        return contactRepository;
    }
}
