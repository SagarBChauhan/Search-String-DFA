package com.example.searchstringusingdfa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_Search;
    LinearLayout layout_result;
    public static TextView tv_result;
    EditText etxt_main,etxt_pattern;
    public static final int NO_OF_CHARS = 256;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Search=findViewById(R.id.btn_Search);
        tv_result=findViewById(R.id.tv_result);
        etxt_main=findViewById(R.id.etxt_main_string);
        etxt_pattern=findViewById(R.id.etxt_pattern_string);

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String main = etxt_main.getText().toString();
                String pattern = etxt_pattern.getText().toString();
                search(pattern.toCharArray(), main.toCharArray(),getApplicationContext());
            }
        });

    }
    public static int getNextState(char[] pat, int M, int state, int x)
    {
        /*
         * If the character c is same as next character in pattern,
         * then simply increment state
         */
        if (state < M && x == pat[state])
            return state + 1;
        int ns, i;
        /*
         * ns stores the result which is next state
         * ns finally contains the longest prefix which is also suffix
         * in "pat[0..state-1]c"
         * Start from the largest possible value and stop when you find
         * a prefix which is also suffix
         */
        for (ns = state; ns > 0; ns--)
        {
            if (pat[ns - 1] == x)
            {
                for (i = 0; i < ns - 1; i++)
                {
                    if (pat[i] != pat[state - ns + 1 + i])
                        break;
                }
                if (i == ns - 1)
                    return ns;
            }
        }
        return 0;
    }

    /*
     * This function builds the TF table which represents Finite Automata for a
     * given pattern
     */
    public static void computeTF(char[] pat, int M, int[][] TF)
    {
        int state, x;
        for (state = 0; state <= M; ++state)
            for (x = 0; x < NO_OF_CHARS; ++x)
                TF[state][x] = getNextState(pat, M, state, x);
    }

    /*
     * Prints all occurrences of pat in txt
     */
    public static void search(char[] pat, char[] txt, Context context)
    {
        int M = pat.length;
        int N = txt.length;
        int[][] TF = new int[M + 1][NO_OF_CHARS];
        computeTF(pat, M, TF);
        // Process txt over FA.
        int i, state = 0;
        for (i = 0; i < N; i++)
        {
            state = TF[state][txt[i]];
            if (state == M)
            {
                tv_result.append(Character.toString(pat[1-1]));
                tv_result.append(" found at " + (i - M + 1));
                tv_result.append(String.format("\n"));
            }
            else
            {
                Toast.makeText(context, "Pattern not found..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
