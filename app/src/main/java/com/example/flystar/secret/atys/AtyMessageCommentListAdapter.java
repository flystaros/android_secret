package com.example.flystar.secret.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.flystar.secret.R;
import com.example.flystar.secret.bean.Comment;
import com.example.flystar.secret.tools.LogMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flystar on 2015/2/28.
 */
public class AtyMessageCommentListAdapter extends BaseAdapter
{
    public AtyMessageCommentListAdapter(Context context) {
        this.context = context;
    }

    private List<Comment> comments = new ArrayList<Comment>();
    private Context context;

    public Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.aty_comment_list_cell,null);
            convertView.setTag(new ListCell((TextView) convertView.findViewById(R.id.tvCellLable)));
        }
        ListCell lc = (ListCell) convertView.getTag();
        Comment comment = (Comment) getItem(position);

        LogMessage.LogPrint("--------++++++-------"+comment.getContent());

        lc.tvCellLabel.setText(comment.getContent());


        return convertView;
    }


    public void addAll(List<Comment> comments)
    {
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }
    public void clear()
    {
        this.comments.clear();
        notifyDataSetChanged();
    }


    public static class ListCell
    {
        public ListCell(TextView tvCellLabel) {
            this.tvCellLabel = tvCellLabel;
        }

        private TextView tvCellLabel;

        public TextView getTvCellLabel() {
            return tvCellLabel;
        }
    }
}
