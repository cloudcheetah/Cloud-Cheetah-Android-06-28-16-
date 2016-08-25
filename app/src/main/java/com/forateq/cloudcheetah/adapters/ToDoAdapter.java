package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.ToDo;

import java.util.List;

/**
 * Created by PC1 on 8/18/2016.
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>{

    private List<ToDo> listTodo;
    private Context mContext;
    public static final String TAG = "AccountsAdapter";

    /**
     * This is the class contsructor user to create new instance of this adapter
     * @param listTodo
     * @param context
     */
    public ToDoAdapter(List<ToDo> listTodo, Context context) {
        this.listTodo = listTodo;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_todo, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ToDo toDo = listTodo.get(i);
        viewHolder.toDoNoteET.setText(toDo.getNote());
        viewHolder.toDoTimeTV.setText(toDo.getTime());
        viewHolder.toDoIdTV.setText(""+toDo.getId());
    }

    @Override
    public int getItemCount() {
        return listTodo == null ? 0 : listTodo.size();
    }


    /**
     * This method is used to clear all the items in the arraylist of resources
     */
    public void clearItems(){
        listTodo.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add new item in the arraylist of project resources
     * @param toDo
     */
    public void addItem(ToDo toDo){
        listTodo.add(toDo);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        MaterialRippleLayout rippleLayout;
        TextView toDoIdTV;
        TextView toDoTimeTV;
        TextView toDoNoteET;

        public ViewHolder(View itemView) {
            super(itemView);
            toDoIdTV = (TextView) itemView.findViewById(R.id.todo_id);
            toDoTimeTV = (TextView) itemView.findViewById(R.id.time);
            toDoNoteET = (TextView) itemView.findViewById(R.id.todo_note);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
