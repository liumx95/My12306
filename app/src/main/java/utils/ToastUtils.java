package utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by star on 2016/9/22.
 */
public class ToastUtils {
    public static  void maktText(Context ctx,String text){
        Toast.makeText(ctx,text,Toast.LENGTH_SHORT).show();
    }
}
