package com.forateq.cloudcheetah.adapters;

/**
 * Created by Vallejos Family on 5/13/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.fragments.ProjectsComponentsContainerFragment;
import com.forateq.cloudcheetah.models.Projects;

import java.util.List;


/**
 * This adapter is used
 * Created by Vallejos Family on 2/25/2016.
 */
public class ProjectCustomAdapter extends BaseAdapter {
    private static LayoutInflater inflater=null;
    private List<Projects> itemsList;
    public static final String TAG = "ProjectCustomAdapter";

    public ProjectCustomAdapter(Context context, List<Projects> itemsList) {
        // TODO Auto-generated constructor stub
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemsList = itemsList;
    }

    /**
     * Adds project on the list of projects
     * @param projects
     */
    public void addItem(Projects projects){
        itemsList.add(projects);
        notifyDataSetChanged();
    }

    public void removeItem(int project_id){
        for(Projects projects:itemsList){
            if(projects.getProject_id() == project_id){
                itemsList.remove(projects);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * Returns the size of the list of projects
     * @return size of list
     */
    public int getSize(){
        return itemsList.size();
    }

    /**
     * Clears all the items in the list of projects
     */
    public void clearItems(){
        itemsList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView project_id_tv;
        TextView project_status_tv;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.project_item_view, null);
        holder.tv=(TextView) rowView.findViewById(R.id.project_name);
        holder.project_id_tv = (TextView) rowView.findViewById(R.id.project_id);
        holder.project_status_tv = (TextView) rowView.findViewById(R.id.status);
        holder.tv.setText(itemsList.get(position).getName());
        holder.project_id_tv.setText(""+itemsList.get(position).getProject_id());
        holder.project_status_tv.setVisibility(View.VISIBLE);
        holder.project_status_tv.setText(itemsList.get(position).getStatus());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectsComponentsContainerFragment projectsComponentsContainerFragment = new ProjectsComponentsContainerFragment();
                MainActivity.replaceFragment(projectsComponentsContainerFragment, TAG);
            }
        });
        return rowView;
    }


}

