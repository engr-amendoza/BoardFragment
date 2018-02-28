package mdza.android.games.boardfragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boardFragment = new BoardFragment(4, 4, this);
        initalize();

    }

    @Override
    protected void onStart() {
        super.onStart();

        test();
    }

    private void initalize() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        View containerBoard = (View) findViewById(R.id.containerBoard);
        containerBoard.setVisibility(View.VISIBLE);
        fragmentTransaction.replace(containerBoard.getId(), boardFragment);
        fragmentTransaction.commit();
    }

    private void test() {
        boardFragment.setButtonColor(1, Color.BLUE);
        boardFragment.setButtonColor(7, Color.BLUE);
        boardFragment.setButtonColor(3, 2, Color.RED);
        boardFragment.setButtonColor(0, 0, Color.RED);
    }


    @Override
    public void onClick(View v) {
        ImageButton button = (ImageButton) v;
        int index = button.getId();
        int row = BoardFragment.indexToRow(index);
        int col = BoardFragment.indexToCol(index);

        Log.d("onClick", String.format("Index: %d", index));
        Log.d("onClick", String.format("ROW: %d\tCOL: %d", row, col));
    }

    private BoardFragment boardFragment;
}
