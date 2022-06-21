package ecandroid.ebs.ec;

public class Businessunit {
    String name;
    boolean checkbox;

    public Businessunit() {
        /*Empty Constructor*/
    }
    public  Businessunit(String bu, boolean status){
        this.name = bu;
        this.checkbox = status;
    }
    //Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

}
