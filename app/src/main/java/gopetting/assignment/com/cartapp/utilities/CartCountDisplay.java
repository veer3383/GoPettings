package gopetting.assignment.com.cartapp.utilities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by viraj on 4/6/16.
 */
public class CartCountDisplay {
    public void displaycartCount(int count, TextView cartcount, UserSession session) {
        if (count > 0) {
            cartcount.setVisibility(View.VISIBLE);
            if (count > 9) {
                cartcount.setText(String.valueOf(count));
            } else {
                cartcount.setText(" " + String.valueOf(count) + " ");
            }
        } else {
            cartcount.setVisibility(View.GONE);
        }
        session.Storecartcount(String.valueOf(count));
    }
    public void animateCart(ImageView carticon, TextView cartcount)
    {
        YoYo.with(Techniques.Bounce).duration(700).playOn(carticon);
        YoYo.with(Techniques.SlideInRight).duration(700).playOn(cartcount);
    }
}
