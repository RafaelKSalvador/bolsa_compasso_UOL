package com.example.agenda.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.agenda.model.Telefone;

@Dao
public interface TelefoneDAO {
    @Query("SELECT * FROM Telefone " +
            "WHERE alunoId = :alunoId LIMIT 1")
    Telefone buscaPrimeiroTelefoneDoAluno(int alunoId);
}
