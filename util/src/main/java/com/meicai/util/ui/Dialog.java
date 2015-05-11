package com.meicai.util.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.meicai.util.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by woo on 1/13/15.
 */
public class Dialog {


	private static String time = null;

	public static void showConfirm(Context ctx, String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		final AlertDialog dialog = builder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface d) {
				Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
		});
		dialog.show();
	}

	public static void showSelect(Context ctx, String title, String msg, DialogInterface.OnClickListener lis) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("确认", lis);
		builder.setNegativeButton("取消", null);


		final AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void showSelectForScan(Context ctx, String title, String msg, DialogInterface.OnClickListener lis1, DialogInterface.OnClickListener lis2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("确认并继续", lis1);
		builder.setNeutralButton("确认", lis2);
		builder.setNegativeButton("取消", null);


		final AlertDialog dialog = builder.create();
		dialog.show();
	}

    public interface OnConfirmDateListener {
        boolean OnConfirm(String start,String end);
    }

	public interface  ResultTimeStamp{
		boolean onResultTime(String date,String time);
	}

    public static AlertDialog showSelectTime(final Context ctx,LayoutInflater inflater, final OnConfirmDateListener listener) {
		return showSelectTime(ctx,inflater,"yyyy-M-d",listener);
    }


	public static AlertDialog showSelectTime(final Context ctx,LayoutInflater inflater,final String format, final OnConfirmDateListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		final AlertDialog dialog =  builder.setView(inflater.inflate(R.layout.dialog_setting_time, null))
				// Add action buttons
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				})
				.setNegativeButton("取消", null).create();
		dialog.show();
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePicker picker = (DatePicker) dialog.findViewById(R.id.dp_1);
				DatePicker picker_end = (DatePicker) dialog.findViewById(R.id.dp_2);
				String start = String.format("%d-%d-%d",picker.getYear(),picker.getMonth()+1,picker.getDayOfMonth());
				String end = String.format("%d-%d-%d",picker_end.getYear(),picker_end.getMonth()+1,picker_end.getDayOfMonth());
				SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
				try {
					Date dstart = simpledateformat.parse(start);
					Date dend = simpledateformat.parse(end);
					if (dstart.after(dend) ) {
						Dialog.showConfirm(ctx,"错误","选择时间错误");
						return;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (listener != null) {
					listener.OnConfirm(start, end);
				}
				dialog.dismiss();

			}
		});
		return dialog;
	}

	public static  AlertDialog showTimePicker(final Context ctx,LayoutInflater inflater,
										   final ResultTimeStamp listener){


		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		final AlertDialog dialog =  builder.setView(inflater.inflate(R.layout.dialog_select_timestamp, null))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {


					}
				})
				.setNegativeButton("取消", null).create();
		dialog.show();


		TimePicker time_picker = (TimePicker) dialog.findViewById(R.id.timePicker);
		time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				time = hourOfDay + ":"  + minute + ":00";
			}
		});

		dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePicker date_picker = (DatePicker) dialog.findViewById(R.id.datePicker);
				String date = String.format("%d-%d-%d",date_picker.getYear(),date_picker.getMonth()+1,date_picker.getDayOfMonth());
				if (listener != null) {
					listener.onResultTime(date,time);
				}
				dialog.dismiss();
			}
		});

		return dialog;

	}


}
