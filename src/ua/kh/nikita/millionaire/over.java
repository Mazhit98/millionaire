package ua.kh.nikita.millionaire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class over extends Activity {

    protected Button new_game;
    protected Button exit;
    protected TextView message;
    protected TextView sum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        game.init_views(this, new String[]{"new_game", "exit", "message", "sum"});

        String money = getIntent().getStringExtra("money");
        sum.setText(money);
        if (money.equals("0")) {
            message.setText(R.string.yor_lose);
        } else if (money.equals("1000000")) {
            message.setText(R.string.yor_millionaire);
        } else {
            message.setText(R.string.your_money);
        }

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                over.this.finish();
                System.exit(0);
            }
        });

        new_game.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(over.this, game.class);
                startActivity(intent);
                over.this.finish();
                System.exit(0);
            }
        });
    }
}