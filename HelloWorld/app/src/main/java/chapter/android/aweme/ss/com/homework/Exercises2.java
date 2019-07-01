package chapter.android.aweme.ss.com.homework;

import android.app.Activity;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {
    private LinearLayout textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise2);
        //testView = (LinearLayout) findViewById(R.id.vg);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.activity_tips, null);
        textView = findViewById(R.id.vg);
//        System.out.println("onCreate: " + getViewCount(view) + "个view");
        TextView tv = (TextView)findViewById(R.id.mtv);
        tv.setText("activity_tips: " + getViewCount(view) + "个view");
    }
    public static int count = 0;

    public static int getViewCount(View view) {
        //todo 补全你的代码
        if(view instanceof ViewGroup){
            ViewGroup vp1 = (ViewGroup) view;
            for (int i=0; i<vp1.getChildCount(); i++){
                count++;
                View viewchild = vp1.getChildAt(i);
                getViewCount(viewchild);
            }
        }else{
            count++;
        }
        return count;
    }
}
