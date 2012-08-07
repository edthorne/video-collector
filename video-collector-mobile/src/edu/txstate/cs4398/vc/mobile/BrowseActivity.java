package edu.txstate.cs4398.vc.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import edu.txstate.cs4398.vc.mobile.controller.CollectionReadWriter;
import edu.txstate.cs4398.vc.mobile.video.VideoMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class BrowseActivity extends Activity {

	private GestureDetector mGestureDetector;

    // x and y coordinates within our side index
    private static float sideIndexX;
    private static float sideIndexY;
    private ArrayList<String> titles;
    private ArrayList<VideoMobile> vml;
    // height of side index
    private int sideIndexHeight;
    
    // number of items in the side index
    private int indexListSize;

    // list with items for side index
    private ArrayList<Object[]> indexList = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_layout);
        CollectionReadWriter rw = new CollectionReadWriter();
        vml = (ArrayList<VideoMobile>)rw.getVideosFromXml(this);
        Collections.sort(vml, new TitleComparator());
        titles = new ArrayList<String>();
        for(VideoMobile vid: vml){
        	Log.i("Interfaces", "Img url: " + vid.getImageURL());
        	titles.add(vid.getTitle());
        }
        Collections.sort(titles);
        
        final ListView lv1 = (ListView) findViewById(R.id.list_view);
        lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles));
       
        lv1.setOnItemClickListener(new ItemListener());
        mGestureDetector = new GestureDetector(this, new SideIndexGestureListener());
    }
    
    private class TitleComparator implements Comparator<VideoMobile>{

		public int compare(VideoMobile vd1, VideoMobile vd2) {
			String t1 = vd1.getTitle();
			String t2 = vd2.getTitle();
			return t1.compareTo(t2);
		}
    	
    }
    
    private class ItemListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.i("Interfaces", "Touched: " + vml.get(position).getTitle());
			VideoMobile vd = vml.get(position);
			
			String title = vd.getTitle();
			String category = vd.getCategory();
			String director = vd.getDirector();
			int year = vd.getYear();
			int runtime = vd.getRuntime();
			String myRating = vd.getRated();
			String notes = vd.getNotes();

			Intent next = new Intent(view.getContext(), ViewActivity.class);
			next.putExtra("title", title);
		    next.putExtra("category", category);
		    next.putExtra("director", director);
		    next.putExtra("year", year);
		    next.putExtra("runtime", runtime);
		    next.putExtra("rating", myRating);
		    next.putExtra("notes", notes);  
			startActivity(next);   
		}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mGestureDetector.onTouchEvent(event))
        {
        	Log.i("Interfaces", "Touched: " + event.toString());
            return true;
        } 
        else
        {
            return false;
        }
    }

    private ArrayList<Object[]> createIndex(String[] strArr)
    {
        ArrayList<Object[]> tmpIndexList = new ArrayList<Object[]>();
        Object[] tmpIndexItem = null;

        int tmpPos = 0;
        String tmpLetter = "";
        String currentLetter = null;
        String strItem = null;

        for (int j = 0; j < strArr.length; j++)
        {
            strItem = strArr[j];
            currentLetter = strItem.substring(0, 1);

            // every time new letters comes
            // save it to index list
            if (!currentLetter.equals(tmpLetter))
            {
                tmpIndexItem = new Object[3];
                tmpIndexItem[0] = tmpLetter;
                tmpIndexItem[1] = tmpPos - 1;
                tmpIndexItem[2] = j - 1;

                tmpLetter = currentLetter;
                tmpPos = j + 1;

                tmpIndexList.add(tmpIndexItem);
            }
        }

        // save also last letter
        tmpIndexItem = new Object[3];
        tmpIndexItem[0] = tmpLetter;
        tmpIndexItem[1] = tmpPos - 1;
        tmpIndexItem[2] = strArr.length - 1;
        tmpIndexList.add(tmpIndexItem);

        // and remove first temporary empty entry
        if (tmpIndexList != null && tmpIndexList.size() > 0)
        {
            tmpIndexList.remove(0);
        }

        return tmpIndexList;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        
        //final ListView listView = (ListView) findViewById(R.id.list_view);
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.side_index);
        sideIndexHeight = sideIndex.getHeight();
        sideIndex.removeAllViews();

        // TextView for every visible item
        TextView tmpTV = null;

        // we'll create the index list
        indexList = createIndex(titles.toArray(new String[titles.size()]));

        // number of items in the index List
        indexListSize = indexList.size();

        // maximal number of item, which could be displayed
        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);

        int tmpIndexListSize = indexListSize;

        // handling that case when indexListSize > indexMaxSize
        while (tmpIndexListSize > indexMaxSize)
        {
            tmpIndexListSize = tmpIndexListSize / 2;
        }

        // computing delta (only a part of items will be displayed to save a
        // place)
        double delta = indexListSize / tmpIndexListSize;

        String tmpLetter = null;
        Object[] tmpIndexItem = null;

        // show every m-th letter
        for (double i = 1; i <= indexListSize; i = i + delta)
        {
            tmpIndexItem = indexList.get((int) i - 1);
            tmpLetter = tmpIndexItem[0].toString();
            tmpTV = new TextView(this);
            tmpTV.setText(tmpLetter);
            tmpTV.setGravity(Gravity.TOP);
            tmpTV.setTextSize(13);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }

        // and set a touch listener for it
        sideIndex.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                // now you know coordinates of touch
                sideIndexX = event.getX();
                sideIndexY = event.getY();

                // and can display a proper item it country list
                displayListItem();

                return false;
            }
            
        });
        
    }

    class SideIndexGestureListener extends
            GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY)
        {
            // we know already coordinates of first touch
            // we know as well a scroll distance
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            // when the user scrolls within our side index
            // we can show for every position in it a proper
            // item in the country list
            if (sideIndexX >= 0 && sideIndexY >= 0)
            {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public void displayListItem()
    {
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        // compute minimal position for the item in the list
        int minPosition = (int) (itemPosition * pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        Object[] indexItem = indexList.get(itemPosition);

        // and compute the proper item in the country list
        int indexMin = Integer.parseInt(indexItem[1].toString());
        int indexMax = Integer.parseInt(indexItem[2].toString());
        int indexDelta = Math.max(1, indexMax - indexMin);

        double pixelPerSubitem = pixelPerIndexItem / indexDelta;
        int subitemPosition = (int) (indexMin + (sideIndexY - minPosition) / pixelPerSubitem);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setSelection(subitemPosition);
    }
}
