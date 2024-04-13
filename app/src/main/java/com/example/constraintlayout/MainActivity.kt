package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener,
    View.OnClickListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtNumPessoas: EditText
    private lateinit var textResult: TextView

    private lateinit var  btnTalk: FloatingActionButton
    private lateinit var  btnShare: FloatingActionButton

    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTalk = findViewById(R.id.button_talk)
        btnShare = findViewById(R.id.button_share)

        edtConta = findViewById<EditText>(R.id.edit_conta)
        edtConta.addTextChangedListener(this)

        edtNumPessoas = findViewById<EditText>(R.id.edit_num_pessoas)
        edtNumPessoas.addTextChangedListener(this)

        textResult = findViewById<EditText>(R.id.text_result)

        btnTalk.setOnClickListener(this)
        btnShare.setOnClickListener(this)


        // Initialize TTS engine
        tts = TextToSpeech(this, this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM24", "Depois de mudar")

        var value: Double
        var people:Int

        if(s.toString().length > 0) {
            //value = s.toString().toDouble()

            try {
                people = edtNumPessoas.text.toString().toInt()
            }catch (e:NumberFormatException ){
                people = 0
            }
            try {
                value = edtConta.text.toString().toDouble()
            }catch (e:NumberFormatException ){
                value = 0.0
            }

           textResult.text = "R$ ${calResult(value, people).toString()}"
            Log.d("PDM24", "p: " + people)
            Log.d("PDM24", "v: " + value)
        //    edtConta.setText("9")
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.button_talk -> clickTalk()
            R.id.button_share -> clickShare()
        }
    }

    fun clickTalk(){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            val textSpeak = textResult.text.toString()
            tts.speak(textSpeak, TextToSpeech.QUEUE_FLUSH, null)

        }
    }

    fun clickShare(){
        Log.d ("PDM23", "Share")

        val textShare = "Vamos Rachar! O valor para cada um é de: R$ " + textResult.text.toString() + ". Valor total: R$ " + edtConta.text.toString() + " para " + edtNumPessoas.text.toString() + " pessoas."
        val intent    = Intent()

        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, textShare)
        startActivity(Intent.createChooser(intent,"Compartilhar"))

    }


    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }

    private fun calResult(value: Double?, people: Int?):Double{
        var result: Double = 0.0

        if (value != null && value != 0.0 && people != null && people != 0) {
            result = (value / people)
        }

        return result
    }



}

