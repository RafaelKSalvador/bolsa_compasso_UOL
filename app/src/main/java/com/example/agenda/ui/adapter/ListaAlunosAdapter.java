package com.example.agenda.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agenda.R;
import com.example.agenda.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class ListaAlunosAdapter extends BaseAdapter {
    private final List<Aluno> alunos = new ArrayList<>();
    private Context contenxt;

    public ListaAlunosAdapter(Context contenxt) {
        this.contenxt = contenxt;
    }

    @Override
    public int getCount() { /*representa a quantidades de elementos que havera no adapter*/
        return alunos.size();
    }

    @Override
    public Aluno getItem(int position) { /*representa qual o item que iremos pegar com base em uma posicao*/
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position) { /*representa o id do que elemento que estamos pegando*/
        return alunos.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) { /*representa a view que estaremos apresentando para cada elemento do adapter*/
        View viewCriada = LayoutInflater.from(contenxt)
                .inflate(R.layout.item_aluno, viewGroup, false);
        Aluno alunoDevolvido = alunos.get(position);
        TextView nome = viewCriada.findViewById(R.id.item_aluno_nome);
        nome.setText(alunoDevolvido.getNome());
        TextView telefone = viewCriada.findViewById(R.id.item_aluno_telefone);
        telefone.setText(alunoDevolvido.getTelefone());
        return viewCriada;
    }

    public void clear() {
        alunos.clear();
    }

    public void addAll(List<Aluno> todos) {
        this.alunos.addAll(alunos);
    }

    public void remove(Aluno aluno) {
        alunos.remove(aluno);
    }
}
