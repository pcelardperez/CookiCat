package com.example.pedro.cookicat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;


public class ReconocimientoVoz extends Fragment {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
    private static final int RESULT_OK = -1;
    private Button bt_start;
    private ArrayList<String> ingredientes;
    private Vector<String> cantidades;
    private Vector<String> unidades;

    private DBHelper BD;


    public ReconocimientoVoz(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_reconocimiento_voz, container, false);

        //Relacionamos el boton con el XML
        bt_start = (Button)rootView.findViewById(R.id.button1);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lanzamos el reconoimiento de voz
                startVoiceRecognitionActivity();
            }
        });
        //Recogemos todos los ingredientes, cantidades y unidades en los vectores
        try {
            getDatos();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return rootView;
    }




    private void startVoiceRecognitionActivity() {
        // Definición del intent para realizar en análisis del mensaje
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Indicamos el modelo de lenguaje para el intent
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Definimos el mensaje que aparecerá
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Diga, Añadir...");
        // Lanzamos la actividad esperando resultados
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    //Recogemos los resultados del reconocimiento de voz
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Si el reconocimiento a sido bueno
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
            //El intent nos envia un ArrayList aunque en este caso solo
            //utilizaremos la pos.0
            ArrayList<String> matches = data.getStringArrayListExtra
                    (RecognizerIntent.EXTRA_RESULTS);
            //Separo el texto en palabras.
            String [ ] palabras = matches.get(0).toString().split(" ");
            //Si la primera palabra es LLAMAR
            if(palabras[0].equals("añadir")){
                int noenc=0;
                for(int a=0;a<ingredientes.size();a++){
                    //Busco el nombre que es la tercera posicion (LLAMAR A LORENA)

                    if(ingredientes.get(a).equalsIgnoreCase(palabras[1])){
                        noenc=1;
                        //Si la encuentra recojo el numero telf en el otro
                        //vector que coincidira con la posicion ya que
                        //los hemos rellenado a la vez.
                        showToast(ingredientes.get(a).toString());
                        //ENVIAMOS LOS DATOS AL ITEMLISTACTIVITY
                        /*
                        Intent intent = new Intent(getActivity(), ItemListActivity.class);
                        Contenido ingredient = new Contenido(ingredientes.get(a),"2","ud");
                        intent.putExtra("ingrediente", ingredient);
                        getActivity().startActivity(intent);
                        */
                        break;
                    }else{
                        //showToast("Por favor repita la palabra...MIAU!");
                    }
                }

                if(noenc==0){
                    showToast("Por favor repita la palabra...MIAU!");
                }
            }
        }
    }
    //Con el getDatos lo que hago es recoger los nombres
    //de la SIM en un vector
    //Y los numeros de telefonos en otro vector, eso sí tienen que coincidir
    //las posiciones de uno y de otro, por eso los relleno a la vez.
    private void getDatos() throws SQLException {
        /*
        ingredientes = new Vector<String>();
        cantidades = new Vector<String>();
        unidades = new Vector<String>();

        ingredientes.add("pollo");
        ingredientes.add("cebolla");
        */
        //Creamos y abrimos la base de datos
        BD=new DBHelper(getActivity());
        BD.openDataBase();

        //Insertamos una nueva alarma con valores _id=1, alarma=1, evento=1
        //BD.insertAlarma(1, 1, 1);

        //Modificamos la alarma anterior dejándola como _id=1, alarma=2, evento=3
        //BD.updateAlarma(1, 2, 3);

        //Obtenemos la alarma creada anteriormente
        //Alarma alarma = BD.getAlarma(1);

        //Borramos la alarma creada anteriormente con índice 1
        //BD.removeAlarma(1)

        //Obtenemos un listado de todas las alarmas

        ingredientes =(ArrayList<String>) BD.getIngredientes();

        ArrayList <String> pruebaingredientes= (ArrayList<String>) BD.getIngredientes();

        }

        @Override
        public void onPause() {
            super.onPause();
            BD.close();
        }
        @Override
        public void onResume() {
            super.onResume();
            try {
                BD.openDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




    protected void showToast(String ingrediente){
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, ingrediente, duration);
        toast.show();
    }
}
