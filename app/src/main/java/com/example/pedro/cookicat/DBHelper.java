package com.example.pedro.cookicat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pedro.cookicat.dummy.Contenido;
import com.example.pedro.cookicat.dummy.Ingrediente;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 19/02/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    //Ruta por defecto de las bases de datos en el sistema Android
    private static String DB_PATH = "/data/data/com.example.pedro.cookicat/databases/";

    private static String DB_NAME = "CookiCatDataBase.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Toma referencia hacia el contexto de la aplicación que lo invoca para poder acceder a los 'assets' y 'resources' de la aplicación.
     * Crea un objeto DBOpenHelper que nos permitirá controlar la apertura de la base de datos.
     * @param context
     */

    public DBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;

    }
    /**
     * Crea una base de datos vacía en el sistema y la reescribe con nuestro fichero de base de datos.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
                //checkDataBase();

        if (dbExist) {
            //la base de datos existe y no hacemos nada.
            Log.d("midebug", "LA BASE DE DATOS YA EXISTE, NO HACEMOS NADA");
        } else {
            //Llamando a este método se crea la base de datos vacía en la ruta por defecto del sistema
            //de nuestra aplicación por lo que podremos sobreescribirla con nuestra base de datos.
            this.getWritableDatabase();

            try {

                copyDataBase();
                Log.d("midebug", "COPIANDO BASE DE DATOS");
            } catch (IOException e) {
                throw new Error("Error copiando Base de Datos");
            }
        }
    }
        /**
         * Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicación.
         * @return true si existe, false si no existe
         */
        private boolean checkDataBase(){

            SQLiteDatabase checkDB = null;

            try{

                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

            }catch(SQLiteException e){

        //si llegamos aqui es porque la base de datos no existe todavía.

            }
            if(checkDB != null){

                checkDB.close();

            }
            return checkDB != null ? true : false;
        }

            /**
             * Copia nuestra base de datos desde la carpeta assets a la recién creada
             * base de datos en la carpeta de sistema, desde dónde podremos acceder a ella.
             * Esto se hace con bytestream.
             * */
        private void copyDataBase() throws IOException{

            //Abrimos el fichero de base de datos como entrada
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            //Ruta a la base de datos vacía recién creada
            String outFileName = DB_PATH + DB_NAME;

            //Abrimos la base de datos vacía como salida
            OutputStream myOutput = new FileOutputStream(outFileName);

            //Transferimos los bytes desde el fichero de entrada al de salida
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Liberamos los streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }

        public void openDataBase() throws SQLException {

            //Abre la base de datos
            try {
                createDataBase();
            } catch (IOException e) {
                throw new Error("Ha sido imposible crear la Base de Datos");
            }

            String myPath = DB_PATH + DB_NAME;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }

        @Override
        public synchronized void close() {
            if(myDataBase != null)
                myDataBase.close();
            super.close();
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //myDataBase=db;

        }
        /**
         * A continuación se crearán los métodos de lectura, inserción, actualización
         * y borrado de la base de datos.
         * */


        //Establecemos los nombres de las columnas

    public static final String ID_INGR = "_id_ingrediente";
    public static final String NOMBRE_INGR = "nombre";
    public static final String TIPO_INGR = "tipo";
    public static final String TABLE_INGR ="ingredientes";

        //Array de strings para su uso en los diferentes métodos
    private static final String[] colsIngr = new String[] {ID_INGR, NOMBRE_INGR, TIPO_INGR};


    public Ingrediente getIngrediente(long _rowIndex) {
        Ingrediente ingr = new Ingrediente();
        Cursor result = myDataBase.query(true, TABLE_INGR,
                colsIngr,
                ID_INGR + "=" + _rowIndex, null, null, null,
                null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
        //Si la alarma no existe, devuelve una alarma con valores -1 y -1
            ingr = new Ingrediente(null,null);

        } else {
            if (result.moveToFirst()) {
                ingr = new Ingrediente(
                        result.getString(result.getColumnIndex(NOMBRE_INGR)),
                        result.getString(result.getColumnIndex(TIPO_INGR))
                );
            }
        }
        return ingr;
    }

    public List<String> getIngredientes() {
        ArrayList<String> ingredient = new ArrayList();
        Cursor result = myDataBase.query(TABLE_INGR,
                colsIngr, null, null, null, null, ID_INGR);
        if (result.moveToFirst())
            do {

                ingredient.add(result.getString(result.getColumnIndex(NOMBRE_INGR)));
            } while(result.moveToNext());
        return ingredient;
    }

    public ArrayList<Contenido> getReceta(){
        ArrayList<Contenido> ingredientes = new ArrayList();

        Cursor resultingr = myDataBase.rawQuery("SELECT i.nombre, r.cantidad, r.unidad FROM ingredientes i, contenido_recetas r WHERE i._id_ingrediente=r.id_ingrediente and r.id_receta=1",null);

        if (resultingr.moveToFirst())
            do {

                //String idReceta = resultingr.getString(0);

                //EJEMPLO PARA PELEAR
                String nombre = resultingr.getString(0);
                String cantidad = resultingr.getString(1);
                String unidad=resultingr.getString(2);

                System.out.println("PREGUNTANDO IDS DE INGREDIENTES"+nombre+cantidad+unidad);
                ingredientes.add(new Contenido(nombre, cantidad, unidad));
            }while(resultingr.moveToNext());
        return ingredientes;
    }

    public void borrar_ingrediente_receta(String ingr){
        if(myDataBase.delete("contenido_recetas", "id_ingrediente=13",null)==0){
            Log.d("midebug", "No se ha borrado ingrediente");
        }else{
            Log.d("midebug", "Ingrediente borrado");
        }
        myDataBase.close();
        ItemListActivity LA = new ItemListActivity();
        LA.reiniciarFragmentLista();

        //return myDataBase.delete("contenido_recetas","id_ingrediente"+"="+1,null )>0;
    }
}
