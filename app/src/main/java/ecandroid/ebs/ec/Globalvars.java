package ecandroid.ebs.ec;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
public class Globalvars
{
    String[] manager_emp_id = {"03502-2015","03941-2019","45539-2013","06157-2015","37401-2013","46333-2013","02768-2016","42318-2013","05781-2017","03941-2014","53967-2013","35177-2013"};
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public Globalvars(Context context,Activity act){
        preferences = act.getSharedPreferences("Global_vars",context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void set(String varname, String varval)
    {
        editor.putString(varname,varval);
        editor.commit();
    }
    public String get(String varname)
    {
        return preferences.getString(varname,null);
    }
}
