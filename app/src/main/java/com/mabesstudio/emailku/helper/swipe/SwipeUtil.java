package com.mabesstudio.emailku.helper.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mabesstudio.emailku.R;

public abstract class SwipeUtil extends ItemTouchHelper.SimpleCallback {
    private Drawable background;
    private Drawable deleteIcon;

    private int xMarkMargin;

    private boolean initiated;
    private Context context;

    private int leftColorCode;
//    private String leftSwipeLabel;

    public SwipeUtil(int dragDirs, int swipeDirs, Context context){
        super(dragDirs, swipeDirs);
        this.context = context;
    }

    private void init(){
        background = new ColorDrawable();
        xMarkMargin = (int) context.getResources().getDimension(R.dimen.ic_clear_margin);
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_light);
        deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initiated = true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View view = viewHolder.itemView;
        if (!initiated) {
            init();
        }
        int itemHeight = view.getBottom() - view.getTop();

        //pengaturan background swipe
        ((ColorDrawable) background).setColor(getLeftColorCode());
        background.setBounds(view.getRight() + (int) dX, view.getTop(), view.getRight(), view.getBottom());
        background.draw(c);

        int intrinsicWidth = deleteIcon.getIntrinsicWidth();
        int intrinsicHeight = deleteIcon.getIntrinsicHeight();

        int xMarkLeft = view.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = view.getRight() - xMarkMargin;
        int xMarkTop = view.getTop() + (itemHeight - intrinsicHeight) / 2;
        int xMarkBottom = xMarkTop + intrinsicHeight;

        //pengaturan icon saat swipe
        deleteIcon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
        deleteIcon.draw(c);

//        //pengaturan text saat swipe
//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        paint.setTextSize(36);
//        paint.setTextAlign(Paint.Align.CENTER);
//        c.drawText(getLeftSwipeLabel(), xMarkLeft + 40, xMarkTop + 10, paint);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public int getLeftColorCode() {
        return leftColorCode;
    }

//    public String getLeftSwipeLabel() {
//        return leftSwipeLabel;
//    }

    public void setLeftColorCode(int leftColorCode) {
        this.leftColorCode = leftColorCode;
    }

//    public void setLeftSwipeLabel(String leftSwipeLabel) {
//        this.leftSwipeLabel = leftSwipeLabel;
//    }
}
