package service;

import tools.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvenementService {
    private final Connection cnx;

    public EvenementService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    // 🔎 Vérifie si un événement avec l'ID donné existe dans la base de données
    public boolean evenementExiste(int idEvenement) throws SQLException {
        String query = "SELECT 1 FROM evenement WHERE id_evenement = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, idEvenement);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }
}