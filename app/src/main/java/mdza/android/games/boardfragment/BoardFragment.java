/*
 * Developed by Alberto Mendoza
 * 02/27/2018
 *
 * Reusable fragment that creates a matrix with R rows and C cols.
 * The cells on the grid can be set to a specific color or image,
 * typically serving as a game board.
 * An onclick callback can be defined for all cells or on a per
 * cell basis.
 */

package mdza.android.games.boardfragment;

import android.app.Fragment;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class BoardFragment extends Fragment {

    public BoardFragment() {}

    public BoardFragment(int rows, int cols, View.OnClickListener onClickListener) {
        this.rows = rows;
        this.cols = cols;
        this.onClickListener = onClickListener;
        tableRow = new TableRow[rows];
        buttons = new ImageButton[rows * cols];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_fragment, container, false);
        tableLayout = (TableLayout) view.findViewById(R.id.boardFragment_TableLayout);
        tableLayout.setWeightSum((float) rows);
        tableLayout.setShrinkAllColumns(true);

        //initButtons();
        createRows(view);

        return view;
    }

    private void initButtons() {
        int index = 0;
        for (int row=0; row<rows; ++row) {
            for (int col=0; col<cols; ++col) {
                buttons[index] = new ImageButton(getActivity());
                index++;
            }
        }
    }

    private void createRows(View view) {
        int index = 0;

        for (int row = 0; row < rows; ++row) {
            tableRow[row] = new TableRow(getActivity());
            tableRow[row].setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            tableRow[row].setWeightSum((float) cols);

            for (int col = 0; col < cols; ++col) {
                createButton(tableRow[row], index, row, col);
                index++;
            }
            tableLayout.addView(tableRow[row]);
        }
    }

    private boolean createButton(TableRow tableRow, int index, int row, int col) {
       // int index = calculateIndex(row, col);
        TableRow.LayoutParams param = new TableRow.LayoutParams();
        param.height = TableRow.LayoutParams.MATCH_PARENT;
        param.width = TableRow.LayoutParams.MATCH_PARENT;
        param.rightMargin = 5;
        param.topMargin = 5;
        param.weight = 1;
        buttons[index] = new ImageButton(getActivity());
        buttons[index].setBackgroundColor(Color.LTGRAY);
        buttons[index].setLayoutParams(param);
        buttons[index].setId(rowColToID(row, col));
        buttons[index].setOnClickListener(onClickListener);
        tableRow.addView(buttons[index]);

        return true;
    }

    //
    // Setters
    //

    public void setButtonColor(int row, int col, @ColorInt int color) {
        setButtonColor(calculateIndex(row, col), color);
    }

    public void setButtonColor(int index, @ColorInt int color) {
        buttons[index].setBackgroundColor(color);
    }

    public void setButtonImage(int row, int col, @DrawableRes int resId) {
        setButtonImage(calculateIndex(row, col), resId);
    }

    public void setButtonImage(int index, @DrawableRes int resId) {
        buttons[index].setImageResource(resId);
    }

    public void setButtonOnClickListener(int row, int col, @Nullable View.OnClickListener l) {
        setButtonOnClickListener(calculateIndex(row, col), l);
    }

    public void setButtonOnClickListener(int index, @Nullable View.OnClickListener l) {
        buttons[index].setOnClickListener(l);
    }

    //
    // Helpers
    //

   // public static int calculateIndex(int row, int col) {
   //     return ((row * col) == 0) ? 0 : (row * col) - 1;
   // }
    public int calculateIndex(int row, int col) { return  (row * cols) + col; }
    public static int rowColToID(int row, int col) { return (row * 10) + col; }
    public static int indexToRow(int index) { return index / 10; }
    public static int indexToCol(int index) { return index % 10; }
    //
    // Getters
    //

    public ImageButton getButton(int index) { return buttons[index]; }
    public ImageButton getButton(int row, int col) { return getButton(calculateIndex(row, col)); }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    private View.OnClickListener onClickListener;
    private TableRow[] tableRow;
    private TableLayout tableLayout;
    private ImageButton[] buttons;
    private View view;
    private int rows;
    private int cols;
}
