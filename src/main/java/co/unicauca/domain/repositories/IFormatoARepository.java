/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.domain.repositories;

import co.unicauca.domain.entities.FormatoA;
import java.util.Optional;

/**
 *
 * @author AN515-54-55MX
 */
public interface IFormatoARepository {
    FormatoA save(FormatoA proyecto);

    Optional<FormatoA> findById(String titulo);

    void update(FormatoA proyecto);
}
