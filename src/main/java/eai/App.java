package eai;

import eai.controller.Handlers;
import eai.service.ContactService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {//modify HOST and PORT in ContactRepository.java
        Handlers handlers = new Handlers(new ContactService());
    }


}
