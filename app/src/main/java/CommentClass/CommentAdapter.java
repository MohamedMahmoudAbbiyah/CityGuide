package CommentClass;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cityguide.PostClass.Post;
import com.cityguide.R;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {

    private List<Comment> comments;
    private Activity context;

    public CommentAdapter(List<Comment> comments, Activity context) {
        super(context, R.layout.custom_view_comment,comments);
        this.comments = comments;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view_comment, null, true);
        TextView usernameText = customView.findViewById(R.id.custom_comment_username);
        TextView locationnameText = customView.findViewById(R.id.custom_comment_locationname);
        TextView ratingText = customView.findViewById(R.id.custom_comment_rating);
        TextView commentText = customView.findViewById(R.id.custom_comment_comment);

        Comment comment = comments.get(position);

        usernameText.setText(comment.getUsername());
        locationnameText.setText(comment.getLocationLocation());
        ratingText.setText(""+comment.getRate());
        commentText.setText(comment.getComment());





        return customView;
    }
}
