package com.example.as.activity;

import java.util.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.example.as.R;
import com.example.as.dao.InaccountDAO;
import com.example.as.dao.OutaccountDAO;

public class TotalChart extends Activity {
    private static final float PI = 3.1415926f;
    TextView textAcc;
    private String[] type = null;
    private String passType = "";
    private List<Integer> start = new ArrayList<>();//画笔开始位置
    private List<String> text = new ArrayList<>();//画笔开始位置
    private int[] color = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.BLUE, Color.BLACK, Color.LTGRAY};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OutaccountDAO outaccountDAO = new OutaccountDAO(TotalChart.this);
        InaccountDAO inaccountDAO = new InaccountDAO(TotalChart.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        passType = bundle.getString("passType");
        textAcc = findViewById(R.id.textAcc);
        int sumM = outaccountDAO.getSumM();
        int sumM1 = inaccountDAO.getSumM();
        if ((passType.equals("OutP")||passType.equals("FOutP"))&& sumM!=0){
            String txt = "支出总计：" + sumM + "元";
            textAcc.setText(txt);
        }else if ((passType.equals("InP")||passType.equals("FInP"))&& sumM1!=0){
            String txt = "收入总计：" + sumM1 + "元";
            textAcc.setText(txt);
        }
        FrameLayout ll = (FrameLayout) findViewById(R.id.canvas);
        ll.addView(new ScanRadar(this));
    }

    public class ScanRadar extends View {
        OutaccountDAO outaccountDAO = new OutaccountDAO(TotalChart.this);
        InaccountDAO inaccountDAO = new InaccountDAO(TotalChart.this);
        public ScanRadar(Context context) {
            super(context);
        }

        public ScanRadar(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        /**
         * 这是绘图方法
         *
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int rWidth = width - getPaddingLeft() - getPaddingRight();
            int rHeight = height - getPaddingTop() - getPaddingBottom();
            int mRadious = Math.min(rWidth, rHeight) / 2;
            //圆心坐标
            int cenX = mRadious + getPaddingLeft();
            int cenY = mRadious + getPaddingTop();
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(255);
            paint.setStrokeWidth(2);

            Paint mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaintBorder.setStyle(Paint.Style.FILL);
            mPaintBorder.setAntiAlias(true);
            mPaintBorder.setColor(Color.BLACK);
            mPaintBorder.setTextSize(35);
            RectF oval = new RectF(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), width - getPaddingRight());
            if (passType.equals("OutP")) {
                Map<String, Float> outcount = outaccountDAO.getTotal();
                int sumM = outaccountDAO.getSumM();
                start.add(0);
                for (Float value : outcount.values()) {
                    int s = (int) ((value/sumM)*100);
                    System.out.println(s);
                    start.add((int) (s*3.6));
                }
                for (String str : outcount.keySet()) {
                    text.add(str);
                }
                System.out.println(start);
                int getStart = 0;
                for (int i = 0; i < start.size() - 1; i++) {
                    paint.setColor(color[i]);
                    canvas.drawArc(oval, getStart, start.get(i+1), true, paint);
                    float degree = (getStart*2 + start.get(i+1)) / 2;
                    getStart += start.get(i+1);
                    //根据角度所在不同象限来计算出文字的起始点坐标s
                    float dx = 0, dy = 0;
                    if (degree > 0 && degree <= 90f) {//在第四象限
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * degree));//注意Math.sin(x)中x为弧度值，并非数学中的角度，所以需要将角度转换为弧度
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * degree));
                    } else if (degree > 90f && degree <= 180f) {//在第三象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (180f - degree)));
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (180f - degree)));
                    } else if (degree > 180f && degree <= 270f) {//在第二象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (270f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (270f - degree)));
                    } else {
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (360f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (360f - degree)));
                    }
                    //文字的基本线坐标设置为半径的2.3/3位置处，起点y坐标设置为半径的2.7/3位置处
                    int baifenbi = (int) (start.get(i+1)/3.6);
                    canvas.drawText(text.get(i) + baifenbi + "%", dx, dy, mPaintBorder);
                }
            } else if (passType.equals("InP")) {
                Map<String, Float> incount = inaccountDAO.getTotal();
                int sumM = inaccountDAO.getSumM();
                start.add(0);
                for (Float value : incount.values()) {
                    int s = (int) ((value/sumM)*100);
                    start.add((int) (s*3.6));
                }
                for (String str : incount.keySet()) {
                    text.add(str);
                }
                int getStart = 0;
                for (int i = 0; i < start.size() - 1; i++) {
                    paint.setColor(color[i]);
                    canvas.drawArc(oval, getStart, start.get(i+1), true, paint);
                    float degree = (getStart*2 + start.get(i+1)) / 2;
                    getStart += start.get(i+1);
                    //根据角度所在不同象限来计算出文字的起始点坐标
                    float dx = 0, dy = 0;
                    if (degree > 0 && degree <= 90f) {//在第四象限
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * degree));//注意Math.sin(x)中x为弧度值，并非数学中的角度，所以需要将角度转换为弧度
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * degree));
                    } else if (degree > 90f && degree <= 180f) {//在第三象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (180f - degree)));
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (180f - degree)));
                    } else if (degree > 180f && degree <= 270f) {//在第二象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (270f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (270f - degree)));
                    } else {
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (360f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (360f - degree)));
                    }
                    int baifenbi = (int) (start.get(i+1)/3.6);
                    canvas.drawText(text.get(i) + baifenbi + "%", dx, dy, mPaintBorder);
                }
            } else if (passType.equals("FOutP")) {
                Map<String, Float> outcount = outaccountDAO.getTotal();
                int sumM = outaccountDAO.getSumM();
                start.add(0);
                for (Float value : outcount.values()) {
                    int s = (int) ((value/sumM)*100);
                    System.out.println(s);
                    start.add((int) (s*3.6));
                }
                for (String str : outcount.keySet()) {
                    text.add(str);
                }
                int getStart = 0;
                for (int i = 0; i < start.size() - 1; i++) {
                    paint.setColor(color[i]);
                    canvas.drawArc(oval, getStart, start.get(i+1), true, paint);
                    float degree = (getStart *2+ start.get(i+1)) / 2;
                    getStart += start.get(i+1);
                    //根据角度所在不同象限来计算出文字的起始点坐标
                    float dx = 0, dy = 0;
                    if (degree > 0 && degree <= 90f) {//在第四象限
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * degree));//注意Math.sin(x)中x为弧度值，并非数学中的角度，所以需要将角度转换为弧度
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * degree));
                    } else if (degree > 90f && degree <= 180f) {//在第三象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (180f - degree)));
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (180f - degree)));
                    } else if (degree > 180f && degree <= 270f) {//在第二象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (270f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (270f - degree)));
                    } else {
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (360f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (360f - degree)));
                    }
                    //文字的基本线坐标设置为半径的2.3/3位置处，起点y坐标设置为半径的2.7/3位置处
                    int baifenbi = (int) (start.get(i+1)/3.6);
                    canvas.drawText(text.get(i) + baifenbi + "%", dx, dy, mPaintBorder);
                }
            } else {
                Map<String, Float> incount = inaccountDAO.getTotal();
                int sumM = inaccountDAO.getSumM();
                start.add(0);
                for (Float value : incount.values()) {
                    int s = (int) ((value/sumM)*100);
                    start.add((int) (s*3.6));
                }
                for (String str : incount.keySet()) {
                    text.add(str);
                }
                int getStart = 0;
                for (int i = 0; i < start.size() - 1; i++) {
                    paint.setColor(color[i]);
                    canvas.drawArc(oval, getStart, start.get(i+1), true, paint);
                    float degree = (getStart*2 + start.get(i+1)) / 2;
                    getStart += start.get(i+1);
                    //根据角度所在不同象限来计算出文字的起始点坐标
                    float dx = 0, dy = 0;
                    if (degree > 0 && degree <= 90f) {//在第四象限
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * degree));//注意Math.sin(x)中x为弧度值，并非数学中的角度，所以需要将角度转换为弧度
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * degree));
                    } else if (degree > 90f && degree <= 180f) {//在第三象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (180f - degree)));
                        dy = (float) (cenY + mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (180f - degree)));
                    } else if (degree > 180f && degree <= 270f) {//在第二象限
                        dx = (float) (cenX - mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (270f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (270f - degree)));
                    } else {
                        dx = (float) (cenX + mRadious * 2.3 / 3 * Math.cos(2 * PI / 360 * (360f - degree)));
                        dy = (float) (cenY - mRadious * 2.7 / 3 * Math.sin(2 * PI / 360 * (360f - degree)));
                    }
                    int baifenbi = (int) (start.get(i+1)/3.6);
                    canvas.drawText(text.get(i) + baifenbi + "%", dx, dy, mPaintBorder);
                }
            }
        }
    }
}
