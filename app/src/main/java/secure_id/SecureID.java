package secure_id;

import android.provider.Settings;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by c3rv30 on 7/5/17.
 */

public class SecureID extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String generateID(){

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        String deviceId = android.provider.Settings.Secure.getString(this.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
            /*if ("2cfdf87def210e87".equals(deviceId) || deviceId == null){
                deviceId = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (deviceId == null){
                    Random tmpRand = new Random();
                    deviceId = String.valueOf(tmpRand.nextLong());
                }
            }
        return getHash(deviceId);*/
        return id;
    }

    private String getHash(String stringToHash){
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        byte[] result = null;
        try{
            result = digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        for (byte b : result){
            sb.append(String.format("%02X", b));
        }

        String messageDigest = sb.toString();
        return messageDigest;
    }

}
