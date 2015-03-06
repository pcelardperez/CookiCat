package com.example.pedro.cookicat.dummy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Contenido implements Serializable {

    private String itemIngrediente;
    private String itemCantidad;
    private String itemUnidad;

    public Contenido(String itemIngrediente, String itemCantidad,String itemUnidad){
        this.itemIngrediente=itemIngrediente;
        this.itemCantidad=itemCantidad;
        this.itemUnidad=itemUnidad;
    }

    public String getItemIngrediente() {
        return itemIngrediente;
    }

    public void setItemIngrediente(String itemIngrediente) {
        this.itemIngrediente = itemIngrediente;
    }

    public String getItemCantidad() {
        return itemCantidad;
    }

    public void setItemCantidad(String itemCantidad) {
        this.itemCantidad = itemCantidad;
    }

    public String getItemUnidad() {
        return itemUnidad;
    }

    public void setItemUnidad(String itemCantidad) {
        this.itemUnidad = itemUnidad;
    }


    /**
     * An array of sample (dummy) items.
     */
    /*
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();
*/
    /**
     * A map of sample (dummy) items, by ID.
     */
    /*
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "Pollo"));
        addItem(new DummyItem("2", "Cebolla"));
        addItem(new DummyItem("3", "Patatas"));
        addItem(new DummyItem("3", "Ajo"));
        addItem(new DummyItem("4", "Sal"));
        addItem(new DummyItem("5", "Zanahoria"));
        addItem(new DummyItem("6", "Pepino"));
        addItem(new DummyItem("7", "Cbellina"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
*/
    /**
     * A dummy item representing a piece of content.
     */
    /*
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
    */
}
