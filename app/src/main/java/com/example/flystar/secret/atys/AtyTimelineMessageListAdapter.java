package com.example.flystar.secret.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.flystar.secret.R;
import com.example.flystar.secret.bean.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flystar on 2015/2/27.
 */
public class AtyTimelineMessageListAdapter extends BaseAdapter
{

    public AtyTimelineMessageListAdapter(Context context)
    {
        this.context = context;
    }

    private List<Message> data = new ArrayList<Message>();

    private Context context = null;

    public Context getContext()
    {
        return context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Message getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.aty_timeline_list_cell,null);
            convertView.setTag(new ListCell((TextView) convertView.findViewById(R.id.tvCellLable)));
        }

        ListCell lc = (ListCell) convertView.getTag();  //获取在convertView中保存的格外数据listCell

        Message msg = getItem(position);

        lc.getTvCellLabel().setText(msg.getMsg());

        return convertView;
    }




    public void clear()
    {
        data.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<Message> data)
    {
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    public static class ListCell
    {
        public ListCell(TextView tvCellLabel)
        {
            this.tvCellLabel = tvCellLabel;
        }
        private TextView tvCellLabel;

        public TextView getTvCellLabel() {
            return tvCellLabel;
        }
    }



}
