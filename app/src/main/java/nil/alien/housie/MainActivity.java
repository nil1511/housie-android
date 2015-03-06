package nil.alien.housie;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import io.cine.primus.Primus;


public class MainActivity extends Activity {
    private Primus primus;
    private final String SERVER_IP = "192.168.1.5:3000";
    private final String PRIMUS_URL = "http://"+SERVER_IP+"/primus";
    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        primus = Primus.connect(this, PRIMUS_URL);
        Primus.PrimusOpenCallback openCallback = new Primus.PrimusOpenCallback() {
            @Override
            public void onOpen() {
                // Websocket open
            }
        };
        Primus.PrimusDataCallback dataCallback = new Primus.PrimusDataCallback() {
            @Override
            public void onData(JSONObject data) {
                //got data
                System.out.print(data);
            }
        };
//        primus.setOpenCallback(openCallback);
//        primus.setDataCallback(dataCallback);
//        try {
//            JSONObject j = new JSONObject();
//            j.put("some", "data");
//            primus.send(j);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        super.onCreate(savedInstanceState);
//        mGLView = new MyGLSurfaceView(this);
//        setContentView(mGLView);
        setContentView(R.layout.activity_main);
    }
}
