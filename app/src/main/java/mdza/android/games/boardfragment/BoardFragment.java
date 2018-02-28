/*
 * Developed by Alberto Mendoza
 * 02/27/2018
 *
 * Reusable fragment that creates a matrix with R rows and C cols.
 * The cells on the grid can be set to a specific color or image,
 * typically serving as a game board.
 * An onclick callback can be defined for all cells or on a per
 * cell basis.
 *
 * 2/28: Fixed issue with row to index, col to index conversion
 *       Fixed exception caused when disabling cell with unlinked resource
 *       Created custom onclick listener for cell clicks
 */

package mdza.android.games.boardfragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class BoardFragment extends Fragment {

    public BoardFragment() {}

    public BoardFragment(int rows, int cols, CellClickListener cellClickListener) {
        this.rows = rows;
        this.cols = cols;
        this.cellClickListener = cellClickListener;
        tableRow = new TableRow[rows];
        buttons = new ImageButton[rows * cols];
        imageResources = new int[rows * cols];
        isEnabled = new boolean[rows * cols];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_fragment, container, false);
        tableLayout = (TableLayout) view.findViewById(R.id.boardFragment_TableLayout);
        tableLayout.setWeightSum((float) rows);
        tableLayout.setShrinkAllColumns(true);

        createRows(view);

        return view;
    }

    private void createRows(View view) {
        for (int row = 0; row < rows; ++row) {
            tableRow[row] = new TableRow(getActivity());
            tableRow[row].setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            tableRow[row].setWeightSum((float) cols);

            for (int col = 0; col < cols; ++col) {
                createButton(tableRow[row], row, col);
            }

            tableLayout.addView(tableRow[row]);
        }
    }

    private boolean createButton(TableRow tableRow, int row, int col) {
        int index = calculateIndex(row, col);

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
        setCellOnClickListener(index, cellClickListener);
        tableRow.addView(buttons[index]);

        return true;
    }

    //
    // Setters
    //

    public void setCellColor(int row, int col, @ColorInt int color) {
        setCellColor(calculateIndex(row, col), color);
    }

    public void setCellColor(int index, @ColorInt int color) {
        buttons[index].setBackgroundColor(color);
    }

    public void setCellImage(int row, int col, @DrawableRes int resId) {
        setCellImage(calculateIndex(row, col), resId);
    }

    public void setCellImage(int index, @DrawableRes int resId) {
        buttons[index].setImageResource(resId);
        imageResources[index] = resId;
    }

    public void disableCell(int row, int col) {
        disableCell(calculateIndex(row, col));
    }

    public void disableCell(int index) {
        int resourceId = imageResources[index];
        try {
            Drawable drawable = getActivity().getResources().getDrawable(resourceId);
            drawable.mutate();
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            buttons[index].setImageDrawable(drawable);
        } catch (Exception e) { }
        finally {
            isEnabled[index] = false;
        }
    }

    public void enableCell(int row, int col) {
        enableCell(calculateIndex(row, col));
    }

    public void enableCell(int index) {
        try {
            buttons[index].setImageResource(imageResources[index]);
        } catch (Exception e) { }
        finally {
            isEnabled[index] = true;
        }
    }

    public void setCellOnClickListener(final int row, final int col, final CellClickListener l) {
        int index = calculateIndex(row, col);
        buttons[index].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l.onClick(row, col);
            }
        });
    }

    public void setCellOnClickListener(int index, final CellClickListener l) {
        setCellOnClickListener(indexToRow(index), indexToCol(index), l);
    }

    public interface CellClickListener {
        void onClick(int row, int col);
    }

    //
    // Helpers
    //

    public int calculateIndex(int row, int col) { return  (row * cols) + col; }
    public int indexToRow(int index) { return index / cols; }
    public int indexToCol(int index) { return index % cols; }
    private int rowColToID(int row, int col) { return (row * 10) + col; }

    //
    // Getters
    //

    public ImageButton getButton(int index) { return buttons[index]; }
    public ImageButton getButton(int row, int col) {
        return getButton(calculateIndex(row, col));
    }
    public boolean isCellEnabled(int row, int col) {
        return isCellEnabled(calculateIndex(row, col));
    }
    public boolean isCellEnabled(int index) { return isEnabled[index]; }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    private CellClickListener cellClickListener;
    private TableRow[] tableRow;
    private TableLayout tableLayout;
    private ImageButton[] buttons;
    private int imageResources[];
    private boolean isEnabled[];
    private View view;
    private int rows;
    private int cols;
}
