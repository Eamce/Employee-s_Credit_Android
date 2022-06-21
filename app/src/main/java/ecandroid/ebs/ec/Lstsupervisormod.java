package ecandroid.ebs.ec;

public class Lstsupervisormod {

    private String emp_id;
    private String txtemployeename;
    private String txtdepartment;
    private String txtEOC;
    private String txtamount;
    private String txtbalance;
    private String txtstatus;
    private boolean selected = false;

    public Lstsupervisormod(String emp_id, String txtemployeename, String txtdepartment, String txtEOC,String txtamount, String txtbalance, String txtstatus) {
        this.emp_id = emp_id;
        this.txtemployeename = txtemployeename;
        this.txtdepartment = txtdepartment;
        this.txtEOC = txtEOC;
        this.txtamount = txtamount;
        this.txtbalance = txtbalance;
        this.txtstatus = txtstatus;
    }

    public String getEmp_id() { return emp_id; }
    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getTxtemployeename() { return txtemployeename; }
    public void setTxtemployeename(String txtemployeename) { this.txtemployeename = txtemployeename; }

    public String getTxtdepartment() { return txtdepartment; }
    public void setTxtdepartment(String txtdepartment) {
        this.txtdepartment = txtdepartment;
    }

    public String getTxtEOC() { return txtEOC; }
    public void setTxtEOC(String txtEOC) {
        this.txtEOC = txtEOC;
    }

    public String getTxtamount() { return txtamount; }
    public void setTxtamount(String txtamount) {
        this.txtamount = txtamount;
    }

    public String getTxtbalance() { return txtbalance; }
    public void setTxtbalance(String txtbalance) {
        this.txtbalance = txtbalance;
    }

    public String getTxtstatus() { return txtstatus; }
    public void setTxtstatus(String txtstatus) {
        this.txtstatus = txtstatus;
    }

    public boolean isSelected() { return selected; }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
