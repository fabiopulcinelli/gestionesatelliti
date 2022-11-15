package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {
	@Query(value="SELECT * FROM satellite WHERE TIMESTAMPDIFF(year, ?1, dataLancio) < -2 AND NOT stato = 'DISATTIVATO'", nativeQuery=true)
	List<Satellite> findLanciatiPiuDi2Anni(Date data);
	
	@Query(value="FROM Satellite WHERE stato = 'DISATTIVATO' AND dataRientro IS NULL")
	List<Satellite> findDisattivatiMaNonRientrati();
	
	@Query(value="SELECT * FROM satellite WHERE TIMESTAMPDIFF(year, ?1, dataLancio) < -10 AND stato = 'FISSO'", nativeQuery=true)
	List<Satellite> findRimastiInOrbita10AnniFissi(Date data);
}
