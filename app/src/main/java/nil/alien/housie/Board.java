package nil.alien.housie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class Board extends ActionBarActivity {

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        try {
            socket = IO.socket("http://192.168.1.3");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    Log.d("socket","emitList");

//                    socket.disconnect();
                }

            }).on("nextNumber", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.d("socket","new Number");
                    System.out.println(args);
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {}

            });
            socket.connect();
            socket.emit("getList");

            Log.d("socket","connecting");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

}
