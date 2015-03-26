package com.example.materialmenu;

import java.util.Random;

import com.example.library.MaterialMenu;
import com.example.library.MaterialMenuView;
import com.example.library.MaterialMenuDrawable.IconState;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends Activity implements View.OnClickListener{

	private MaterialMenuView materialMenuView;
    private int              materialButtonState;
    private DrawerLayout     drawerLayout;
    private boolean          direction;
	private static int generated = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		        materialMenuView = (MaterialMenuView)findViewById(R.id.material_menu_button);
		        materialMenuView.setOnClickListener(this);
		        materialMenuView.setState(IconState.ARROW);
		    }

		    @Override public void onClick(View v) {
		        final int id = v.getId();
		        switch (id) {
		            case R.id.material_menu_button:
		                setMainState();
		                break;
		        }
		    }


		    private void setMainState() {
		        materialButtonState = generateState(materialButtonState);
		        materialMenuView.animatePressedState(intToState(materialButtonState));
		    }

		    public void refreshDrawerState() {
		        this.direction = drawerLayout.isDrawerOpen(Gravity.START);
		    }

		    public static int generateState(int previous) {
//		    	generated++;
//		    	if (generated > 5) {
//		    		generated = 0;
//				}
		    	generated = new Random().nextInt(6);
		        return generated != previous ? generated : generateState(previous);
		    }

		    public static IconState intToState(int state) {
		        switch (state) {
		            case 0:
		                return IconState.BURGER;
		            case 1:
		            	return IconState.ARROW;
		            case 2:
		            	return IconState.X;
		            case 3:
		            	return IconState.CHECK;
		            case 4:
		            	return IconState.UP;
		            case 5:
		            	return IconState.DOWN;
		        }
		        throw new IllegalArgumentException("Must be a number [0,5)");
		    }
	
}
