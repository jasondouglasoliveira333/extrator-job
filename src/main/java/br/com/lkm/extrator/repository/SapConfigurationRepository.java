package br.com.lkm.extrator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.lkm.extrator.entity.SapConfiguration;

@Repository
public interface SapConfigurationRepository extends JpaRepository<SapConfiguration, Integer>{

	public List<SapConfiguration> findByDestinationName(String destinationName);

}
