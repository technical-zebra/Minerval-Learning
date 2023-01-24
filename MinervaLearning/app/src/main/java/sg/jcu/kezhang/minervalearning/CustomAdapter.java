package sg.jcu.kezhang.minervalearning;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<QuizViewHolder> {

    // Declare variables
    private final ArrayList<Term> terms;
    Context context;

    /***
     * Constructor
     * @param dataSet ArrayList of terms
     * @param context Context
     */
    public CustomAdapter(ArrayList<Term> dataSet, Context context)
    {
        this.terms = dataSet;
        this.context = context;
    }

    /***
     * Create view holder
     * @param parent ViewGroup
     * @param viewType int
     * @return QuizViewHolder
     */
    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater  = LayoutInflater.from(context);

        // Inflate the carpark item layout.
        View photoView
                = inflater
                .inflate(R.layout.card_word,
                        parent, false);

        return new QuizViewHolder(photoView);
    }

    /***
     * Bind view holder
     * @param viewHolder QuizViewHolder
     * @param position int
     */
    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(final QuizViewHolder viewHolder, final int position)
    {
        /* Get information from ArrayList of Carpark objects and implement each on
        the CarparkViewHolder template. */
        viewHolder.word
                .setText(terms.get(position).getWord());
        viewHolder.definition
                .setText(terms.get(position).getDefinition());

        viewHolder.deleteButton.setOnClickListener(view -> {
            QuizDatabaseHelper db = new QuizDatabaseHelper(context);
            db.removeWord(terms.get(position).getWord());
            db.close();

            terms.remove(position);
            notifyDataSetChanged();
        });

    }

    /***
     * Get item count
     * @return int
     */
    @Override
    public int getItemCount()
    {
        return terms.size();
    }


}
