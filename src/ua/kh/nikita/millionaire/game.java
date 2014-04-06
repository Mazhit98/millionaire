package ua.kh.nikita.millionaire;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class game extends Activity {

    public static int[] q_num_to_money = new int[]{
            100, 200, 300, 500, 1000,
            2000, 4000, 8000, 16000, 32000,
            64000, 125000, 250000, 500000, 1000000
    };
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
    protected TextView q;       //current question  TextView
    protected short question_number = 0; // 1..15
    protected String[] questions15;
    protected short s_time_left;
    private String right_answer;
    private Random random;
    private Thread my_timer;
    private boolean timer_stopped = false;


    public static void init_views(Activity this_o, String[] strings) {

        Class<?> c = this_o.getClass();
        int drawableResourceId;
        for (String string : strings) {
            Field f;
            try {
                f = c.getDeclaredField(string);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return;
            }
            f.setAccessible(true);
            drawableResourceId = this_o.getResources().getIdentifier(string, "id", this_o.getPackageName());
            try {
                f.set(this_o, this_o.findViewById(drawableResourceId));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            f.setAccessible(false);
        }
    }

    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v != null && v.equals(e))
                return true;

        return false;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game);

        random = new Random();
        my_timer = new Thread(new timer60());


        init_views(this, new String[]{
                "call", "fifty", "zal", "get_money",
                "var1", "var2", "var3", "var4",
                "money", "time_left", "q"
        });

        load_questions();
        next_question();

        get_money.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // take money and exit
                game_over(false);
            }
        });

        View.OnClickListener answering = new View.OnClickListener() {
            public void onClick(View v) {

                timer_stopped = true;
                try {

                    my_timer.join();
                    my_timer.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Button b = (Button) v;
                if (b.getText().equals(right_answer)) {
                    animate_button(b, R.drawable.but1, new Runnable() {
                        @Override
                        public void run() {
                            next_question();
                        }
                    });
                } else {
                    // wrong answer

                    Runnable todo = new Runnable() {
                        @Override
                        public void run() {
                            game_over(false);
                        }
                    };
                    animate_button(b, R.drawable.but2, todo);

                    // searching right answer
                    if (var1.getText().equals(right_answer)) {
                        animate_button(var1, R.drawable.but1, todo);
                    } else if (var2.getText().equals(right_answer)) {
                        animate_button(var2, R.drawable.but1, todo);
                    } else if (var3.getText().equals(right_answer)) {
                        animate_button(var3, R.drawable.but1, todo);
                    } else if (var4.getText().equals(right_answer)) {
                        animate_button(var4, R.drawable.but1, todo);
                    }

                }
            }
        };

        var1.setOnClickListener(answering);
        var2.setOnClickListener(answering);
        var3.setOnClickListener(answering);
        var4.setOnClickListener(answering);

        fifty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fifty.setOnClickListener(null);
                fifty.setBackgroundResource(R.drawable.podsoff);
                fifty_fifty();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                call.setOnClickListener(null);
                call.setBackgroundResource(R.drawable.zvonokoff);
                timer_set((short) (80 + s_time_left));

                showContacts();

            }
        });

        zal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                zal.setOnClickListener(null);
                zal.setBackgroundResource(R.drawable.zaloff);
                hall();
            }
        });

    }

    private void hall() {
        short[] places = new short[]{0, 0, 0, 0};
        short _right = 0;
        short k;
        if (var1.getText().equals(right_answer)) {
            _right = 1;
        } else if (var2.getText().equals(right_answer)) {
            _right = 2;
        } else if (var3.getText().equals(right_answer)) {
            _right = 3;
        } else if (var4.getText().equals(right_answer)) {
            _right = 4;
        }
        if (question_number <= 10 || randomInteger(0, 1) == 1) {
            places[0] = _right;
            while (places[1] == 0) {
                k = (short) randomInteger(1, 4);
                if (k != _right) {
                    places[1] = k;
                }
            }
        } else {
            while (places[0] == 0) {
                k = (short) randomInteger(1, 4);
                if (k != _right) {
                    places[0] = k;
                }
            }
            places[1] = _right;
        }
        for (int z = 2; z <= 3; z++) {
            while (places[z] == 0) {
                k = (short) randomInteger(1, 4);
                for (int i2 : places) {
                    if (i2 == k) {
                        k = 0;
                    }
                }
                if (k != 0) {
                    places[z] = k;
                }
            }
        }
        Button[] variants = new Button[]{var1, var2, var3, var4};
        String toast = "ВНИМАНИЕ! Зал принял решение:\r\n";
        int pl = 1;
        for (int i2 : places) {
            toast += (new Integer(pl++)).toString() + ". " + variants[i2 - 1].getText() + "\r\n";
        }
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();

    }

    private void showContacts() {
        Intent i = new Intent();
        i.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity"));
        i.setAction("android.intent.action.MAIN");
        i.addCategory("android.intent.category.LAUNCHER");
        i.addCategory("android.intent.category.DEFAULT");
        startActivity(i);
    }

    private void fifty_fifty() {
        int i1 = 5, i2 = 5, k1;
        while (i1 == 5 || i2 == 5) {
            if (i1 == 5) {
                k1 = randomInteger(0, 3);
                if (k1 == 0 && !var1.getText().equals(right_answer)) {
                    i1 = 0;
                    var1.setBackgroundResource(R.drawable.but2);
                } else if (k1 == 1 && !var2.getText().equals(right_answer)) {
                    i1 = 1;
                    var2.setBackgroundResource(R.drawable.but2);
                } else if (k1 == 2 && !var3.getText().equals(right_answer)) {
                    i1 = 2;
                    var3.setBackgroundResource(R.drawable.but2);
                } else if (k1 == 3 && !var4.getText().equals(right_answer)) {
                    i1 = 3;
                    var4.setBackgroundResource(R.drawable.but2);
                }
            }
            k1 = randomInteger(0, 3);
            if (i2 == 5 && k1 != i1) {
                if (k1 == 0 && !var1.getText().equals(right_answer)) {
                    i2 = 0;
                    var1.setBackgroundResource(R.drawable.but2);
                } else if (k1 == 1 && !var2.getText().equals(right_answer)) {
                    i2 = 1;
                    var2.setBackgroundResource(R.drawable.but2);
                } else if (k1 == 2 && !var3.getText().equals(right_answer)) {
                    i2 = 2;
                    var3.setBackgroundResource(R.drawable.but2);
                } else if (k1 == 3 && !var4.getText().equals(right_answer)) {
                    i2 = 3;
                    var4.setBackgroundResource(R.drawable.but2);
                }
            }
        }
    }

    private void animate_button(Button b, int resource_id, Runnable next_to_do) {
        animate_button_process r = new animate_button_process(b, resource_id, next_to_do);
        if (resource_id == R.drawable.but1) {
            r.setInterval(200);
        }
        new Thread(r).start();
    }

    private synchronized void timer_set(short s){
        s_time_left = s;
        time_left.setText(Short.toString(s));
    }

    protected void next_question() {
        timer_set((short) 61);
        timer_stopped = false;
        my_timer = new Thread(new timer60());
        my_timer.start();

        int i = question_number;
        question_number++;
        if (question_number == 15) {
            // game over
            game_over(true);
            return;
        }

        String[] parts = questions15[i].split("#@");
        money.setText((new Integer(q_num_to_money[i])).toString() + " $");
        q.setText(parts[0]);
        right_answer = parts[1];
        ArrayList<String> answers = new ArrayList<String>();
        answers.add(parts[1]);
        answers.add(parts[2]);
        answers.add(parts[3]);
        answers.add(parts[4]);
        Collections.shuffle(answers);
        var1.setText(answers.get(0));
        var2.setText(answers.get(1));
        var3.setText(answers.get(2));
        var4.setText(answers.get(3));

        var1.setBackgroundResource(R.drawable.but3);
        var2.setBackgroundResource(R.drawable.but3);
        var3.setBackgroundResource(R.drawable.but3);
        var4.setBackgroundResource(R.drawable.but3);

    }

    /**
     * Loads 15 questions for 1 game
     */
    private void load_questions() {
        questions15 = new String[15];
        int i = 0, q1_len;

        short k, k2;

        for (String[] q_array : questions.questions) {

            q1_len = q_array.length;
            Short[] used_indexes = {-1, -1, -1};
            k2 = 0;
            while (k2 < 3) {
                k = (short) this.randomInteger(0, q1_len - 1);
                if (!contains(used_indexes, k)) {
                    used_indexes[k2] = k;
                    questions15[i] = q_array[k];
                    i++;
                    k2++;
                }
            }
        }
    }

    private void game_over(boolean winner) {

        String money = "0";
        timer_stopped = true;
        try {
            my_timer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (winner) {
            money = (new Integer(q_num_to_money[question_number - 1])).toString();
        } else if (question_number >= 10) {
            money = (new Integer(q_num_to_money[9])).toString();
        } else if (question_number >= 5) {
            money = (new Integer(q_num_to_money[4])).toString();
        }
        Intent intent = new Intent(this, over.class);
        intent.putExtra("money", money);
        startActivity(intent);

        finish();
        System.exit(0);
    }

    private int randomInteger(int aStart, int aEnd) {

        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * random.nextDouble());

        return (int) (fraction + aStart);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class animate_button_process implements Runnable {

        protected long interval = 500;
        protected Button b = null;
        protected int resource_id = 0;
        protected Runnable next_to_do;

        public animate_button_process(Button b, int resource_id, Runnable next_to_do) {
            this.b = b;
            this.resource_id = resource_id;
            this.next_to_do = next_to_do;
        }

        public void setInterval(long interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (resource_id != 0) {
                for (int i = 0; i < 5; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            b.setBackgroundResource(resource_id);
                        }
                    });
                    sleep(interval);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            b.setBackgroundResource(R.drawable.but3);
                        }
                    });
                    sleep(interval);
                }
                runOnUiThread(next_to_do);
            }

        }
    }

    private class timer60 implements Runnable {

        public timer60() {
        }

        @Override
        public void run() {

            while (true) {
                if (Thread.currentThread().isInterrupted() || timer_stopped){

                    return;
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public synchronized void run() {
                        if (Thread.currentThread().isInterrupted() || timer_stopped){
                            return;
                        }

                        s_time_left -= 1;
                        if (s_time_left > 0) {
                            time_left.setText(Integer.toString(s_time_left));
                        } else {
                            game_over(false);
                            //Thread.currentThread().interrupt();
                        }

                    }
                });

                sleep(1000);

            }

        }
    }
}