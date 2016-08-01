package mindvalley_sanjeev_android_test.mindvalley.Services;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import mindvalley_sanjeev_android_test.mindvalley.R;

/**
 * Created by AT0003 on 27-07-2016.
 * Auther Sanjeev Sangral software Developer
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
   // private String[] mDataset;
    static ArrayList<HashMap<String, String>> user_List;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView id,name,date,username,color,likes,width;
        private ImageView img_android;

        public ViewHolder(View v) {
            super(v);
          //  id = (TextView)v.findViewById(R.id.id);
         //   date = (TextView)v.findViewById(R.id.date);
            likes = (TextView)v.findViewById(R.id.likes);
//            width = (TextView)v.findViewById(R.id.width);
//            color = (TextView)v.findViewById(R.id.color);
            username = (TextView)v.findViewById(R.id.username);
            img_android = (ImageView) v.findViewById(R.id.image_profile);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<HashMap<String, String>> user_List, Context context) {

        this.user_List = user_List;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        HashMap<String,String> mArrayList= new HashMap<>();
        mArrayList = user_List.get(position);
     //   holder.id.setText(mArrayList.get(Constant.TAG_ID));
//        holder.date.setText(mArrayList.get(Constant.CREATED_AT));
        holder.likes.setText(mArrayList.get(Constant.LIKES));
//        holder.width.setText(mArrayList.get(MainActivity.WIDTH));
//        holder.color.setText(mArrayList.get(MainActivity.COLOR));
        holder.username.setText(mArrayList.get(Constant.USERNAME));
        Picasso.with(context).load(mArrayList.get(Constant.IMAGE)).resize(240, 120).into(holder.img_android);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return user_List.size();
    }


}
