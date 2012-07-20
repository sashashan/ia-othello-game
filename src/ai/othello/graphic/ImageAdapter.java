package ai.othello.graphic;

import java.util.Hashtable;

import ai.othello.entities.GameI.COLOR;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private int width;
	private int height;

	private Hashtable<Integer, COLOR> positions = new Hashtable<Integer, COLOR>();

	public ImageAdapter(Context c, int imgWidth, int imgHeight) {
		mContext = c;
		width = imgWidth;
		height = imgHeight;
	}

	public int getCount() {
		return 8*8;
	}

	public COLOR getItem(int position) {
		if (positions.containsKey(position)) {
			return positions.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(height, width));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setBackgroundResource(R.color.light_green);
		} else {
			imageView = (ImageView) convertView;
		}
		if (getItem(position) != null) {
			imageView.setImageResource((getItem(position) == COLOR.LIGHT) ? R.drawable.light : R.drawable.dark);
		}
		return imageView;
	}

	public void setPawn(int position, COLOR color) {
		positions.put(position, color);
		notifyDataSetChanged();
	}
}