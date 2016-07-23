package com.imooc.calculator;

import java.util.ArrayList;
import java.util.Arrays;

import bsh.EvalError;
import bsh.Interpreter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	Button btn_0;
	Button btn_1;
	Button btn_2;
	Button btn_3;
	Button btn_4;
	Button btn_5;
	Button btn_6;
	Button btn_7;
	Button btn_8;
	Button btn_9;

	Button btn_point;
	Button btn_divide;
	Button btn_multiply;// ����
	Button btn_minus;// ��ȥ
	Button btn_pluse;// ��
	Button btn_equal;// ����

	Button btn_clear;
	Button btn_del;

	EditText et_showview;
	boolean needclear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_0 = (Button) findViewById(R.id.btn_0);
		btn_1 = (Button) findViewById(R.id.btn_1);
		btn_2 = (Button) findViewById(R.id.btn_2);
		btn_3 = (Button) findViewById(R.id.btn_3);
		btn_4 = (Button) findViewById(R.id.btn_4);
		btn_5 = (Button) findViewById(R.id.btn_5);
		btn_6 = (Button) findViewById(R.id.btn_6);
		btn_7 = (Button) findViewById(R.id.btn_7);
		btn_8 = (Button) findViewById(R.id.btn_8);
		btn_9 = (Button) findViewById(R.id.btn_9);
		btn_point = (Button) findViewById(R.id.btn_point);// С���
		btn_divide = (Button) findViewById(R.id.btn_divide);// ����
		btn_multiply = (Button) findViewById(R.id.btn_multiply);// ����
		btn_minus = (Button) findViewById(R.id.btn_minus);// ��ȥ
		btn_pluse = (Button) findViewById(R.id.btn_pluse);// ��
		btn_equal = (Button) findViewById(R.id.btn_equal);// ����

		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_del = (Button) findViewById(R.id.btn_del);
		et_showview = (EditText) findViewById(R.id.et_showview);

		btn_0.setOnClickListener(this);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		btn_4.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_6.setOnClickListener(this);
		btn_7.setOnClickListener(this);
		btn_8.setOnClickListener(this);
		btn_9.setOnClickListener(this);

		btn_point.setOnClickListener(this);
		btn_divide.setOnClickListener(this);
		btn_multiply.setOnClickListener(this);
		btn_minus.setOnClickListener(this);
		btn_pluse.setOnClickListener(this);
		btn_equal.setOnClickListener(this);

		btn_clear.setOnClickListener(this);
		btn_del.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String str = et_showview.getText().toString();
		switch (v.getId()) {
		case R.id.btn_0:
		case R.id.btn_1:
		case R.id.btn_2:
		case R.id.btn_3:
		case R.id.btn_4:
		case R.id.btn_5:
		case R.id.btn_6:
		case R.id.btn_7:
		case R.id.btn_8:
		case R.id.btn_9:
		case R.id.btn_point:
			if(needclear){
				str = "";
				et_showview.setText("");
			}
			et_showview.setText(str + ((Button) v).getText());
			break;
		case R.id.btn_pluse:
		case R.id.btn_minus:
		case R.id.btn_multiply:
		case R.id.btn_divide:
			if(needclear){
				et_showview.setText("");
			}
			et_showview.setText(str +" "+((Button) v).getText()+" ");
			break;
		case R.id.btn_equal:
			getResult();
			break;
		case R.id.btn_del:
			if (str != null && !str.equals("")) {
				et_showview.setText(str.substring(0, str.length() - 1));
			}
			break;
		case R.id.btn_clear:
			et_showview.setText("");
			break;
		}
	}

	/**
	 * ��ȡ������
	 */
	private void getResult() {
		needclear = true;
		String exp = et_showview.getText().toString();
		double r = 0;
	    int space = exp.indexOf(' ');//���������ո�λ��
        String s1 = exp.substring(0, space);//s1���ڱ����һ��������
        String op = exp.substring(space + 1, space + 2);//op���ڱ��������
        String s2 = exp.substring(space + 3);//s2���ڱ���ڶ���������
        double arg1 = Double.parseDouble(s1);//���������stringת��ΪSingle
        double arg2 = Double.parseDouble(s2);
        if(op.equals("＋")){
        	 r = arg1 + arg2;
        }else if(op.equals("－")){
        	r = arg1 - arg2;
        }else if(op.equals("×")){
        	 r = arg1 * arg2;
        }else if(op.equals("÷")){
        	 if (arg2 == 0)
             {
                r=0;
             }
             else
             {
                 r = arg1 / arg2;
             }
        }       
        if(!s1.contains(".")&&!s2.contains(".")){
        	int result = (int)r;
        	et_showview.setText(result+"");
        }else{
        	et_showview.setText(r+"");
        }
	}
//
//	/***
//	 * @param exp
//	 *            ������ʽ
//	 * @return ��ݱ��ʽ���ؽ��
//	 */
//	private String getRs(String exp) {
//		Interpreter bsh = new Interpreter();
//		Number result = null;
//		try {
//			exp = filterExp(exp);
//			result = (Number) bsh.eval(exp);
//		} catch (EvalError e) {
//			e.printStackTrace();
//			return "0";
//		}
//		return result.doubleValue() + "";
//	}
//
//	/**
//	 * @param exp
//	 *            ������ʽ
//	 * @return ��Ϊ��������,ȫ����Ҫ��С�����.
//	 */
//	private String filterExp(String exp) {
//		String num[] = exp.split("");
//		String temp = null;
//		double dtemp = 0 ;
//		String str_temp="";
//		for (int i = 0; i < num.length; i++) {
//			temp = num[i];
//			
//			if (temp.equals("+") || temp.equals("-") || temp.equals("��")
//					|| temp.equals("��")) {
//				if(dtemp==0){
//				dtemp=Double.parseDouble(str_temp);
//				}
//				
//				str_temp="";
//			}else{
//				str_temp=str_temp+temp;
//			}
//		}
//
//		int begin = 0, end = 0;
//		for (int i = 1; i < num.length; i++) {
//			temp = num[i];
//			if (temp.matches("[+-/()*]")) {
//				if (temp.equals("."))
//					continue;
//				end = i - 1;
//				temp = exp.substring(begin, end);
//				if (temp.trim().length() > 0 && temp.indexOf(".") < 0)
//					num[i - 1] = num[i - 1] + ".0";
//				begin = end + 1;
//			}
//		}
//		return Arrays.toString(num).replaceAll("[\\[\\], ]", "");
//	}
}
