package com.bupa.uploadpic.adapter;/**
 * Created by Administrator on 2017/4/19.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bupa.uploadpic.R;
import com.bupa.uploadpic.util.ImageUtils;
import com.bupa.uploadpic.util.UIUtils;

import java.util.LinkedList;

/**
 * 作者：ZLei on 2017/4/19 11:28
 * 邮箱：93319@163.com
 * 备注: (该类的作用)
 */
public class ImageAdapter extends BaseAdapter {
    private LinkedList<String> imagePathList;
    private Context context;

    /**
     * 控制最多上传的图片数量
     */

    public ImageAdapter(Context context, LinkedList<String> imagePath) {
        this.context = context;
        this.imagePathList = imagePath;
    }

    public void update(LinkedList<String> imagePathList) {
        this.imagePathList = imagePathList;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {// 创建ImageView
            holder = new ViewHolder();
            convertView = View.inflate(UIUtils.getContext(),
                    R.layout.up_phone_items, null);
            holder.imupadd = (ImageView) convertView
                    .findViewById(R.id.im_upadd);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position) == "default") {// 图片地址为空时设置默认图片
            holder.imupadd.setImageResource(R.drawable.up_photo_add);
        } else {
            holder.imupadd.setImageBitmap(getBitmap(getItem(position).toString()));
        }
        if (position==10) {
            imagePathList.remove(imagePathList.getLast());
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imupadd;
    }

    private Bitmap getBitmap(String data) {
        return ImageUtils.getImageThumbnail(
                data,
                ImageUtils.getWidth(UIUtils.getContext()) / 3 - 5,
                ImageUtils.getWidth(UIUtils.getContext()) / 3 - 5);
    }
}