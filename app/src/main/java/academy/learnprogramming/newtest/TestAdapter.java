package academy.learnprogramming.newtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import academy.learnprogramming.newtest.response.ImagepResponse;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyviewHolder> {
    Context context;
    List<ImagepResponse> productResponseList;
    public TestAdapter(Context context, List<ImagepResponse> productResponseList)
    {
        this.context = context;
        this.productResponseList = productResponseList;
    }
    @NonNull
    @Override
    public TestAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_item,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.MyviewHolder holder, int position) {

       // holder.imageView.setText(productResponseList.get(position).getName());
        String s=productResponseList.get(position).getImg();
        Bitmap bm= StringToBitMap(s);
        holder.imageView.setImageBitmap(StringToBitMap(s));
        holder.textView.setText(productResponseList.get(position).getText());

    }
    public void setProductList(List<ImagepResponse> productList)
    {
        this.productResponseList=productList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(productResponseList!= null){

            return productResponseList.size();
        }
        return 0;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyviewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img1);
            textView = itemView.findViewById(R.id.text1);

        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
