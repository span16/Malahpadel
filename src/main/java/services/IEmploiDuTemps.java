// IEmploiDuTemps.java
package services;

import models.EmploiDuTemps;
import java.sql.SQLException;
import java.util.List;

public interface IEmploiDuTemps {
    void ajouter(EmploiDuTemps edt) throws SQLException;
    void modifier(EmploiDuTemps edt) throws SQLException;
    void supprimer(int id) throws SQLException;
    List<EmploiDuTemps> recuperer() throws SQLException;

    // Renommez ceci pour correspondre à "recupererParId" dans l'implémentation
    EmploiDuTemps recupererParId(int id) throws SQLException;
}
