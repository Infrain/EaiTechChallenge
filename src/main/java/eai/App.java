package eai;

import eai.controller.Handlers;
import eai.service.LogicService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {//modify HOST and PORT in UserService.java
        Handlers handlers = new Handlers(new LogicService());
    }


}
