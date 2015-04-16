package demo.af.jarrah.com.zoomslidingmenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import demo.af.jarrah.com.libslidingmenu.SlidingMenu;


public class ActivityMain extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content);

        SlidingMenu slidingMenu = new SlidingMenu(this);
        View menu = getLayoutInflater().inflate(R.layout.layout_menu, null);
        slidingMenu.attachToActivity(this, menu, getResources().getDrawable(R.drawable.test_bg));

    }

}
