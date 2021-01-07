package com.example.agenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda.R;
import com.example.agenda.dao.AlunoDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaAlunosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        AlunoDAO dao = new AlunoDAO();

        setTitle("Lista de alunos");

        FloatingActionButton botaoNovoAluno = findViewById(R.id.activity_lista_alunos_fab_novo_aluno);
        botaoNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaAlunosActivity.this,
                        FormularioAlunoActivity.class));
            }
        });

        ListView listaDeAlunos = findViewById(R.id.activity_lista_alunos_listview);
        listaDeAlunos.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dao.todos()));

    }
}
