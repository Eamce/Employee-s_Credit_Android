package ecandroid.ebs.ec;

public class BackupEmployees {
    private String uploadeddate;
    private String totalamount;
    private String totalemp;


    public BackupEmployees(String uploadeddate, String totalamount, String total_emp) {
        this.uploadeddate = uploadeddate;
        this.totalamount = totalamount;
        this.totalemp = total_emp;
    }


    public String getUploadeddate() {
        return uploadeddate;
    }

    public void setUploadeddate(String uploadeddate) {
        this.uploadeddate = uploadeddate;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getTotalemp() {
        return totalemp;
    }

    public void setTotalemp(String totalemp) {
        this.totalemp = totalemp;
    }
}
