package br.com.lkm.extrator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.lkm.extrator.entity.Corporation;

@Repository
public interface CorporationRepository extends JpaRepository<Corporation, Integer>{

	List<Corporation> findByMailConfigurationNotNull();

	@Query("select c from Corporation c where c.lastJobExecution < current_timestamp - retroativeDays")
	List<Corporation> findAllRequired();

}
