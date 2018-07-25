package com.moons.xst.track.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moons.xst.track.R;

public class AlertDialogAdapter extends BaseAdapter {

	private Context context;
	private List<String> listData;
	private int mCheckItem;
	
	HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个
	
	
	public AlertDialogAdapter(Context context, List<String> list, int checkItem)
	{
		this.context = context;
		this.listData= list;
		mCheckItem = checkItem;
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.listitem_alertdialog, null);
			holder = new ViewHolder();
			holder.desc = (TextView) convertView.findViewById(R.id.alertdialog_itemdesc);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
        final ImageView radio=(ImageView) convertView.findViewById(R.id.radio);
		holder.rdBtn = radio;
		
		holder.desc.setText(listData.get(position));
		holder.rdBtn.setBackgroundResource(R.drawable.icon_radiobutton_unchecked);
		if (listData.get(position).equalsIgnoreCase(listData.get(mCheckItem)))
			holder.rdBtn.setBackgroundResource(R.drawable.icon_radiobutton_checked);
		
		//当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中	
        /*holder.rdBtn.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
              
                //重置，确保最多只有一项被选中
                for(String key:states.keySet()){
                    states.put(key, false);                   
                }
                states.put(String.valueOf(position), radio.isChecked());
                mCheckItem = position;
                AlertDialogAdapter.this.notifyDataSetChanged();
            }
        });
		
        boolean res=false;
        if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position))== false){
            res=false;
            states.put(String.valueOf(position), false);
        }
        else
            res = true;
        
        holder.rdBtn.setChecked(res);*/
        
        

		return convertView;
	}
	
	static class ViewHolder {
           TextView desc;
           ImageView rdBtn;
	}
}
