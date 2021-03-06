package org.ayo.robot.anim.propertyanim;

import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.ayo.component.Master;
import org.ayo.component.MasterActivity;
import org.ayo.robot.anim.DemoBase;
import org.ayo.robot.anim.R;
import org.ayo.robot.anim.propertyanim.model.AnimatorInfo;
import org.ayo.robot.anim.propertyanim.model.AnimatorSetInfo;
import org.ayo.sample.menu.notify.ToasterDebug;

import java.util.ArrayList;

import static android.R.attr.type;

public class AnimatorCreateActivity extends MasterActivity {

    private View mTarget,mTarget2;

    private void add(){
        reset();
        AlertDialog.Builder builder5=new AlertDialog.Builder(getActivity());
        final String arrItem[] = {AnimatorInfo.TYPE_alpha,
        AnimatorInfo.TYPE_rotate,
        AnimatorInfo.TYPE_rotateX,
        AnimatorInfo.TYPE_rotateY,
        AnimatorInfo.TYPE_scaleX,
        AnimatorInfo.TYPE_scaleY,
        AnimatorInfo.TYPE_translateX,
        AnimatorInfo.TYPE_translateY};
        builder5.setItems(arrItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String type = arrItem[which];
                Class c = null;
                if(type.equals(AnimatorInfo.TYPE_alpha)){
                    c = DemoAlpha.class; //.startForResult(a);
                }else if(type.equals(AnimatorInfo.TYPE_rotate)){
                    c = DemoRotate.class; //.startForResult(a);
                }else if(type.equals(AnimatorInfo.TYPE_rotateX)){
                    c = DemoRotateX.class; //.startForResult(a);
                }else if(type.equals(AnimatorInfo.TYPE_rotateY)){
                    c = DemoRotateY.class; //.startForResult(a);
                }else if(type.equals(AnimatorInfo.TYPE_scaleX)){
                    c = DemoScaleX.class; //.startForResult(a);
                }else if(type.equals(AnimatorInfo.TYPE_scaleY)){
                    c = DemoScaleY.class; //.startForResult(a);
                }else if(type.equals(AnimatorInfo.TYPE_translateX)){
                    c = DemoTranslateX.class; //.startForResult(a);
                }else if(type.equals(AnimatorInfo.TYPE_translateY)){
                    c = DemoTranslateY.class; //.startForResult(a);
                }

                Master.startPageForResult(getActivity(), c, null, 205);
                //DemoBase.startForResult(getActivity(), arrItem[which]);
            }
        });
        builder5.create().show();
    }

    protected void startAnim() {
        if(animatorSetInfo == null || animatorSetInfo.animators == null || animatorSetInfo.animators.size() == 0){
            ToasterDebug.toastShort("请先创建动画");
            return;
        }
        o1 = animatorSetInfo.parse(mTarget);
        o1.start();

        o2 = animatorSetInfo.parse(mTarget2);
        o2.start();
    }

    private AnimatorSet o1, o2;
    private AnimatorSetInfo animatorSetInfo;

    protected void stopAnim() {
        if(o1 != null) o1.cancel();
        if(o2 != null) o2.cancel();
    }

    protected void reset(){
        if(o1 != null) {
            o1.cancel();
            resetAnim(mTarget);
        }
        if(o2 != null) {
            o2.cancel();
            resetAnim(mTarget2);
        }
    }

    protected void resetAnim(View v){
        v.setPivotX(0);
        v.setPivotY(0);
        v.setRotation(0);
        v.setRotationX(0);
        v.setRotationY(0);
        v.setTranslationX(0);
        v.setTranslationY(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setTranslationZ(0);
        }
        v.setAlpha(1);
        v.setScaleX(1);
        v.setScaleY(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("result", "222222");
        if(DemoBase.isMyResult(requestCode)){
            AnimatorInfo a = DemoBase.getResult(data);
            if(a != null){
                animatorSetInfo.animators.add(a);
                TextView tv_info = id(R.id.tv_info);
                tv_info.setText(tv_info.getText() + "\n" + a.toString());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_animator_create);
        mTarget = findViewById(R.id.hello_world);
        mTarget2 = findViewById(R.id.hello_world2);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopAnim();
            }
        });

        findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                add();
            }
        });
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        animatorSetInfo = new AnimatorSetInfo();
        animatorSetInfo.playSequencial = AnimatorSetInfo.PLAY_TOGETHER;
        animatorSetInfo.animators = new ArrayList<>();

        RadioGroup rg_play = id(R.id.rg_play);
        rg_play.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_together){
                    animatorSetInfo.playSequencial = AnimatorSetInfo.PLAY_TOGETHER;
                }else if(checkedId == R.id.rb_seq){
                    animatorSetInfo.playSequencial = AnimatorSetInfo.PLAY_SEQUENCIAL;
                }
            }
        });
    }

    public <T> T id(int id) {
        return (T) findViewById(id);
    }


}
