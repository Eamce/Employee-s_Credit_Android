package ecandroid.ebs.ec;

public class Downloadeditems {
     private String itemid;
    private String itemname;
    private String itemsubname;
    private String itemqty;
    private String imgURL;

    public Downloadeditems(String itemid, String itemname, String itemsubname, String itemqty, String imgURL) {
        this.itemid = itemid;
        this.itemname = itemname;
        this.itemsubname = itemsubname;
        this.itemqty = itemqty;
        this.imgURL = imgURL;
    }


    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemsubname() {
        return itemsubname;
    }

    public void setItemsubname(String itemsubname) {
        this.itemsubname = itemsubname;
    }

    public String getItemqty() {
        return itemqty;
    }

    public void setItemqty(String itemqty) {
        this.itemqty = itemqty;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
