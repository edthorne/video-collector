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

/**
 * Responsible for displaying the videos saved on mobile device sorted by title.
 * @author Rudolph Newball
 *
 */
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
    final static String[] ABC =	{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
    							 "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
    							 "W", "X", "Y", "Z"};

    /**
     * Called when activity is created. It sorts the title and assign the widgets on screen to a variable.
     * Sets up the variables needed to create the side index.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_layout);
        CollectionReadWriter rw = new CollectionReadWriter();
        vml = (ArrayList<VideoMobile>) ((VideoApp)getApplicationContext()).getVideoList();
        Collections.sort(vml, new TitleComparator());
        titles = new ArrayList<String>();
        for(VideoMobile vid: vml){
        	titles.add(vid.getTitle());
        }
        Collections.sort(titles);
        
        final ListView lv1 = (ListView) findViewById(R.id.list_view);
        lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles));
       
        lv1.setOnItemClickListener(new ItemListener());
        mGestureDetector = new GestureDetector(this, new SideIndexGestureListener());
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (mGestureDetector.onTouchEvent(event))
        {
            return true;
        } 
        else
        {
            return false;
        }
    }
    
    /**
     * Called when focus is changed to this activity.
     * It sets up the side index to be displayed
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        final LinearLayout sideIndex = (LinearLayout) findViewById(R.id.side_index);
        sideIndexHeight = sideIndex.getHeight();
        sideIndex.removeAllViews();

        TextView tv = null;

        indexList = createIndex(titles.toArray(new String[titles.size()]));

        indexListSize = indexList.size();

        // maximal number of item, which could be displayed
        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 26);

        int tmpIndexListSize = indexListSize;

        while (tmpIndexListSize > indexMaxSize)
        {
            tmpIndexListSize = tmpIndexListSize / 2;
        }

        double delta = indexListSize / tmpIndexListSize;

        String tmpLetter = null;
        Object[] tmpIndexItem = null;

        for (double i = 1; i <= indexListSize; i = i + delta)
        {
            tmpIndexItem = indexList.get((int) i - 1);
            tmpLetter = tmpIndexItem[0].toString();
            tv = new TextView(this);
            tv.setText(tmpLetter);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(20);		// size of each index letter
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
            tv.setLayoutParams(params);
            sideIndex.addView(tv);
        }

        // and set a touch listener for it
        sideIndex.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                // Coordinates of touch on index list
                sideIndexX = event.getX();
                sideIndexY = event.getY();
                displayListItem();
                return false;
            }
            
        });
        
    }
    
    private ArrayList<Object[]> createIndex(String[] strArr)
    {
        ArrayList<Object[]> tmpIndexList = new ArrayList<Object[]>();
        Object[] tmpIndexItem = null;

        int tmpPos = 0, latestLetterIdx = 0;
        String tmpLetter = " ";
        String currentVidLetter = null;
        String strItem = null;

        if(strArr.length > 0){
	        for (int i = 0; i < ABC.length; i++)
	        {
	        	if(i < strArr.length){
	        		strItem = strArr[i];
	        		currentVidLetter = strItem.substring(0,1);
	        	}
	        	// Map the first video of a given letter to its corresponding letter on the side index
	        	// if there is no video for a given letter, then that letter well map to the previous mapped video
	            if (!tmpLetter.equals(currentVidLetter))
	            {  
	            	// adding new letter
	            	tmpIndexItem = new Object[3];
	                tmpIndexItem[0] = tmpLetter;
	                tmpIndexItem[1] = tmpPos - 1;
	                tmpIndexItem[2] = i - 1;
	                tmpLetter = currentVidLetter;
	                
	             	
	                tmpIndexList.add(tmpIndexItem);
	                
	                int j = latestLetterIdx;
	             	while(!(currentVidLetter.equals(ABC[j]))){
	             		tmpIndexItem = new Object[3];
	                    tmpIndexItem[0] = ABC[j];
	                    tmpIndexItem[1] = tmpPos - 1;
	                    tmpIndexItem[2] = i - 1;
	                    tmpIndexList.add(tmpIndexItem);
	                    j++;
	                    
	             	}		
	             	latestLetterIdx = j+1;
	             	tmpPos = i + 1;
	             	
	            }
	            if(ABC[i].equals("Z") && tmpLetter.equals(currentVidLetter)){
	            	int j = latestLetterIdx -1;
	            	while(!("Z".equals(ABC[j]))){
	             		tmpIndexItem = new Object[3];
	                    tmpIndexItem[0] = ABC[j];
	                    tmpIndexItem[1] = tmpPos - 2;
	                    tmpIndexItem[2] = latestLetterIdx - 1;
	                    tmpIndexList.add(tmpIndexItem);
	                    j++;
	             	}
	            }
	            
	        }
        }

        tmpIndexItem = new Object[3];
        tmpIndexItem[0] = ABC[ABC.length - 1];
        tmpIndexItem[1] = tmpPos - 1;
        tmpIndexItem[2] = ABC.length - 1;
        tmpIndexList.add(tmpIndexItem);

        if (tmpIndexList != null && tmpIndexList.size() > 0)
        {
            tmpIndexList.remove(0);
        }

        return tmpIndexList;
    }

    private class SideIndexGestureListener extends
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

            if (sideIndexX >= 0 && sideIndexY >= 0)
            {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private void displayListItem()
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
    
    private class TitleComparator implements Comparator<VideoMobile>{

		public int compare(VideoMobile vd1, VideoMobile vd2) {
			String t1 = vd1.getTitle();
			String t2 = vd2.getTitle();
			return t1.compareTo(t2);
		}
    	
    }
    
    private class ItemListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//Log.i("Interfaces", "Touched: " + vml.get(position).getTitle());
			VideoMobile vd = vml.get(position);
			
			String title = vd.getTitle();
			String category = vd.getCategory();
			String director = vd.getDirector();
			int year = vd.getYear();
			int runtime = vd.getRuntime();
			String rated = vd.getRated();
			String notes = vd.getNotes();
			byte[] image = vd.getImageBytes();
			byte myRating = vd.getMyRating();
			Log.i("Interfaces", "my rating: " + myRating);
			Intent next = new Intent(view.getContext(), ViewActivity.class);
			next.putExtra("title", title);
		    next.putExtra("category", category);
		    next.putExtra("director", director);
		    next.putExtra("year", year);
		    next.putExtra("runtime", runtime);
		    next.putExtra("rating", rated);
		    next.putExtra("notes", notes);  
		    next.putExtra("image", image);
		    next.putExtra("myRating", myRating);
			startActivity(next);   
		}
    }
}
