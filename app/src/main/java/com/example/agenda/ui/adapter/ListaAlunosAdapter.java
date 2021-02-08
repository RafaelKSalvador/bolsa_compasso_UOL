package com.example.agenda.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agenda.R;
import com.example.agenda.asynctask.BuscaPrimeiroTelefoneDoAlunoTask;
import com.example.agenda.database.AgendaDatabase;
import com.example.agenda.database.dao.TelefoneDAO;
import com.example.agenda.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class ListaAlunosAdapter extends BaseAdapter {
    private final List<Aluno> alunos = new ArrayList<>();
    private final Context contenxt;
    private final TelefoneDAO dao;

    public ListaAlunosAdapter(Context contenxt) {
        this.contenxt = contenxt;
        dao = AgendaDatabase.getInstance(contenxt).getTelefoneDAO();
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
        View viewCriada = criaView(viewGroup);
        Aluno alunoDevolvido = alunos.get(position);
        vincula(viewCriada, alunoDevolvido);
        return viewCriada;
    }

    private void vincula(View view, Aluno aluno) {
        TextView nome = view.findViewById(R.id.item_aluno_nome);
        nome.setText(aluno.getNome());
        TextView telefone = view.findViewById(R.id.item_aluno_telefone);
        new BuscaPrimeiroTelefoneDoAlunoTask(dao, aluno.getId(), telefoneEncontrado -> {
            telefone.setText(telefoneEncontrado.getNumero());
        }).execute();
    }

    private View criaView(ViewGroup viewGroup) {
        return LayoutInflater.from(contenxt)
                .inflate(R.layout.item_aluno, viewGroup, false);
    }

    public void atualiza(List<Aluno> alunos) {
        this.alunos.clear();
        this.alunos.addAll(alunos);
        notifyDataSetChanged();
    }

    public void remove(Aluno aluno) {
        alunos.remove(aluno);
        notifyDataSetChanged();
    }
}
