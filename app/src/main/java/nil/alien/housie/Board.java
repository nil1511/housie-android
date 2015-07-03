package nil.alien.housie;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;

public class Board extends Activity implements View.OnClickListener{

    private Socket socket;
    private TableLayout myCard;
    TextView[] number = new TextView[25];
    private Timer getList;
    private TextView newNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        newNumber = (TextView) findViewById(R.id.newNumber);
        myCard =  (TableLayout) findViewById(R.id.myCard);
        try {
            socket = IO.socket("http://192.168.1.3:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if(args.length > 0){
                        JSONObject obj = (JSONObject) args[0];
                        System.out.println(obj);
                    }else{
                        System.out.println("no object");
                    }

                    System.out.println(args);
                    Log.d("socket", "emitList");
                }
            }).on("nextNumber", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if(args.length > 0){
                        final JSONArray number = (JSONArray) args[0];
                        if(number.length() > 0){
                            try {
                                updateView(newNumber,number.get(0).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).on("list", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (args.length > 0) {
                        JSONArray jsonArray = (JSONArray) args[0];
                        if (jsonArray != null) {
                            int len = jsonArray.length();
                            for (int i = 0; i < len; i++) {
                                try {
                                    updateView(number[i],jsonArray.get(i).toString());
                                    System.out.println(jsonArray.get(i).toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        Log.d("list", "no Number");
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            socket.connect();

            Log.d("socket", "connecting");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        createBoard();

    }
    private void updateView(final TextView tv, final String number){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(number);
            }
        });
    }

    private void createBoard() {
        TableRow row;
        int _10dp = getDpSize(10);

        for (int i=0,k=0;i<5;i++){
            row = new TableRow(Board.this);
            int _60dp = getDpSize(60);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, _60dp));
            row.setGravity(Gravity.CENTER);
            myCard.addView(row);
            for(int j=0;j<5;j++,k++){
                number[k] = new TextView(this);
                number[k].setText(k+"");
                number[k].setGravity(Gravity.CENTER);
                number[k].setTextSize(_10dp);
                number[k].setBackgroundResource(R.drawable.border);
                number[k].setLayoutParams(new TableRow.LayoutParams(_60dp, _60dp));
                number[k].setOnClickListener(this);
                row.addView(number[k]);
            }
        }
        socket.emit("getList");
    }

    public int getDpSize(int size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View v) {
        System.out.println(v);
    }
}
