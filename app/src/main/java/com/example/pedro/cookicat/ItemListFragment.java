package com.example.pedro.cookicat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import com.example.pedro.cookicat.dummy.Contenido;
import com.example.pedro.cookicat.dummy.CustomAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

    private DBHelper BD;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    CustomAdapter adapter;
    private List<Contenido> rowItems;
    Contenido nuevoIngrediente=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        return inflater.inflate(R.layout.list_fragment,null,false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        rowItems = new ArrayList<Contenido>();
/*
            rowItems = new ArrayList();
            rowItems.add(new Contenido("Pollo","1","ud"));
            rowItems.add(new Contenido("Cebolla","2","ud"));
            rowItems.add(new Contenido("Patatas","3","ud"));
            rowItems.add(new Contenido("Ajo","2","ud"));
            rowItems.add(new Contenido("Sal","2","cuch"));
            rowItems.add(new Contenido("Zanahoria","4","ud"));
            rowItems.add(new Contenido("Pepino","1","ud"));
            rowItems.add(new Contenido("Cbellina","3","ud"));
            rowItems.add(new Contenido("Caldo","200","ml"));
*/

        try {
            BD=new DBHelper(getActivity());
            BD.openDataBase();

            rowItems =(ArrayList<Contenido>) BD.getReceta();

            ArrayList <Contenido> pruebaingredientes= (ArrayList<Contenido>) BD.getReceta();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        adapter = new CustomAdapter(getActivity(),rowItems);
        setListAdapter(adapter);
        /*
        if(getArguments().containsKey("ingrediente")) {
            //mItem = Contenido.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Contenido nuevoIngr = (Contenido) getArguments().getSerializable("ingrediente");
            showToast(nuevoIngr.getItemIngrediente());
            if (nuevoIngrediente != null) {
                añadirElemento(nuevoIngr);
            }
        }
        */
        BD.close();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, final int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        //mCallbacks.onItemSelected(rowItems.get(position).toString());
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.ingrediente_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String ingrediente = rowItems.get(position).getItemIngrediente();
                Toast.makeText(getActivity(),"You Clicked : " + item.getTitle() + " Para: "+ingrediente,Toast.LENGTH_SHORT).show();
                BD=new DBHelper(getActivity());
                try {
                    BD.openDataBase();
                    BD.borrar_ingrediente_receta(ingrediente);
                    BD.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */


    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    protected void showToast(String ingrediente){
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, "Desde ListFragment"+ingrediente, duration);
        toast.show();
    }

}
