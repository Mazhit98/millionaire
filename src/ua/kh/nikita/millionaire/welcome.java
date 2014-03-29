package ua.kh.nikita.millionaire;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // start new game
            }
        });

        final Button button1 = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // exit
                finish();
                System.exit(0);
            }
        });
    }
}
