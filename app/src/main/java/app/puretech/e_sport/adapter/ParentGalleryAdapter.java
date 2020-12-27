package app.puretech.e_sport.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.puretech.e_sport.R;
import app.puretech.e_sport.model.ParentGalleryDTO;
import app.puretech.e_sport.utill.ImageUtil;

import static app.puretech.e_sport.utill.DataManager.urlTrainerGallary;

public class ParentGalleryAdapter extends BaseAdapter {

    private List<ParentGalleryDTO> array_list;
    private Activity activity;

    public ParentGalleryAdapter(List<ParentGalleryDTO> array_list, Activity activity) {
        this.array_list = array_list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return array_list.size();
    }

    @Override
    public Object getItem(int position) {
        return array_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).
                    inflate(R.layout.parent_gallery_item, parent, false);
        }
        ImageView iv_image = convertView.findViewById(R.id.iv_trainer_gallery_image);
       // ImageUtil.displayImage(iv_image, array_list.get(position).getStr_image(), null);

        Picasso.with(activity).load(urlTrainerGallary +array_list.get(position).getStr_image())
                .error(R.drawable.no_gallery)
                .into(iv_image);
        return convertView;
    }
}