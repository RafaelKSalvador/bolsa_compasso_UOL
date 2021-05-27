package com.example.newsnow.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.newsnow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.util.*

@Suppress("DEPRECATION")
class CadastroActivity : AppCompatActivity() {

    private lateinit var fba: FirebaseAuth
    var escolhefoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        fba = FirebaseAuth.getInstance()

        cadastrar_botao.setOnClickListener {
            cadastroUsuario()
        }

        texto_login_nova_conta.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        img_cirular_cadastro.setOnClickListener {
            abreGaleria()
        }

    }

    private fun cadastroUsuario() {
        if (cadastro_nome.text.toString().isEmpty()){
            cadastro_nome.error = "Por favor, digite um nome."
            cadastro_nome.requestFocus()
            return
        }
        if (cadastro_email.text.toString().isEmpty()) {
            cadastro_email.error = "Por favor, digite um email."
            cadastro_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(cadastro_email.text.toString()).matches()) {
            cadastro_email.error = "Por favor, digite um email válido."
            cadastro_email.requestFocus()
            return
        }
        if (cadastro_senha.text.toString().isEmpty()) {
            cadastro_senha.error = "Por favor, digite sua senha."
            cadastro_senha.requestFocus()
            return
        }

        fba.createUserWithEmailAndPassword(
            cadastro_email.text.toString(),
            cadastro_senha.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = fba.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                uploadImagemParaFireB()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, "Erro no login. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("MainActivity", "Photo was selected")
            escolhefoto = data.data
            img_cirular_cadastro.setImageURI(escolhefoto)
        }
    }

    //sobe imagem para o firebase
    private fun uploadImagemParaFireB() {
        if (escolhefoto == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(escolhefoto!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File Location: $it")

                    salvaUsuarioDatabaseFireB(it.toString())
                }
            }
            .addOnFailureListener {

            }
    }

    private fun salvaUsuarioDatabaseFireB(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, cadastro_nome.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")
            }
    }

    private fun abreGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }
}

class User(val uid: String, val name: String, val profileImageUrl: String) {
    constructor() : this("", "", "")
}
