package eai.entity;

import com.google.gson.JsonObject;

public class Contact {
    private String name;
    private String address;
    private String phone;

    public Contact(JsonObject j){
        this.name = j.get("name").toString();
        this.address = j.get("address").toString();
        this.phone = j.get("phone").toString();
    }

    public Contact(String name, String address, String phone){
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString(){
        return "name: "+ name + "\n" + "address: "+ address + "\n" +"phone: "+ phone + "\n";
    }

}
