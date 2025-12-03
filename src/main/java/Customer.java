abstract class Customer {


//    Static fields;
    private static int customerCounter = 0;

//    private fields
    private final String customerId;
    private String name;
    private int age;
    private String contact;
    private String address;

    public Customer(String name, int age, String contact, String address){
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.customerId = String.format("CUS%03d", ++customerCounter);
    }

//    SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAddress(String address){
        this.address = address;
    }


//    GETTERS
    public String getName(){
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String getContact(){
        return this.contact;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCustomerId(){
        return this.customerId;
    }

//    ABSTRACT METHODS
    abstract public void displayCustomerDetails();
    abstract public String getCustomerType();

}
