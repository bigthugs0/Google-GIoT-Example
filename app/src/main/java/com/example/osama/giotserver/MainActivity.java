package com.example.osama.giotserver;


import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG= " TurnLED Activity";

    private Gpio mLedGpio;
    PeripheralManagerService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize ServiceManager
            service = new PeripheralManagerService();

        //Start Socket Listening Server
        Server server= new Server(this);

        //Show Current Raspberry PI WIFI IP on Activity Screen
        ((TextView)findViewById(R.id.ipaddress)).setText(GetDeviceipWiFi());
        ((TextView)findViewById(R.id.portnotext)).setText(String.valueOf(Server.socketServerPORT));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the resource.
        if (mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }

    //Convert received On/Off String to Boolean
    private boolean StringtoBoolean(String isChecked){
        if(isChecked.equals("on")){
            return true;
        }
        return false;
    }


    //LED Switch Method
    public void SwitchLed(String GPIO_PIN_NAME,String isChecked){
            try {
                //Open Connection to given GPIO PIN
                mLedGpio = service.openGpio(GPIO_PIN_NAME);

                // Configure as an output.
                mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        if (mLedGpio != null) {


            try {
                //Set Value of GPIO PIN to On or Off
                mLedGpio.setValue(StringtoBoolean(isChecked));
                //Close the GPIO PIN
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }

        }
    }




    // Get Current Raspberry Pi WIFI IP
    public String GetDeviceipWiFi(){

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }
}