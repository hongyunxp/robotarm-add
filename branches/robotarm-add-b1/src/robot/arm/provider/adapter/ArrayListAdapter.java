package robot.arm.provider.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * 列表适配器基类
 * 
 * @author wanglin(lin3.wang@changhong.com) 2011-5-20 下午02:19:33
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter {
    
    protected ArrayList<T> mList;
    
    protected Activity mContext;
    
    protected ListView mListView;
    
    public ArrayListAdapter(Activity context) {
        this.mContext = context;
    }
    
    public int getCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
    }
    
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }
    
    public long getItemId(int position) {
        return position;
    }
    
    abstract public View getView(int position,
                                 View convertView,
                                 ViewGroup parent);
    
    public void setList(ArrayList<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
    
    public ArrayList<T> getList() {
        return mList;
    }
    
    public void setList(T[] list) {
        ArrayList<T> arrayList = new ArrayList<T>(list.length);
        for (T t : list) {
            arrayList.add(t);
        }
        setList(arrayList);
    }
    
    public ListView getListView() {
        return mListView;
    }
    
    public void setListView(ListView listView) {
        mListView = listView;
    }

	
    
}
