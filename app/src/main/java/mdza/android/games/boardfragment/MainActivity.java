package mdza.android.games.boardfragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements BoardFragment.CellClickListener {

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

        //testColor();
        testImage();
    }

    private void initalize() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        View containerBoard = (View) findViewById(R.id.containerBoard);
        containerBoard.setVisibility(View.VISIBLE);
        fragmentTransaction.replace(containerBoard.getId(), boardFragment);
        fragmentTransaction.commit();
    }

    private void testColor() {
        boardFragment.setCellColor(1, Color.BLUE);
        boardFragment.setCellColor(7, Color.BLUE);
        boardFragment.setCellColor(3, 2, Color.RED);
        boardFragment.setCellColor(0, 0, Color.RED);
    }

    private void testImage() {
        int resId = R.drawable.ic_action_name;
        boardFragment.setCellImage(1, resId);
        boardFragment.setCellImage(7, resId);
        boardFragment.setCellImage(3, 2, resId);
        boardFragment.setCellImage(0, 0, resId);

        boardFragment.disableCell(1);
    }

    @Override
    public void onClick(int row, int col) {
        int index = boardFragment.calculateIndex(row, col);

        Log.d("onClick", String.format("Index: %d\tROW: %d\tCOL: %d", index, row, col));

        if (boardFragment.isCellEnabled(index)) {
            boardFragment.disableCell(index);
            boardFragment.setCellColor(index, Color.GRAY);
        }
        else {
            boardFragment.enableCell(index);
            boardFragment.setCellColor(index, Color.GREEN);
        }

    }

    private BoardFragment boardFragment;
}
