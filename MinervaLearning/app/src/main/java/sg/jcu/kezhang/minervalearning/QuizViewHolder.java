package sg.jcu.kezhang.minervalearning;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class QuizViewHolder extends RecyclerView.ViewHolder {

    // Declared Instance Variables.
    TextView word, definition;
    ImageView deleteButton;

    View view;

    /** Creates ViewHolder for the RecycleView Adaptor which initialise graphic Layout of one item.
     * @param itemView The view for one item that will generated in RecycleView.
     */
    public QuizViewHolder(View itemView)
    {
        // Inherit constructor from super class.
        super(itemView);

        // Define components of the item and match them with XML layout.
        word
                = itemView
                .findViewById(R.id.word);

        definition
                = itemView
                .findViewById(R.id.definition);

        deleteButton
                = itemView
                .findViewById(R.id.delete_icon);

        view = itemView;
    }
}
