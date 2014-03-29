package ua.kh.nikita.millionaire;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;


public class game extends Activity {

    protected Button call;
    protected Button fifty;
    protected Button zal;
    protected Button get_money;
    protected Button var1;
    protected Button var2;
    protected Button var3;
    protected Button var4;
    protected TextView money;  // ensured money
    protected TextView time_left;
    protected TextView q;       //current question

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);

        init_buttons(new String[]{"call", "fifty", "zal", "get_money", "var1", "var2", "var3", "var4"});

        get_money.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // exit
                finish();
                System.exit(0);
            }
        });
    }

    private void init_buttons(String[] strings) {

        Class<?> c = this.getClass();
        int drawableResourceId;
        for (String string : strings) {
            Field f = null;
            try {
                f = c.getDeclaredField(string);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return;
            }
            f.setAccessible(true);
            drawableResourceId = this.getResources().getIdentifier(string, "id", this.getPackageName());
            try {
                f.set(this, (Button) findViewById(drawableResourceId));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            f.setAccessible(false);
        }
    }
}