package services;

import models.Profil;

import java.sql.SQLException;
import java.util.List;


    public interface Iprofil <T>
    {
        void addProfil(T p) throws SQLException;
        Profil getProfilByUserId(int userId) throws SQLException;
        void updateProfil(T p) throws SQLException;
        void deleteProfil(int userId) throws SQLException;


    }


