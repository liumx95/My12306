package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by star on 2016/9/22.
 */
public class NetUtils {
    public static  boolean check(Context context){
        ConnectivityManager connectivity= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity!=null) {
            //获取网络连接管理的对象
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                //判断当前网络是否连接
                if (info.getState() == NetworkInfo.State.CONNECTED) ;
                return true;
            }
        }
        return  false;
    }
}
